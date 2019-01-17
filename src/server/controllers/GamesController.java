package server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Console;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Cookie;
import server.models.Games;
import server.models.services.GamesService;
import server.models.User;
import server.models.services.UserService;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;

@Path("games/")
public class GamesController {

    //Response from the server for making a new game
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
        String pgnSetup;
        String log;
        String maker;
        Date date = new Date();
        String Cdate = new SimpleDateFormat("yyyy.MM.dd").format(date);

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
        }
        int gId = Games.nextId();
        if(whoWhite==1){
            log = "Creating new game with " + maker + " as white VS " + player2UN + " as black.";
            pgnSetup = "[Event \"?\"]\n" +
                    "[Site \"Cheesy Chess\"]\n" +
                    "[Date \"" + Cdate + "\"]\n" +
                    "[Round \"?\"]\n" +
                    "[White \"" + maker + "\"]\n" +
                    "[Black \"" + player2UN + "\"]\n" +
                    "[Result \"*\"] \n" +
                    "\n";
        }else{
            log = "Creating new game with " + maker + " as black VS " + player2UN + " as white.";
            pgnSetup = "[Event \"?\"]\n" +
                    "[Site \"Cheesy Chess\"]\n" +
                    "[Date \"" + Cdate + "\"]\n" +
                    "[Round \"?\"]\n" +
                    "[White \"" + player2UN + "\"]\n" +
                    "[Black \"" + maker + "\"]\n" +
                    "[Result \"*\"] \n" +
                    "\n";
        }

        Console.log(log);
        String success = GamesService.insert(new Games(gId, creator, opo, whoWhite, pgnSetup));

        if (!success.equals("OK")) {
            return "Error: Can't create new game.";
        }
        return success;
    }

    //The list user to show all the games to the user
    @SuppressWarnings("unchecked")
    private String getGamesList(int cUser) {

        JSONArray gamesList  = new JSONArray();

        for (Games g: Games.gamess) {
            if (g.getOwner() == cUser) {
                for (User u : User.users) {
                    if (u.getId() == g.getPlayer2()) {
                        gamesList.add(g.toJSON(u.getName()));
                    }
                }
            } else if (g.getPlayer2() == cUser) {
                for (User u : User.users) {
                    if (u.getId() == g.getOwner()) {
                        gamesList.add(g.toJSON(u.getName()));
                    }
                }
            }
        }
        return gamesList.toString();
    }

    //The servers response and gathering of all games related to the user
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
            if(gamesList.equals("[]")){
                JSONObject eCheck = new JSONObject();
                eCheck.put("empty", "empty");
                return eCheck.toString();
            } else {
                return gamesList;
            }
        } else {
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }

    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteFriend(@FormParam("gameId") int gameId,
                               @CookieParam("sessionToken") Cookie sessionCookie) {

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

        Console.log("/games/delete of game: " + gameId);
        Games game = GamesService.selectById(gameId);
        if (game == null) {
            return "This game doesn't exist";
        } else {
            if (game.getOwner()==(cookieUser)) {
                return GamesService.deleteById(gameId);
            } else if (game.getPlayer2()==(cookieUser)) {
                return GamesService.deleteById(gameId);
            }else{
                return "That's not your game to delete";
            }
        }
    }

    @GET
    @Path("getOne")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String getGame(@FormParam("gameId") int gameId) {
        Console.log("/games/getOne for id: " + gameId);
        Games game = GamesService.selectById(gameId);
        String gamePlay;
        if (game == null) {
            return "Error: This game doesn't exist";
        } else {
            gamePlay = game.getMoveHistory();
            return gamePlay;
        }
    }
}
