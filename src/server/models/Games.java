package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Games {
    private int gameId;
    private int owner;
    private int player2;
    private boolean ownerWhite;
    private String moveHistory;


    public static ArrayList<Games> gamess = new ArrayList<>();

    public Games(int gameId, int owner, int player2, boolean ownerWhite, String moveHistory) {
        this.gameId = gameId;
        this.owner = owner;
        this.player2 = player2;
        this.ownerWhite = ownerWhite;
        this.moveHistory = moveHistory;
    }

    public static int nextId() {
        int id = 0;
        for (Games g : gamess) {
            if (g.getGameId() > id) {
                id = g.getGameId();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("gameId", getGameId());
        j.put("owner", getOwner());
        j.put("player2", getPlayer2());
        j.put("ownerWhite", getOwnerWhite());
        j.put("moveHistory", getMoveHistory());


        return j;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public boolean getOwnerWhite() {
        return ownerWhite;
    }

    public void setOwnerWhite(boolean ownerWhite) {
        this.ownerWhite = ownerWhite;
    }

    public String getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(String moveHistory) {
        this.moveHistory = moveHistory;
    }
}