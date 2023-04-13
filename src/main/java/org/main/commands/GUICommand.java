package org.main.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GUICommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            Inventory shopGUI = Bukkit.createInventory(player, 27, ChatColor.AQUA + "Sklep GUI");

            // Przedmiot 1 (połączone sloty 2x2)
            ItemStack item1 = new ItemStack(Material.IRON_INGOT, 4);
            ItemMeta item1_meta = item1.getItemMeta();
            item1_meta.setDisplayName(ChatColor.RED + "Żelazny blok");
            ArrayList<String> item1_lore = new ArrayList<>();
            item1_lore.add(ChatColor.GRAY + "4 sztuki żelaza");
            item1_meta.setLore(item1_lore);
            item1.setItemMeta(item1_meta);

            ItemStack[] item1_array = {item1, null, null, null};
            shopGUI.setItem(11, mergeItems(item1_array));

            // Przedmiot 2 (pojedynczy slot)
            ItemStack item2 = new ItemStack(Material.GOLD_INGOT, 1);
            ItemMeta item2_meta = item2.getItemMeta();
            item2_meta.setDisplayName(ChatColor.YELLOW + "Złoty blok");
            ArrayList<String> item2_lore = new ArrayList<>();
            item2_lore.add(ChatColor.GRAY + "9 sztuk złota");
            item2_meta.setLore(item2_lore);
            item2.setItemMeta(item2_meta);

            shopGUI.setItem(15, item2);

            // Przycisk "Kup"
            ItemStack buyButton = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta buyButton_meta = buyButton.getItemMeta();
            buyButton_meta.setDisplayName(ChatColor.GREEN + "Kup");
            buyButton_meta.setLore(Collections.singletonList(ChatColor.GRAY + "Kupuje wybrany przedmiot"));
            buyButton.setItemMeta(buyButton_meta);

            shopGUI.setItem(26, buyButton);

            player.openInventory(shopGUI);
        }

        return false;
    }

    // Metoda służąca do łączenia przedmiotów w jednym slocie 2x2
    private ItemStack mergeItems(ItemStack[] items) {
        Inventory inv = Bukkit.createInventory(null, 9);
        inv.setContents(items);
        return inv.getItem(0);
    }
}
