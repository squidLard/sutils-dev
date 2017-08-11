package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Listeners.Crowbar;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CrowBarCommand
        implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Player p = (Player)sender;

        if (!p.hasPermission("util.command.admin")) {
            p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("crowgive"))
        {
            if (args.length == 0)
            {
                p.sendMessage(Color.translate("&cUsage: /" + label + " <PlayerName>"));
                return true;
            }
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null)
            {
                p.sendMessage(ChatColor.RED + "That player is not online! Try again later!");
                return true;
            }
            ItemStack stack = new Crowbar().getItemIfPresent();
            target.getInventory().addItem(new ItemStack[] { stack });
            target.sendMessage(Color.translate("&aYou were given a CROWBAR from &7" + sender.getName()));
            p.sendMessage(Color.translate("&aYou have given &7" + target.getName() + " &aa CROWBAR"));
            return true;
        }
        return false;
    }
}
