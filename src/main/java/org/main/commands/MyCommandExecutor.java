package org.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MyCommandExecutor implements CommandExecutor {
    //Ta klasa robi cooldown
    public static final Map<Player, Cooldown> cooldowns = new HashMap<>();
    public static long COOLDOWN_TIME = 30 * 1000; // 30 sekund w milisekundach

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostępna tylko dla graczy.");
            return true;
        }

        Player player = (Player) sender;
        Cooldown cooldown = cooldowns.getOrDefault(player, new Cooldown(COOLDOWN_TIME));

        if (cooldown.isOnCooldown()) {
            player.sendMessage("Musisz poczekać jeszcze " + cooldown.getTimeLeft() + " sekund przed użyciem tej komendy.");
        } else {
            // Wykonaj swoją logikę
            player.sendMessage("Wykonywanie komendy...");

            // Ustaw cooldown na gracza
            cooldown.setCooldown();
            cooldowns.put(player, cooldown);
        }

        return true;
    }

    public static class Cooldown {
        private final long cooldownTime;
        private long lastUsed;

        public Cooldown(long cooldownTime) {
            this.cooldownTime = cooldownTime;
            this.lastUsed = 0;
        }

        public boolean isOnCooldown() {
            return (System.currentTimeMillis() - lastUsed) < cooldownTime;
        }

        public long getTimeLeft() {
            return (cooldownTime - (System.currentTimeMillis() - lastUsed)) / 1000;
        }

        public void setCooldown() {
            this.lastUsed = System.currentTimeMillis();
        }
    }
}
