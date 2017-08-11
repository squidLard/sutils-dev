package com.squidlard.sutils.Listeners;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.squidlard.sutils.Core.isAdmin;

public class FreezeListener implements Listener{

    private final Core core = Core.getInstance();
    private final Set<UUID> frozen = new HashSet<>();
    private AlertTask alertTask;

    public boolean isFrozen(Player player) {
        return this.frozen.contains(player.getUniqueId());
    }

    public void setFreeze(CommandSender sender, Player target, boolean status)
    {
        if (status)
        {
            this.frozen.add(target.getUniqueId());
            this.alertTask = new AlertTask(target);
            this.alertTask.runTaskTimerAsynchronously(this.core,0L, 100L);
            target.sendMessage(Color.translate("&cYou have been frozen by a staff member."));
            if ((sender instanceof Player))
            {
                Bukkit.getServer().getConsoleSender().sendMessage(Color.translate("&7&o[" + sender.getName() + ": &ehas frozen " + target.getName() + ".&7]"));
                for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (staff.equals(sender)) {
                            return;
                        } else {
                            staff.sendMessage(Color.translate("&7&o[" + sender.getName() + ": &ehas frozen " + target.getName() + ".&7]"));
                        }
                    }
                }
            }
            else
            {
                for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        staff.sendMessage(Color.translate("&7&o[" + sender.getName() + ": &ehas frozen " + target.getName() + ".&7]"));
                    }
                }
            }
        }
        else
        {
            this.alertTask.cancel();
            this.frozen.remove(target.getUniqueId());
            target.sendMessage(Color.translate("&aYou are no longer frozen."));
            if ((sender instanceof Player))
            {
                Bukkit.getServer().getConsoleSender().sendMessage(Color.translate("&7&o[" + sender.getName() + ": &ehas frozen " + target.getName() + ".&7]"));
                for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        if (staff.equals(sender)) {
                            return;
                        } else {
                            staff.sendMessage(Color.translate("&7&o[" + sender.getName() + ": &ehas frozen " + target.getName() + ".&7]"));
                        }
                    }
                }
            }
            else
            {
                for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                    if (isAdmin(staff)) {
                        staff.sendMessage(Color.translate("&7&o[" + sender.getName() + ": &ehas frozen " + target.getName() + ".&7]"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        int x = (int) e.getFrom().getX();
        int z = (int) e.getFrom().getZ();
        int y = (int) e.getFrom().getY();

        int y1 = (int) e.getTo().getY();
        int x1 = (int) e.getTo().getX();
        int z1 = (int) e.getTo().getZ();

        if (!(x != x1 || z != z1 || y !=y1))
            return; // This means that the player moved the camera

        if (isFrozen(p)) {
            e.setTo(e.getFrom());
        }
    }

    public Player getDamager(Entity entity)
    {
        if ((entity instanceof Player)) {
            return (Player)entity;
        }
        if ((entity instanceof Projectile))
        {
            Projectile projectile = (Projectile)entity;
            if ((projectile.getShooter() != null) && ((projectile.getShooter() instanceof Player))) {
                return (Player)projectile.getShooter();
            }
        }
        return null;
    }

    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        Player damager = getDamager(event.getDamager());
        Player damaged = getDamager(event.getEntity());
        if ((damager != null) && (damaged != null) && (damaged != damager))
        {
            if (isFrozen(damager))
            {
                damager.sendMessage(ChatColor.RED + ("You can not attack players while frozen."));
                event.setCancelled(true);
            }
            if (isFrozen(damaged))
            {
                damager.sendMessage(ChatColor.RED + ("This player is currently frozen!"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (isFrozen(player)) {
            if ((!e.getMessage().startsWith("/msg")) &&
                    (!e.getMessage().startsWith("/helpop")) &&
                    (!e.getMessage().startsWith("/message")) &&
                    (!e.getMessage().startsWith("/r")) &&
                    (!e.getMessage().startsWith("/m")) &&
                    (!e.getMessage().startsWith("/request")))
            {
                e.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You can not use commands while frozen!");
            }
        }
    }

    @EventHandler
    public void onMove2(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (isFrozen(player)) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                block.setType(Material.GRASS);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (isFrozen(player))
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("util.recieve")) {
                    staff.sendMessage("");
                    staff.sendMessage(Color.translate("&4&l" + e.getPlayer().getName() + " has logged out while frozen!"));
                    staff.sendMessage("");
                }
            }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player= (Player) e.getWhoClicked();
        if(isFrozen(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if(isFrozen(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if(isFrozen(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e ) {
        Player player = e.getPlayer();
        if(isFrozen(player)) {
            ItemStack item = e.getItemDrop().getItemStack().clone();
            e.getItemDrop().remove();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if(isFrozen(player)) {
            e.setCancelled(true);
        }
    }

    private class AlertTask
            extends BukkitRunnable
    {
        private final Player player;

        public AlertTask(Player player)
        {
            this.player = player;
        }

        public void run()
        {
            if (isFrozen(player))
            {
                this.player.setHealth(this.player.getMaxHealth());
                this.player.setFireTicks(0);
                this.player.setFoodLevel(20);
                this.player.setSaturation(3.0F);
                for (PotionEffect potionEffect : this.player.getActivePotionEffects()) {
                    this.player.removePotionEffect(potionEffect.getType());
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m---------------------------------------------"));

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f▋▋▋▋&c▋&f▋▋▋▋"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f▋▋▋&c▋&6▋&c▋&f▋▋▋"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f▋▋&c▋&6▋&0▋&6▋&c▋&f▋▋ &7Do &4NOT &7log out!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f▋▋&c▋&6▋&0▋&6▋&c▋&f▋▋ &7If you log out, you will be banned!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f▋&c▋&6▋▋&0▋&6▋▋&c▋&f▋ &7You have &45 minutes &7 to join TeamSpeak!"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f▋&c▋&6▋▋▋▋▋&c▋&f▋ " + "&7TeamSpeak: " + Core.getInstance().getConfig().getString("TeamSpeakIP")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c▋&6▋▋▋&0▋&6▋▋▋&c▋"));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c▋▋▋▋▋▋▋▋▋"));

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m---------------------------------------------"));
            }
        }
    }
}
