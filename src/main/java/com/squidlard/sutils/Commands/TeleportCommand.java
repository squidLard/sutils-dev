package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Dalton on 6/20/2017.
 */
public class TeleportCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;
        if (!player.hasPermission("util.command.tp")) {
            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }

        if (!(player instanceof Player)) {
            player.sendMessage(Color.translate("&cNo."));
        }

        if (args.length == 0) {
            player.sendMessage(Color.translate("&cUsage: /" + label + " <player>"));
            return true;
        }

        Player target = player.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Color.translate("&cCould not find player."));
            return true;
        }

        if ((args.length == 1) && (cmd.getName().equalsIgnoreCase("tphere"))) {
            target.teleport(player);
            player.sendMessage(Color.translate("&aYou have teleported &7" + target.getName() + " &ato you."));
            target.sendMessage(Color.translate("&aYou have been teleported to &7" + sender.getName() + "&a."));
            return true;
        }

        if (args.length == 1) {
                Location targetplayer = target.getLocation();
                player.teleport(targetplayer);
                player.sendMessage(Color.translate("&aYou have been teleported to &7" + target.getName() + "&a."));
                return true;
        }

        if (args.length == 2) {
            Player target1 = player.getServer().getPlayer(args[1]);
            target.teleport(target1);
            player.sendMessage(Color.translate("&aYou have teleported &7" + args[0] + " &ato &7" + args[1] + "&a."));
            target.sendMessage(Color.translate("&aYou have been teleported to &7" + args[1] + "&a."));
            target1.sendMessage(Color.translate("&7" + args[0] + " &awas teleported to you."));
            return true;
        }

        return false;
    }
}
