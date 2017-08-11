package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class FreezeCommand implements CommandExecutor {

    private final Core core = Core.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("util.command.basic"))) {
            sender.sendMessage(Color.translate(core.getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Color.translate("&cUsage: /" + label + " <player>"));
            return true;
        }
        Player target = Bukkit.getServer().getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(Color.translate("&cPlayer could not be found."));
            return true;
        }
        Player player = (Player) sender;
        if (Bukkit.getServer().getPlayer(args[0]) == player) {
            player.sendMessage(Color.translate("&cYou can not freeze yourself."));
            return true;
        }
        if (target.hasPermission("util.command.basic")) {
            sender.sendMessage(Color.translate("&aYou can not freeze other staff."));
            return true;
        }
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Color.translate("&cNo Permission."));
                return true;
            }
            if (this.core.getFreezeListener().isFrozen(target))
                {
                    this.core.getFreezeListener().setFreeze(sender, target, false);
                    player.sendMessage(Color.translate(core.getConfig().getString("UnfreezePlayerMsg").replace("%target%", target.getName())));
                    return true;
                }
                this.core.getFreezeListener().setFreeze(sender, target, true);
                player.sendMessage(Color.translate(core.getConfig().getString("FreezePlayerMsg").replace("%target%", target.getName())));
                return true;
            }
            return false;
        }
    }

