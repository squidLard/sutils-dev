package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Dalton on 4/25/2017.
 */
public class BlackListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender.hasPermission("util.command.admin")) {
            if (args.length != 1)
            {
                sender.sendMessage(Color.translate("&cUsage: /blacklist <player>"));
            }
            else if (args.length == 1)
            {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban -s " + args[0] + " You are blacklisted from the Network.");

                Bukkit.broadcastMessage(Color.translate("&a" + args[0] + " has been blacklisted by " + sender.getName()));
            }
        } else {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        return false;
    }

}
