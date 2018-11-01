package server.models.services;

import server.Console;
import server.DatabaseConnection;
import server.models.Messages;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessagesService {

    public static String selectAllInto(List<Messages> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT messageId, chatId, Author, Words FROM Messages"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Messages(results.getInt("messageId"), results.getInt("chatId"), results.getString("Author"), results.getString("Words")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
        return "OK";
    }

    public static Messages selectById(int id) {
        Messages result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT messageId, chatId, Author, Words FROM Messages WHERE messageId = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Messages(results.getInt("messageId"), results.getInt("chatId"), results.getString("Author"), results.getString("Words"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
        }
        return result;
    }

    public static String insert(Messages itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Messages (messageId, chatId, Author, Words) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getMessageId());
            statement.setInt(2, itemToSave.getChatId());
            statement.setString(3, itemToSave.getAuthor());
            statement.setString(4, itemToSave.getWords());






            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String update(Messages itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Messages SET chatId = ?, Author = ?, Words = ? WHERE messageId = ?"
            );
            statement.setInt(1, itemToSave.getChatId());
            statement.setString(2, itemToSave.getAuthor());
            statement.setString(3, itemToSave.getWords());






            statement.setInt(4, itemToSave.getMessageId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Messages WHERE messageId = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

}