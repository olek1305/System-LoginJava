package org.main.idle;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ArcyCoins {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/Arcy_RPG";
    private static final String DATABASE_USER = "postgres";
    private static final String DATABASE_PASSWORD = "test123";
    private static Connection connection;

    // Pozostałe metody ArcyCoins

    public static void connect() throws SQLException, ClassNotFoundException {
        if (connection == null || connection.isClosed()) {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        }
    }



    public static void giveCoins(Player player, int amount) {
        String playerName = player.getName();
        String insertCoinsQuery = "UPDATE arcycoins SET coins = coins + ? WHERE player_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(insertCoinsQuery)) {
            statement.setInt(1, amount);
            statement.setString(2, playerName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getCoins(Player player) throws SQLException {
        String playerName = player.getName();
        String checkPlayerExistsQuery = "SELECT coins FROM arcycoins WHERE player_name = ?;";

        try (PreparedStatement statement = connection.prepareStatement(checkPlayerExistsQuery)) {
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int coins = resultSet.getInt("coins");
                return coins;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    public static void createPlayerIfNotExists(Player player) throws SQLException {
        String playerName = player.getName();
        String checkPlayerExistsQuery = "SELECT * FROM arcycoins WHERE player_name = ?;";

        try (PreparedStatement statement = connection.prepareStatement(checkPlayerExistsQuery)) {
            statement.setString(1, playerName);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                String insertPlayerQuery = "INSERT INTO arcycoins (player_name, coins) VALUES (?, 0);";

                try (PreparedStatement insertStatement = connection.prepareStatement(insertPlayerQuery)) {
                    insertStatement.setString(1, playerName);
                    insertStatement.executeUpdate();
                }
            }
        }
    }

    public static void createTableIfNotExists() {
        String createTableQuery =
                "CREATE TABLE IF NOT EXISTS arcycoins (" +
                "id SERIAL PRIMARY KEY," +
                "player_name TEXT NOT NULL," +
                "coins INT NOT NULL" +
                ");";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableQuery);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
