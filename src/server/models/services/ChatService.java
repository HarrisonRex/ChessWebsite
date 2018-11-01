package server.models.services;

import server.Console;
import server.DatabaseConnection;
import server.models.Chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ChatService {

    public static String selectAllInto(List<Chat> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT chatId, gameId, p1Notes, p2Notes FROM Chat"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Chat(results.getInt("chatId"), results.getInt("gameId"), results.getString("p1Notes"), results.getString("p2Notes")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Chat' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
        return "OK";
    }

    public static Chat selectById(int id) {
        Chat result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT chatId, gameId, p1Notes, p2Notes FROM Chat WHERE chatId = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Chat(results.getInt("chatId"), results.getInt("gameId"), results.getString("p1Notes"), results.getString("p2Notes"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Chat' table: " + resultsException.getMessage();

            Console.log(error);
        }
        return result;
    }

    public static String insert(Chat itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Chat (chatId, gameId, p1Notes, p2Notes) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getChatId());
            statement.setInt(2, itemToSave.getGameId());
            statement.setString(3, itemToSave.getP1Notes());
            statement.setString(4, itemToSave.getP2Notes());






            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Chat' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String update(Chat itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Chat SET gameId = ?, p1Notes = ?, p2Notes = ? WHERE chatId = ?"
            );
            statement.setInt(1, itemToSave.getGameId());
            statement.setString(2, itemToSave.getP1Notes());
            statement.setString(3, itemToSave.getP2Notes());






            statement.setInt(4, itemToSave.getChatId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Chat' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Chat WHERE chatId = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Chat' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

}