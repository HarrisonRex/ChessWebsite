package server.controllers;

import server.BCrypt;
import server.Console;
import server.models.User;
import server.models.services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("user/")
public class UserController {
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String newMessage(@FormParam("username") String name,
                             @FormParam("email") String email,
                             @FormParam("password1") String password1,
                             @FormParam("password2") String password2) {
        Console.log("/user/new - Creating new user: " + name);
        UserService.selectAllInto(User.users);
        for (User u: User.users) {
            if (u.getName().toLowerCase().equals(name.toLowerCase())) {
                return "Error: Username already exists";
            }
        }
        if (!password1.equals(password2)) {
            return "Error: Passwords do not match.";
        }
        String token = UUID.randomUUID().toString();
        int userId = User.nextId();
        String password = BCrypt.hashpw(password1,BCrypt.gensalt());
        String success = UserService.insert(new User(userId, 1500, name, password, email, token));

        if (success.equals("OK")) {
            return token;
        } else {
            return "Error: Can't create new user.";
        }
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String newMessage(@FormParam("username") String username,
                             @FormParam("password") String password ) {

        Console.log("/user/login - Attempt by user: " + username +".");
        UserService.selectAllInto(User.users);
        for (User u: User.users) {
            if (u.getName().toLowerCase().equals(username.toLowerCase())) {
                boolean tester = (BCrypt.checkpw(password, u.getPassword()));
                if (!tester) {
                    return "Error: Incorrect password";
                }
                String token = UUID.randomUUID().toString();
                u.setSessionToken(token);
                String success = UserService.update(u);
                if (success.equals("OK")) {
                    Console.log("Login success for user: " + username +".");
                    return token;
                } else {
                    return "Error: Can't create session token.";
                }
            }
        }
        return "Error: Can't find user account.";
    }

    @GET
    @Path("get")
    @Produces(MediaType.TEXT_PLAIN)
    public String getUser(@CookieParam("sessionToken") Cookie sessionCookie) {

        String currentUser = UserService.validateSessionCookie(sessionCookie);

        if (currentUser == null) {
            return "";
        } else {
            return currentUser;
        }
    }
}
