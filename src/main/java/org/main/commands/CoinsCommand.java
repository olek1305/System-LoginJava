package org.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.main.API.MyCommandExecutor;
import org.main.API.MySQLAPI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoinsCommand implements CommandExecutor {

    private Plugin plugin;

    public CoinsCommand(Plugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                player.sendMessage("This command doesn't accept any arguments.");
                return true;
            }

            if (MyCommandExecutor.cooldowns.containsKey(player)) {
                MyCommandExecutor.Cooldown cooldown = MyCommandExecutor.cooldowns.get(player);
                if (cooldown.isOnCooldown()) {
                    player.sendMessage("You still have to wait " + cooldown.getTimeLeft() + " seconds before using this command.");
                    return true;
                }
            }

            try {
                int coins = getCoins(player);
                player.sendMessage("You have " + coins + " ArcyCoins.");

                MyCommandExecutor.Cooldown cooldown = new MyCommandExecutor.Cooldown(MyCommandExecutor.COOLDOWN_TIME);
                cooldown.setCooldown();
                MyCommandExecutor.cooldowns.put(player, cooldown);
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage("An error occurred while downloading your account balance.");
            }
        } else {
            sender.sendMessage("This command is only available to players.");
        }
        return true;
    }


    public static int getCoins(Player player) throws SQLException {
        String playerName = player.getName();
        String checkPlayerExistsQuery = "SELECT coins FROM arcycoins WHERE player_name = ?;";

        try (Connection connection = MySQLAPI.getConnection();
             PreparedStatement statement = connection.prepareStatement(checkPlayerExistsQuery)) {
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
}