package org.main.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.main.API.MySQLAPI;
import org.main.Arcy_RPG;

import java.security.SecureRandom;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class RegisterCommand implements CommandExecutor {
    private static final long COOLDOWN_TIME = 10 * 60 * 1000; // 10 minutes in milliseconds
    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final LuckPerms luckPerms;

    public RegisterCommand(Arcy_RPG plugin) {
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        // Check for cooldown
        if (cooldowns.containsKey(player.getUniqueId())) {
            long lastUsed = cooldowns.get(player.getUniqueId());
            long timeLeft = COOLDOWN_TIME - (System.currentTimeMillis() - lastUsed);

            if (timeLeft > 0) {
                player.sendMessage("You need to wait " + (timeLeft / 1000) + " seconds before using this command again.");
                return true;
            }
        }

        if (!player.hasPermission("systemLogin.register")) {
            player.sendMessage("You don't have permission to use this command.");
            return true;
        }

        String name = player.getName();
        UUID uuid = player.getUniqueId();

        // Generate a unique token
        String token = generateToken();

        // Save the token, name, and UUID to the database
        boolean success = saveTokenToDatabase(token, name, uuid);

        if (success) {
            // Send the token to the player
            player.sendMessage("Your registration token is: " + token);

            // Create a link with the token
            String registrationUrl = "http://TuLink/register?token=" + token;
            TextComponent linkMessage = new TextComponent("Click here to register with a token.");
            linkMessage.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, registrationUrl));
            linkMessage.setColor(net.md_5.bungee.api.ChatColor.GREEN);

            player.spigot().sendMessage(ChatMessageType.CHAT, linkMessage);
        } else {
            player.sendMessage("An error occurred while generating the token. Please try again later.");
        }

        // Update the cooldown
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        // Check for permission
        if (!player.hasPermission("systemLogin.register")) {
            player.sendMessage("You do not have permission to use this command.");
            return true;
        }
        return true;
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    private boolean saveTokenToDatabase(String token, String name, UUID uuid) {
        String insertTokenQuery = "INSERT INTO tokens (token, name, expires_at, uuid) VALUES (?, ?, ?, ?)";


        try (Connection connection = MySQLAPI.getConnection();
             PreparedStatement statement = connection.prepareStatement(insertTokenQuery)) {
            statement.setString(1, token);
            statement.setString(2, name);

            LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(10);
            statement.setTimestamp(3, Timestamp.valueOf(expiresAt));

            statement.setString(4, uuid.toString());

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void removeExpiredTokens() {
        String deleteExpiredTokensQuery = "DELETE FROM tokens WHERE expires_at < CURRENT_TIMESTAMP";

        try (Connection connection = MySQLAPI.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteExpiredTokensQuery)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static class TokenCleanupTask extends BukkitRunnable {
        private final RegisterCommand registerCommand;

        public TokenCleanupTask(RegisterCommand registerCommand) {
            this.registerCommand = registerCommand;
        }
        @Override
        public void run() {
            registerCommand.removeExpiredTokens();
        }
    }
}
