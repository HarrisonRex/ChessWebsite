package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Chat {
    private int chatId;
    private int gameId;
    private String p1Notes;
    private String p2Notes;

    public static ArrayList<Chat> chats = new ArrayList<>();

    public Chat(int chatId, int gameId, String p1Notes, String p2Notes) {
        this.chatId = chatId;
        this.gameId = gameId;
        this.p1Notes = p1Notes;
        this.p2Notes = p2Notes;
    }

    public static int nextId() {
        int id = 0;
        for (Chat c : chats) {
            if (c.getChatId() > id) {
                id = c.getChatId();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("chatId", getChatId());
        j.put("gameId", getGameId());
        j.put("p1Notes", getP1Notes());
        j.put("p2Notes", getP2Notes());

        return j;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getP1Notes() {
        return p1Notes;
    }

    public void setP1Notes(String p1Notes) {
        this.p1Notes = p1Notes;
    }

    public String getP2Notes() {
        return p2Notes;
    }

    public void setP2Notes(String p2Notes) {
        this.p2Notes = p2Notes;
    }
}