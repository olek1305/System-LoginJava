package org.main;

import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;


import org.main.commands.ArcyCoinsCommand;
import org.main.commands.GUICommand;
import org.main.commands.MyCommandExecutor;
import org.main.events.PlayerJoinListener;
import org.main.idle.ArcyCoins;
import org.main.idle.ZombieListener;


public final class Arcy_RPG extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Arcy_RPG enabled.");
        try {
            ArcyCoins.connect();
            ArcyCoins.createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

            getCommand("gui").setExecutor(new GUICommand());
            getServer().getPluginManager().registerEvents(new ZombieListener(this), this);
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
            this.getCommand("arcycoins").setExecutor(new ArcyCoinsCommand(this));
        }
        @Override
        public void onDisable () {
            getLogger().info("Arcy_RPG disabled.");
        }
}
