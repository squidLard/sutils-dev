package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.squidlard.sutils.Core.isAdmin;

public class GameModeCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (!sender.hasPermission("util.command.admin")) {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length > 1) {
            sender.sendMessage(Color.translate("&cUsage: /gmc : /gms : /gmc <player> : /gms <player>"));
        }
        if (args.length == 0) {
            if ((cmd.getName().equalsIgnoreCase("gmc") || (cmd.getName().equalsIgnoreCase("gm1")))) {
                if (p.getGameMode() == GameMode.CREATIVE) {
                    p.sendMessage(Color.translate("&cYou are already in CREATIVE."));
                    return true;
                }
                p.setGameMode(GameMode.CREATIVE);
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (p == staff) {
                            p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("GameModeCreativeMsg").replace("%player%", p.getName())));
                        }
                        else
                        {
                            staff.sendMessage(Color.translate("&7[&o" + p.getName() + ": &eSet gamemode of " + p.getName()  + " to &fCREATIVE&7]"));
                        }
                    }
                }
                return true;
            }
            if ((cmd.getName().equalsIgnoreCase("gms") || (cmd.getName().equalsIgnoreCase("gm0")))) {
                if (p.getGameMode() == GameMode.SURVIVAL) {
                    p.sendMessage(Color.translate("&cYou are already in SURVIVAL."));
                    return true;
                }
                p.setGameMode(GameMode.SURVIVAL);
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (p == staff) {
                            p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("GameModeSurvivalMsg").replace("%player%", p.getName())));
                        }
                        else
                        {
                            staff.sendMessage(Color.translate("&7[&o" + p.getName() + ": &eSet gamemode of " + p.getName() + " to &fSURVIVAL&7]"));
                        }
                    }
                }
                return true;
            }
        }
        if (args.length == 1) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                p.sendMessage(Color.translate("&cCould not find player " + args[0]));
                return true;
            }
            if ((cmd.getName().equalsIgnoreCase("gmc") || (cmd.getName().equalsIgnoreCase("gm1")))) {
                if (target.getGameMode() == GameMode.CREATIVE) {
                    p.sendMessage(Color.translate("&cThat player is already in CREATIVE."));
                    return true;
                }
                target.setGameMode(GameMode.CREATIVE);
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (p == staff) {
                            p.sendMessage(Color.translate("&eSet gamemode of " + target.getName() + " to &fCREATIVE"));
                        }
                        else
                        {
                            staff.sendMessage(Color.translate("&7[&o" + p.getName() + ": &eSet gamemode of " + target.getName() + " to &fCREATIVE&7]"));
                        }
                    }
                }
            }
            if ((cmd.getName().equalsIgnoreCase("gms") || (cmd.getName().equalsIgnoreCase("gm0")))) {
                if (target.getGameMode() == GameMode.SURVIVAL) {
                    p.sendMessage(Color.translate("&cThat player is already in SURVIVAL."));
                    return true;
                }
                target.setGameMode(GameMode.SURVIVAL);
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (p == staff) {
                            p.sendMessage(Color.translate("&eSet gamemode of " + target.getName() + " to &fSURVIVAL"));
                        }
                        else
                        {
                            staff.sendMessage(Color.translate("&7[&o" + p.getName() + ": &eSet gamemode of " + target.getName() + " to &fSURVIVAL&7]"));
                        }
                    }
                }
            }
        }
        return false;
    }
}
