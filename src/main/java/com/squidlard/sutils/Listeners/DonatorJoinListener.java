package com.squidlard.sutils.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.squidlard.sutils.Core.donator;

public class DonatorJoinListener implements Listener{

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getDisplayName();
        if (p.isOp()) {
            donator.remove(name);
            return;
        }
        if (p.hasPermission("*")) {
            donator.remove(name);
            return;
        }
        if (p.hasPermission("util.donator.set")) {
            donator.add(name);
            return;
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = e.getPlayer().getName();
        if (p.hasPermission("util.donator.set")) {
            if (donator.contains(name)) {
                donator.remove(name);
                return;
            }
        }
    }

}

