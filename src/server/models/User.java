package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class User {
    private int id;
    private int rank;
    private String name;
    private String password;
    private String email;
    private String sessionToken;


    public User(int id, int rank, String name, String password, String email, String sessionToken) {
        this.id = id;
        this.rank = rank;
        this.name = name;
        this.password = password;
        this.email = email;
        this.sessionToken = sessionToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public static ArrayList<User> users = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (User u : users) {
            if (u.getId() > id) {
                id = u.getId();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("id", getId());
        j.put("rank", getRank());
        j.put("name", getName());
        j.put("password", getPassword());
        j.put("email", getEmail());
        j.put("sessionToken", getSessionToken());


        return j;
    }
}

