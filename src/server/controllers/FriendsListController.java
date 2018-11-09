package server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Console;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import server.models.FriendsList;
import server.models.User;
import server.models.services.FriendsListService;
import server.models.services.UserService;

@Path("friendsList/")
public class FriendsListController {

    @POST
    @Path("new")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String newFriend (@FormParam("newFriendFormFriend") String friendUN,
                             @CookieParam("sessionToken") Cookie sessionCookie) {

        FriendsListService.selectAllInto(FriendsList.friendslists);

        int creator = 0;
        int addedFriend = 0;
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
            }else if(u.getName().toLowerCase().equals(friendUN.toLowerCase())) {
                addedFriend = u.getId();
            }
        }
        if (creator==0){
            return "Error: Can't find current user.";
        }else if (addedFriend==0){
            return "Error: Can't find opponent.";
        }
        int flID = FriendsList.nextId();

        for (FriendsList fl: FriendsList.friendslists) {
            if (fl.getUser() == creator && fl.getUser2() == addedFriend) {
                return "Error you are already friends";
            }else if (fl.getUser() == addedFriend && fl.getUser2() == creator){
                return "Error you are already friends";
            }
        }

        Console.log("Adding " + friendUN + " as a friend for " + maker);
        String success = FriendsListService.insert(new FriendsList(flID, creator, addedFriend, 1, maker, friendUN));

        if (!success.equals("OK")) {
            return "Error: Can't create new game.";
        }
        return success;
    }



    @SuppressWarnings("unchecked")
    private String getFriendsList() {
        JSONArray friendsListList = new JSONArray();
        for (FriendsList fl: FriendsList.friendslists) {
            friendsListList.add(fl.toJSON());
        }
        return friendsListList.toString();
    }
    @GET
    @Path("list")
    @Produces (MediaType.APPLICATION_JSON)
    public String listFriends(@CookieParam("sessionToken") Cookie sessionCookie) {

        if (UserService.validateSessionCookie(sessionCookie) == null) {
            return "Error: Invalid user session token";
        }

        Console.log("/friendsList/list - Getting all friends from the database");
        String status = FriendsListService.selectAllInto(FriendsList.friendslists);
        if (status.equals("OK")){
            return getFriendsList();
        }else{
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }



    @SuppressWarnings("unchecked")
    private String getPendingFriendsList(int cUser) {
        JSONArray friendsListList = new JSONArray();
            for (FriendsList fl: FriendsList.friendslists) {
                if(fl.getPending()==1) {
                    if(fl.getUser2()==cUser)
                        friendsListList.add(fl.toJSON());
                }
            }
        return friendsListList.toString();
    }
    @GET
    @Path("pendingList")
    @Produces (MediaType.APPLICATION_JSON)
    public String pendingListFriends(@CookieParam("sessionToken") Cookie sessionCookie) {

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


        Console.log("/friendsList/list - Getting all friends from the database");
        String status = FriendsListService.selectAllInto(FriendsList.friendslists);
        if (status.equals("OK")){
            return getPendingFriendsList(cookieUser);
        }else{
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }
}
