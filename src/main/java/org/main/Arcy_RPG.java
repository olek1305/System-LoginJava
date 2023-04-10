package org.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.main.shop.ShopGUI;

public final class Arcy_RPG extends JavaPlugin {
    private ShopGUI shopGUI;

    @Override
    public void onEnable() {
        getLogger().info("Arcy_RPG enabled.");
        // Initialize the ShopGUI object
        shopGUI = new ShopGUI(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Arcy_RPG disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("shop") && sender instanceof Player) {
            Player player = (Player) sender;
            // Open the shop GUI using the ShopGUI object
            shopGUI.open(player);
            return true;
        }
        return false;
    }
}
