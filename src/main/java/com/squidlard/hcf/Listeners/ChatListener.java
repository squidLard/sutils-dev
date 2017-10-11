package com.squidlard.hcf.Listeners;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
                player.sendMessage(Color.translate("&eStaff chat mode for %player% set to true.").replace("%player%", player.getName()));
            }
        }
        else
        {
            this.staffchat.remove(player.getUniqueId());
            player.sendMessage(Color.translate("&eStaff chat mode for %player% set to false.").replace("%player%", player.getName()));
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
            Player[] arrayOfPlayer;
            int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
            for (int i = 0; i < j; i++) {
                Player staff = arrayOfPlayer[i];
                if (this.core.isMod(staff)) {
                    staff.sendMessage(this.core.color(this.core.getConfig().getString("StaffChatMsg").replace("{msg}", msg).replace("{player}", p.getName())));
                    Bukkit.getServer().getConsoleSender().sendMessage(Core.getInstance().getConfig().getString("StaffChatMsg").replace("{msg}", msg).replace("{player}", p.getName()));
                }
            }
        }
    }
}
