package server.controllers;

import server.Console;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Cookie;
import server.models.Games;
import server.models.services.GamesService;
import server.models.User;
import server.models.services.UserService;

@Path("games/")
public class GamesController {
    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String newGame (@FormParam("player2") String player2UN,
                           @FormParam("ownerWhite") int ownWhite,
                           @CookieParam("sessionToken") Cookie sessionCookie) {

        int creator = 0;
        int opo = 0;
        int whoWhite = 0;
        String maker;
        String currentUser = UserService.validateSessionCookie(sessionCookie);
        if (currentUser == null) {
            return "Error: Can't find you.";
        } else {
            maker = currentUser;
        }
        UserService.selectAllInto(User.users);
        for (User u: User.users) {
            if(u.getName().toLowerCase().equals(maker.toLowerCase())){
                creator = u.getId();
            }else if(u.getName().toLowerCase().equals(player2UN.toLowerCase())) {
                opo = u.getId();
            }
        }

        if (creator==0){
            return "Error: Can't find current user.";
        }else if (opo==0){
            return "Error: Can't find opponent.";
        }

        if (ownWhite==1){
            whoWhite = 1;
        }else if (ownWhite!=1){
            whoWhite = 0;
        }
        int gId = Games.nextId();
        Console.log("Creating new game for: " + maker + " vs " + player2UN + ". " + gId);
        String moveHist = "";
        String success = GamesService.insert(new Games(gId, creator, opo, whoWhite, moveHist));

        if (!success.equals("OK")) {
            return "Error: Can't create new game.";
        }
        return success;
    }
}
