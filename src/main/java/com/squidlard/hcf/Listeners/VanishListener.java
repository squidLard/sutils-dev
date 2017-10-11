package com.squidlard.hcf.Listeners;

import com.squidlard.hcf.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.squidlard.hcf.Core.isMod;

public class VanishListener implements Listener {

    private final Core core = Core.getInstance();
    private final Set<UUID> vanished = new HashSet<>();

    public boolean isVanished(Player player) {
        return this.vanished.contains(player.getUniqueId());
    }

    public void setVanished(Player player, Boolean status) {
        Object playerInventory;
        if (status) {
            this.vanished.add(player.getUniqueId());
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                if (!isMod(online)) {
                    online.hidePlayer(player);
                }
            }
        }
        else
        {
            this.vanished.remove(player.getUniqueId());
            for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                online.showPlayer(player);
            }
        }
    }

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e) {
        if ((!(e.getEntity() instanceof Player)) || (!(e.getDamager() instanceof Player))) {
            return;
        }
        Player player = (Player) e.getDamager();
        if (isVanished(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (this.vanished.size() > 0) {
            for (UUID uuid : this.vanished) {
                Player vanishedPlayer = Bukkit.getServer().getPlayer(uuid);
                if (vanishedPlayer != null) {
                    if (isMod(player)) {
                        return;
                    } else {
                        player.hidePlayer(vanishedPlayer);
                    }
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        this.setVanished(player, false);
    }

}
