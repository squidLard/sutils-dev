package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Listeners.InspectListener;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by Dalton on 5/7/2017.
 */
public class InspectCommand implements CommandExecutor, Listener{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("util.command.basic")) {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(Color.translate("&cUsage: /" + label + " <player>"));
            return true;
        }
        final Player target = Bukkit.getServer().getPlayer(args[0]);
        final Player player = (Player) sender;
        if (target == null) {
            player.sendMessage(Color.translate("&cCould not find player " + args[0]));
            return true;
        }
        if (args.length == 1) {
            InspectListener.InspectPlayer(player, args);
        }
        return false;
    }

    @EventHandler
    public void onInspectClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (event.getInventory().getName().contains("§8Inventory ")) {
            event.setCancelled(true);
        }
    }
}
