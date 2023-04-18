package org.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Tutaj umieść instrukcje, które mają być wyświetlone dla gracza
            player.sendMessage("§6§lWelcome to ArcyRPG!");
            player.sendMessage("§6§lHere are some basic commands:");
            player.sendMessage("§e/register §f- §6§lRegister your account using the provided token");

        } else {
            sender.sendMessage("This command is only available to players.");
        }
        return true;
    }
}
