package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class FriendsList {
    private int friendsListId;
    private int user;
    private int user2;


    public static ArrayList<FriendsList> friendslists = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (FriendsList f: friendslists) {
            if (f.getFriendsListId() > id) {
                id = f.getFriendsListId();
            }
        }
        return id + 1;
    }

    public FriendsList(int friendsListId, int user, int user2) {
        this.friendsListId = friendsListId;
        this.user = user;
        this.user2 = user2;
    }


    public int getFriendsListId() {
        return friendsListId;
    }

    public void setFriendsListId(int friendsListId) {
        this.friendsListId = friendsListId;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("friendsListId", getFriendsListId());
        j.put("user", getUser());
        j.put("user2", getUser2());
        return j;
    }
}








