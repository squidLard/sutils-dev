package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class ModCommand implements CommandExecutor{

    public static ArrayList<String> modMode = new ArrayList();

    private final Core core = Core.getInstance();

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        if (!player.hasPermission("util.command.admin")) {
            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length < 1) {
            if (!(player instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "No Permission.");
                return true;
            }
            if (this.core.getModModeListener().isStaffModeActive(player))
            {
                this.core.getModModeListener().setStaffMode(player, false);
                return true;
            }
            this.core.getModModeListener().setStaffMode(player, true);
            return true;
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Could not find player.");
            return true;
        }
        if (args.length == 1) {
            if (!(target.hasPermission("util.command.basic"))) {
                sender.sendMessage(ChatColor.RED + "That player does not have access to Mod Mode.");
                return true;
            }
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "No Permission.");
                return true;
            }
            if (this.core.getModModeListener().isStaffModeActive(target))
            {
                this.core.getModModeListener().setStaffMode(target, false);
                player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("ModDisableMsg").replace("%player%", target.getName())));
                return true;
            }
            this.core.getModModeListener().setStaffMode(target, true);
            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("ModEnableMsg").replace("%player%", target.getName())));
            return true;
        }
        return false;
    }

}

