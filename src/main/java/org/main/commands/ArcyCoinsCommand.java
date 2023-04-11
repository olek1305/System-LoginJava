package org.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.main.idle.ArcyCoins;

import java.sql.SQLException;

public class ArcyCoinsCommand implements CommandExecutor {

    private Plugin plugin;

    public ArcyCoinsCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (MyCommandExecutor.cooldowns.containsKey(player)) {
                MyCommandExecutor.Cooldown cooldown = MyCommandExecutor.cooldowns.get(player);
                if (cooldown.isOnCooldown()) {
                    player.sendMessage("Musisz poczekać jeszcze " + cooldown.getTimeLeft() + " sekund przed użyciem tej komendy.");
                    return true;
                }
            }

            try {
                int coins = ArcyCoins.getCoins(player);
                player.sendMessage("Posiadasz " + coins + " ArcyCoins.");

                // ustawienie cooldownu na 32 sekund, mozna inaczej wpisac
                // new MyCommandExecutor.Cooldown(32 * 1000);
                MyCommandExecutor.Cooldown cooldown = new MyCommandExecutor.Cooldown(MyCommandExecutor.COOLDOWN_TIME);
                cooldown.setCooldown();
                MyCommandExecutor.cooldowns.put(player, cooldown);
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage("Wystąpił błąd podczas pobierania Twojego stanu konta.");
            }
        } else {
            sender.sendMessage("Ta komenda jest dostępna tylko dla graczy.");
        }
        return true;
    }
}