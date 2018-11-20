package server.controllers;

import org.json.simple.JSONArray;
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
    public String newGame (@FormParam("newGameFormPlayer2") String player2UN,
                           @FormParam("newGameFormOwnerWhite") int ownWhite,
                           @CookieParam("sessionToken") Cookie sessionCookie) {

        GamesService.selectAllInto(Games.gamess);

        int creator = 0;
        int opo = 0;
        int whoWhite = 0;
        String log;
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
        if(whoWhite==1){
            log = "Creating new game with " + maker + " as white VS " + player2UN + " as black.";
        }else{
            log = "Creating new game with " + maker + " as black VS " + player2UN + " as white.";
        }
        Console.log(log);
        String success = GamesService.insert(new Games(gId, creator, opo, whoWhite, null));

        if (!success.equals("OK")) {
            return "Error: Can't create new game.";
        }
        return success;
    }


    @SuppressWarnings("unchecked")
    private String getGamesList(int cUser) {
        JSONArray gamesList  = new JSONArray();
        for (Games g: Games.gamess) {
            if(g.getOwner()==cUser){
                gamesList.add(g.toJSON());
            }else if(g.getPlayer2()==cUser){
                gamesList.add(g.toJSON());
            }
        }
        return gamesList.toString();
    }
    @GET
    @Path("list")
    @Produces (MediaType.APPLICATION_JSON)
    public String listGames(@CookieParam("sessionToken") Cookie sessionCookie) {
        int cookieUser = 0;
        String cookieUserName = UserService.validateSessionCookie(sessionCookie);
        if (cookieUserName == null) {
            return "Error: Invalid user session token";
        } else {
            UserService.selectAllInto(User.users);
            for (User u: User.users) {
                if (u.getName().toLowerCase().equals(cookieUserName.toLowerCase())) {
                    cookieUser = u.getId();
                }
            }
        }
        Console.log("/games/list - Getting all games from the database");
        String status = GamesService.selectAllInto(Games.gamess);
        if (status.equals("OK")){
            String gamesList = getGamesList(cookieUser);
        }


        return "";
    }
}
