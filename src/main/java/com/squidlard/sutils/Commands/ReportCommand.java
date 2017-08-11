package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import com.squidlard.sutils.Utilities.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.squidlard.sutils.Utilities.TimeUtils.LongCountdown.setFormat;


public class ReportCommand implements CommandExecutor{

    private final Map<UUID, Long> request = new HashMap();

    public void setCooldown(Player player, long value)
    {
        this.request.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis() + value));
    }

    public void removeCooldown(Player player)
    {
        if (isCooldownActive(player)) {
            this.request.remove(player.getUniqueId());
        }
    }

    public boolean isCooldownActive(Player player) {
        if (!this.request.containsKey(player.getUniqueId())) {
            return false;
        }
        return ((Long)this.request.get(player.getUniqueId())).longValue() > System.currentTimeMillis();
    }

    public long getMillisecondLeft(Player player) {
        if (!isCooldownActive(player)) {
            return -1L;
        }
        return ((Long)this.request.get(player.getUniqueId())).longValue() - System.currentTimeMillis();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if (!player.hasPermission("util.command.player")) {
            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Color.translate("&cUsage: /report <player> <reason>"));
            return true;
        }
        if(isCooldownActive(player)) {
            player.sendMessage(new Color().translate("&cYou can not use this command for &c" + setFormat(getMillisecondLeft(player)) + "&c."));
            return true;
        }
        if (args.length >= 2) {
            Player target = Bukkit.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Color.translate("&cThat player is not online."));
                return true;
            }
            player.sendMessage(new Color().translate("&aYour report has been sent to all online staff members."));
            setCooldown(player, TimeUtils.parse("30s"));
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("util.recieve")) {
                    staff.sendMessage(Color.translate("&9[Report]&b " + sender.getName() + " &7has reported &b" + target.getName()));
                    staff.sendMessage(Color.translate("     &9Reason&8:&7" + StringUtils.join(args, ' ').replace(args[0], "")));
                }
            }
        }
        return true;
    }
}
