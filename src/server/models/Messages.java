package server.models;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Messages {
    private int messageId;
    private int chatId;
    private String author;
    private String words;


    public static ArrayList<Messages> messagess = new ArrayList<>();

    public Messages(int messageId, int chatId, String author, String words) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.author = author;
        this.words = words;
    }

    public static int nextId() {
        int id = 0;
        for (Messages m : messagess) {
            if (m.getMessageId() > id) {
                id = m.getMessageId();
            }
        }
        return id + 1;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("messageId", getMessageId());
        j.put("chatId", getChatId());
        j.put("author", getAuthor());
        j.put("words", getWords());

        return j;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}