package com.squidlard.sutils.Listeners;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatListener implements Listener{

    private Core core = Core.getInstance();
    private final Set<UUID> staffchat = new HashSet<>();
    private long muteChatMillis;

    public boolean isStaffChatActive(Player player) {
        return this.staffchat.contains(player.getUniqueId());
    }

    public void setStaffchat(Player player, Boolean status) {
        if (status) {
            if (this.core.isMod(player)) {
                this.staffchat.add(player.getUniqueId());
                player.sendMessage(Color.translate("&bStaff Chat &7Enabled."));
            }
        }
        else
        {
            this.staffchat.remove(player.getUniqueId());
            player.sendMessage(Color.translate("&bStaff Chat &7Disabled."));
        }
    }

    public void setMuteChatMillis(long value)
    {
        this.muteChatMillis = (System.currentTimeMillis() + value);
    }

    public boolean isChatMuted()
    {
        return getMillisecondLeft() > 0L;
    }

    public long getMillisecondLeft()
    {
        return this.muteChatMillis - System.currentTimeMillis();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if ((isChatMuted()) && !(player.hasPermission("util.bypass"))) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe chat is currently muted!"));
        }
    }

    @EventHandler
    public void onStaffChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();
        if (isStaffChatActive(p)) {
            e.setCancelled(true);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (this.core.isMod(staff)) {
                    staff.sendMessage(Color.translate(Core.getInstance().getConfig().getString("StaffPrefix") + p.getName() + " &8»&b " + msg));
                    Bukkit.getServer().getConsoleSender().sendMessage(Color.translate(Core.getInstance().getConfig().getString("StaffPrefix") + p.getName() + " &8»&b " + msg));
                }
            }
        }
    }
}
