package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Dalton on 5/7/2017.
 */
public class CopyInvCommand implements CommandExecutor{

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("util.command.admin")) {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage(Color.translate("&cUsage: /copyinv <player>"));
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(Color.translate("&cCould not find player " + args[0]));
            return true;
        }
        if (args.length == 1) {
            Player player = (Player) sender;

            player.getInventory().setContents(target.getInventory().getContents());
            player.getInventory().setArmorContents(target.getInventory().getArmorContents());
            player.sendMessage(Color.translate("&cYou have copied " + args[0] + " 's inventoy."));
        }
        return false;
    }
}
