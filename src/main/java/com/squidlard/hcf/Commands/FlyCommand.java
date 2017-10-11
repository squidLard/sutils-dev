package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

import static com.squidlard.hcf.Core.isAdmin;

/**
 * Created by Dalton on 6/18/2017.
 */
public class FlyCommand implements CommandExecutor, Listener{

    public static ArrayList<String> god = new ArrayList<>();

    public static ArrayList<String> fly = new ArrayList<String>();

    public void enterFly(Player p) {
        fly.add(p.getName());
        p.setAllowFlight(true);
        p.setFlying(true);
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (isAdmin(staff)) {
                if (p == staff) {
                    p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("FlyEnableMsg").replace("{player}", p.getName())));
                }
                else
                {
                    staff.sendMessage(Color.translate("&7[&o" + p.getName() + ": &eFlight mode of " + p.getName()  + " set to true.&7]"));
                }
            }
        }
    }

    public void LeaveFly(Player p) {
        fly.remove(p.getName());
        p.setAllowFlight(false);
        p.setFlying(false);
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (isAdmin(staff)) {
                if (p == staff) {
                    p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("FlyDisableMsg").replace("{player}", p.getName())));
                }
                else
                {
                    staff.sendMessage(Color.translate("&7[&o" + p.getName() + ": &eFlight mode of " + p.getName()  + " set to false&7]"));
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if (!sender.hasPermission("util.command.basic")) {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length > 1) {
            player.sendMessage(Color.translate("&cUsage: /fly : /fly <player>"));
        }
        if (args.length == 0) {
            if (cmd.getName().equalsIgnoreCase("fly")) {
                if (fly.contains(player.getName())) {
                    fly.remove(player.getName());
                    player.setAllowFlight(false);
                    for (Player staff : Bukkit.getOnlinePlayers()) {
                        if (isAdmin(staff)) {
                            if (player == staff) {
                                player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("FlyDisableMsg").replace("{player}", player.getName())));
                            }
                            else
                            {
                                staff.sendMessage(Color.translate("&7[&o" + player.getName() + ": &eFlight mode of " + player.getName()  + " set to false&7]"));
                            }
                        }
                    }
                    return true;
                }
                fly.add(player.getName());
                player.setAllowFlight(true);
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (player == staff) {
                            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("FlyEnableMsg").replace("{player}", player.getName())));
                        }
                        else
                        {
                            staff.sendMessage(Color.translate("&7[&o" + player.getName() + ": &eFlight mode of " + player.getName()  + " set to true&7]"));
                        }
                    }
                }
                return true;
            }
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (args.length == 1) {
            if (cmd.getName().equalsIgnoreCase("fly")) {
                if (fly.contains(target.getName())) {
                    fly.remove(target.getName());
                    target.setAllowFlight(false);
                    for (Player staff : Bukkit.getOnlinePlayers()) {
                        if (isAdmin(staff)) {
                            if (player == staff) {
                                player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("FlyDisableMsg").replace("{player}", target.getName())));
                            }
                            else
                            {
                                staff.sendMessage(Color.translate("&7[&o" + player.getName() + ": &eFlight mode of " + target.getName()  + " set to false&7]"));
                            }
                        }
                    }
                    return true;
                }
                fly.add(target.getName());
                target.setAllowFlight(true);
                for (Player staff : Bukkit.getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (player == staff) {
                            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("FlyEnableMsg").replace("{player}", target.getName())));
                        }
                        else
                        {
                            staff.sendMessage(Color.translate("&7[&o" + player.getName() + ": &eFlight mode of " + target.getName()  + " set to true&7]"));
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void OnEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (god.contains(((Player) e.getEntity()).getName())) {
                e.setCancelled(true);
            }
        }
    }
}
