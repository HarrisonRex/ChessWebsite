package server.models.services;

import server.Console;
import server.DatabaseConnection;
import server.models.FriendsList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FriendsListService {

    public static String selectAllInto(List<FriendsList> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT friendsListId, User, User2 FROM FriendsList"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new FriendsList(results.getInt("friendsListId"), results.getInt("User"), results.getInt("User2")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'FriendsList' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
        return "OK";
    }

    public static FriendsList selectById(int id) {
        FriendsList result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT friendsListId, User, User2 FROM FriendsList WHERE friendsListId = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new FriendsList(results.getInt("friendsListId"), results.getInt("User"), results.getInt("User2"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'FriendsList' table: " + resultsException.getMessage();

            Console.log(error);
        }
        return result;
    }

    public static String insert(FriendsList itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO FriendsList (friendsListId, User, User2) VALUES (?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getFriendsListId());
            statement.setInt(2, itemToSave.getUser());
            statement.setInt(3, itemToSave.getUser2());







            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'FriendsList' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String update(FriendsList itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE FriendsList SET User = ?, User2 = ? WHERE friendsListId = ?"
            );
            statement.setInt(1, itemToSave.getUser());
            statement.setInt(2, itemToSave.getUser2());







            statement.setInt(3, itemToSave.getFriendsListId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'FriendsList' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM FriendsList WHERE friendsListId = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'FriendsList' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

}