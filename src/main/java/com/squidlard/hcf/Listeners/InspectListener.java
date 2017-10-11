package com.squidlard.hcf.Listeners;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Dalton on 8/1/2017.
 */
public class InspectListener {

    public static void InspectPlayer(Player player, String[] args) {
        Player target = Bukkit.getServer().getPlayer(args[0]);
        Inventory myInventory = Bukkit.createInventory(player, 45, ChatColor.DARK_GRAY + "Inventory " + ChatColor.WHITE + target.getName());

        ItemStack[] items = target.getInventory().getContents();

        myInventory.setContents(items);
        myInventory.setItem(36, target.getInventory().getHelmet());
        myInventory.setItem(37, target.getInventory().getChestplate());
        myInventory.setItem(38, target.getInventory().getLeggings());
        myInventory.setItem(39, target.getInventory().getBoots());

        player.openInventory(myInventory);
        player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("InspectPlayerMsg").replace("{player}", target.getName())));
    }

    public static void InspectPlayerEvent(Player player, final Player target) {
        Inventory myInventory = Bukkit.createInventory(target, 45, ChatColor.DARK_GRAY + "Inventory " + ChatColor.WHITE + target.getName());

        ItemStack[] items = target.getInventory().getContents();

        myInventory.setContents(items);
        myInventory.setItem(36, target.getInventory().getHelmet());
        myInventory.setItem(37, target.getInventory().getChestplate());
        myInventory.setItem(38, target.getInventory().getLeggings());
        myInventory.setItem(39, target.getInventory().getBoots());

        player.openInventory(myInventory);
        player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("InspectPlayerMsg").replace("{player}", target.getName())));
    }

}
