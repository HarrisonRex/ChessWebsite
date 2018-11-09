package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class FriendsList {
    private int friendsListId;
    private int user;
    private int user2;
    private int pending;
    private String user1UN;
    private String user2UN;


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

    public FriendsList(int friendsListId, int user, int user2, int pending, String user1UN, String user2UN) {
        this.friendsListId = friendsListId;
        this.user = user;
        this.user2 = user2;
        this.pending = pending;
        this.user1UN = user1UN;
        this.user2UN = user2UN;
    }


    public String getUser1UN() {
        return user1UN;
    }

    public void setUser1UN(String user1UN) {
        this.user1UN = user1UN;
    }

    public String getUser2UN() {
        return user2UN;
    }

    public void setUser2UN(String user2UN) {
        this.user2UN = user2UN;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
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
        j.put("pending", getPending());
        j.put("user1UN", getUser1UN());
        j.put("user2UN", getUser2UN());
        return j;
    }
}








