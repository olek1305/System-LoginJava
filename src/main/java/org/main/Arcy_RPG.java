package org.main;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;
import org.main.commands.CoinsCommand;
import org.main.commands.HelpCommand;
import org.main.commands.RegisterCommand;
import org.main.events.PlayerJoinListener;

import static org.main.API.MySQLAPI.connect;


public final class Arcy_RPG extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Arcy_RPG enabled.");
        try {
            connect();
        } catch (SQLException e) {
            getLogger().severe("Failed to connect to the database. Check your connection settings.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        } catch (ClassNotFoundException e) {
            getLogger().severe("MariaDB Driver not found. Disabling the plugin.");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }



        getCommand("register").setExecutor(new RegisterCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        //getCommand("coins").setExecutor(new CoinsCommand(this));
        getCommand("help").setExecutor(new HelpCommand());

        startTokenCleanupTask();
    }

    @Override
    public void onDisable() {
        getLogger().info("Arcy_RPG disabled.");

    }

    private void startTokenCleanupTask() {
        RegisterCommand registerCommand = new RegisterCommand(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                registerCommand.removeExpiredTokens();
            }
        }.runTaskTimer(this, 0, 20 * 60 * 1440); // Uruchom zadanie co 10 minut (20 ticków * 60 sekund * 1 minut)
        // W metodzie onCommand() klasy RegisterCommand
        new RegisterCommand.TokenCleanupTask(registerCommand).runTaskTimerAsynchronously(this, 0, 10 * 60 * 20); // 10 minut w tickach
    }

}
