package org.main.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.main.API.MySQLAPI;

import java.sql.*;
import java.util.UUID;

import static org.main.API.MySQLAPI.getConnection;

public class PlayerJoinListener implements Listener {

    private static final String CHECK_USER_QUERY = "SELECT uuid FROM users WHERE uuid = ?";
    private static final String CREATE_USER_QUERY = "INSERT INTO users (uuid, name) VALUES (?, 'default')";

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String uuidString = event.getPlayer().getUniqueId().toString();
        String playerName = event.getPlayer().getName();
        UUID uuid = UUID.fromString(uuidString);
        if (!userExists(uuidString)) {
            createUser(uuid, playerName);
        }
    }

    private boolean userExists(String uuid) {
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_QUERY)) {
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createUser(UUID uuid, String playerName) {
        try (Connection connection = MySQLAPI.getConnection()) {
            String insertUserQuery = "INSERT INTO users (uuid, name, created_at) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE uuid = ?, name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertUserQuery)) {
                preparedStatement.setString(1, uuid.toString());
                preparedStatement.setString(2, playerName);
                preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setString(4, uuid.toString());
                preparedStatement.setString(5, playerName);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}