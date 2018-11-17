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
                return "Error: you are already friends";
            }else if (fl.getUser() == addedFriend && fl.getUser2() == creator){
                return "Error: you are already friends";
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
    private String getFriendsList(int cUser) {
        JSONArray friendsListList = new JSONArray();
        for (FriendsList fl: FriendsList.friendslists) {
            if(fl.getPending()==0) {
                if(fl.getUser2()==cUser) {
                    friendsListList.add(fl.toJSON(fl.getUser1UN()));
                } else if(fl.getUser()==cUser){
                    friendsListList.add(fl.toJSON(fl.getUser2UN()));
                }
            }
        }
        return friendsListList.toString();
    }
    @GET
    @Path("list")
    @Produces (MediaType.APPLICATION_JSON)
    public String listFriends(@CookieParam("sessionToken") Cookie sessionCookie) {

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
            String endFriendList = getFriendsList(cookieUser);
            if (endFriendList.equals("[]")){
                JSONObject eCheck = new JSONObject();
                eCheck.put("empty", "empty");
                return eCheck.toString();
            } else {
                return endFriendList;
            }
        }else{
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }



    @GET
    @Path("loadOpoDD")
    @Produces (MediaType.APPLICATION_JSON)
    public String loadOpoDD(@CookieParam("sessionToken") Cookie sessionCookie) {

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
        String status = FriendsListService.selectAllInto((FriendsList.friendslists));
        if(status.equals("OK")){
            String endDD = getFriendsList(cookieUser);
            if (endDD.equals("[]")){
                JSONObject eCheck = new JSONObject();
                eCheck.put("empty", "empty");
                return eCheck.toString();
            } else {
                return endDD;
            }
        }else{
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }


    @POST
    @Path("delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteFriend(@FormParam("friendId") int friendId,
                                      @CookieParam("sessionToken") Cookie sessionCookie) {
        String currentUsername = UserService.validateSessionCookie(sessionCookie);
        if (currentUsername == null) return "Error: Invalid user session token";

        Console.log("/friendsList/delete of friendsListId: " + friendId);
        FriendsList friend = FriendsListService.selectById(friendId);
        if (friend == null) {
            return "This entry in friendsList doesn't exist";
        } else {
            if (friend.getUser2UN().equals(currentUsername)) {
                return FriendsListService.deleteById(friendId);
            } else if (friend.getUser1UN().equals(currentUsername)) {
                return FriendsListService.deleteById(friendId);
            }else{
                return "That's not your friend to delete";
            }
        }
    }



    @SuppressWarnings("unchecked")
    private String getPendingFriendsList(int cUser) {
        JSONArray friendsListList = new JSONArray();
        for (FriendsList fl: FriendsList.friendslists) {
            if(fl.getPending()==1) {
                if(fl.getUser2()==cUser) {
                    friendsListList.add(fl.toJSON(""));
                }
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
            String endPendingList = getPendingFriendsList(cookieUser);
            if (endPendingList.equals("[]")){
                JSONObject ePendCheck = new JSONObject();
                ePendCheck.put("empty", "empty");
                return ePendCheck.toString();
            } else {
                return endPendingList;
            }
        }else{
            JSONObject response = new JSONObject();
            response.put("error", status);
            return response.toString();
        }
    }



    @POST
    @Path("pendingDelete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String deletePendingFriend(@FormParam("pendingFriendId") int pendingFriendId,
                                @CookieParam("sessionToken") Cookie sessionCookie) {
        String currentUsername = UserService.validateSessionCookie(sessionCookie);
        if (currentUsername == null) return "Error: Invalid user session token";

        Console.log("/friendsList/pendingDelete of friendsListId: " + pendingFriendId);
        FriendsList pendingFriend = FriendsListService.selectById(pendingFriendId);
        if (pendingFriend == null) {
            return "This entry in friendsList doesn't exist";
        } else {
            if (!pendingFriend.getUser2UN().equals(currentUsername)) {
                return "That's not your friend to delete";
            }
            return FriendsListService.deleteById(pendingFriendId);
        }
    }



    @POST
    @Path("pendingAccept")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String acceptPendingFriend (@FormParam("pendingFriendId") int pendingFriendId,
                                @CookieParam("sessionToken") Cookie sessionCookie) {
        String currentUsername = UserService.validateSessionCookie(sessionCookie);
        if (currentUsername == null) return "Error: Invalid user session token";

        Console.log("/friendsList/pendingAccept - friendsListId: " + pendingFriendId);
        FriendsList pendingFriend = FriendsListService.selectById(pendingFriendId);
        if (pendingFriend == null) {
            return "This entry in friendsList doesn't exist";
        } else {
            if (!pendingFriend.getUser2UN().equals(currentUsername)) {
                return "That's not your friend to delete";
            }
            pendingFriend.setPending(0);
            return FriendsListService.update(pendingFriend);
        }

    }
}
