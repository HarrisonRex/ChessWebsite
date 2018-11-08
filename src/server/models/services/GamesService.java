package server.models.services;

import server.Console;
import server.DatabaseConnection;
import server.models.Games;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GamesService {

    public static String selectAllInto(List<Games> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT gameId, Owner, player2, OwnerWhite, MoveHistory FROM Games"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Games(results.getInt("gameId"), results.getInt("Owner"), results.getInt("player2"), results.getInt("OwnerWhite"), results.getString("MoveHistory")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Games' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
        return "OK";
    }

    public static Games selectById(int id) {
        Games result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT gameId, Owner, player2, OwnerWhite, MoveHistory FROM Games WHERE gameId = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Games(results.getInt("gameId"), results.getInt("Owner"), results.getInt("player2"), results.getInt("OwnerWhite"), results.getString("MoveHistory"));


                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Games' table: " + resultsException.getMessage();

            Console.log(error);
        }
        return result;
    }

    public static String insert(Games itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Games (gameId, Owner, player2, OwnerWhite, MoveHistory) VALUES (?, ?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getGameId());
            statement.setInt(2, itemToSave.getOwner());
            statement.setInt(3, itemToSave.getPlayer2());
            statement.setInt(4, itemToSave.getOwnerWhite());
            statement.setString(5, itemToSave.getMoveHistory());





            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Games' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String update(Games itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Games SET Owner = ?, player2 = ?, OwnerWhite = ?, MoveHistory = ? WHERE gameId = ?"
            );
            statement.setInt(1, itemToSave.getOwner());
            statement.setInt(2, itemToSave.getPlayer2());
            statement.setInt(3, itemToSave.getOwnerWhite());
            statement.setString(4, itemToSave.getMoveHistory());





            statement.setInt(5, itemToSave.getGameId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Games' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Games WHERE gameId = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Games' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

}