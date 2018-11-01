package server.models.services;

import server.Console;
import server.DatabaseConnection;
import server.models.User;

import javax.ws.rs.core.Cookie;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    public static String selectAllInto(List<User> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT id, rank, name, password, email, sessionToken FROM Users"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new User(results.getInt("id"), results.getInt("rank"), results.getString("name"), results.getString("password"), results.getString("email"), results.getString("sessionToken")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Users' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
        return "OK";
    }

    public static User selectById(int id) {
        User result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT id, rank, name, password, email, sessionToken FROM Users WHERE id = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new User(results.getInt("id"), results.getInt("rank"), results.getString("name"), results.getString("password"), results.getString("email"), results.getString("sessionToken"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Users' table: " + resultsException.getMessage();

            Console.log(error);
        }
        return result;
    }

    public static String insert(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Users (id, rank, name, password, email, sessionToken) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getId());
            statement.setInt(2, itemToSave.getRank());
            statement.setString(3, itemToSave.getName());
            statement.setString(4, itemToSave.getPassword());
            statement.setString(5, itemToSave.getEmail());
            statement.setString(6, itemToSave.getSessionToken());




            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Users' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String update(User itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Users SET rank = ?, name = ?, password = ?, email = ?, sessionToken = ? WHERE id = ?"
            );
            statement.setInt(1, itemToSave.getRank());
            statement.setString(2, itemToSave.getName());
            statement.setString(3, itemToSave.getPassword());
            statement.setString(4, itemToSave.getEmail());
            statement.setString(5, itemToSave.getSessionToken());




            statement.setInt(6, itemToSave.getId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Users' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Users WHERE id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Users' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String validateSessionCookie(Cookie sessionCookie) {
        if (sessionCookie != null) {
            String token = sessionCookie.getValue();
            String result = UserService.selectAllInto(User.users);
            if (result.equals("OK")) {
                for (User u : User.users) {
                    if (u.getSessionToken().equals(token)) {
                        Console.log("Valid session token received.");
                        return u.getName();
                    }
                }
            }
        }
        Console.log("Error: Invalid user session token");
        return null;
    }

}