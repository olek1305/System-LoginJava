package org.main.shop;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.awt.*;

public class ShopGUI {
    private final Plugin plugin;

    public ShopGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        // Create a new JFrame for the shop GUI
        JFrame frame = new JFrame("Shop");

        // Set the size of the JFrame
        frame.setSize(300, 150);

        // Create the "Sell" button
        JButton sellButton = new JButton("Sell");

        // Create the "Buy" button
        JButton buyButton = new JButton("Buy");

        // Add action listeners to the buttons
        sellButton.addActionListener(e -> {
            // Do something when the "Sell" button is clicked
            player.sendMessage("You clicked the Sell button.");
        });
        buyButton.addActionListener(e -> {
            // Do something when the "Buy" button is clicked
            player.sendMessage("You clicked the Buy button.");
        });

        // Create a new JPanel to hold the buttons
        JPanel panel = new JPanel(new GridLayout(2, 1));

        // Add the buttons to the panel
        panel.add(sellButton);
        panel.add(buyButton);

        // Add the panel to the JFrame
        frame.add(panel);

        // Set the JFrame to be visible
        frame.setVisible(true);

        // Create the "Buy Bread" button
        JButton buyBreadButton = new JButton("Buy 6 bread for 1 diamond");

        // Add an action listener to the "Buy Bread" button
        buyBreadButton.addActionListener(e -> {
            // Check if the player has a diamond in their inventory
            if (player.getInventory().contains(Material.DIAMOND)) {
                // Remove the diamond from the player's inventory
                player.getInventory().removeItem(new ItemStack(Material.DIAMOND, 1));

                // Add 6 bread to the player's inventory
                player.getInventory().addItem(new ItemStack(Material.BREAD, 6));

                // Send a message to the player
                player.sendMessage("You bought 6 bread for 1 diamond.");
            } else {
                // Send a message to the player if they don't have a diamond
                player.sendMessage("You don't have a diamond to buy bread.");
            }
        });

// Add the "Buy Bread" button to the panel
        panel.add(buyBreadButton);

    }

}

