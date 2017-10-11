package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements Listener, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player p = (Player)sender;
        if (args.length != 1)
        {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWrong number of args &7(&cUse '_' instead of space&7)"));
        }
        else if (!p.hasPermission("util.command.admin"))
        {
            p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
        }
        else if ((p.getItemInHand() == null) || (p.getItemInHand() == new ItemStack(Material.AIR)))
        {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're not holding an item!"));
        }
        else
        {
            ItemMeta meta = p.getItemInHand().getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[0].replace("_", " ")));
            p.getItemInHand().setItemMeta(meta);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Item &asuccessfully &7renamed!"));
        }
        return false;
    }
}
