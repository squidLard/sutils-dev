package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import com.squidlard.hcf.Utilities.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.squidlard.hcf.Utilities.TimeUtils.LongCountdown.setFormat;


/**
 * Created by Dalton on 4/29/2017.
 */
public class RequestCommand implements CommandExecutor{

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
    {

        if ((sender instanceof Player))
        {
            Player player = (Player)sender;
            if (!player.hasPermission("util.command.player")) {
                player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
                return true;
            }
            if (arguments.length < 1)
            {
                player.sendMessage(new Color().translate("&cUsage: /" + label + " <message...>"));
            }
            else if (isCooldownActive(player))
            {
                player.sendMessage(new Color().translate("&cYou can not use this command for &c" + setFormat(getMillisecondLeft(player)) + "&c."));
            }
            else
            {
                setCooldown(player, TimeUtils.parse("30s"));
                player.sendMessage(new Color().translate("&aYour request has been sent to all online staff members."));
                for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                    if (staff.hasPermission("util.recieve"))
                    {
                        staff.sendMessage(Color.translate("&4[Request] &e" + player.getName() + " &7requested assistance"));
                        staff.sendMessage(Color.translate("     &4Reason:&7 " + StringUtils.join(arguments, ' ') + "&e."));
                    }
                }
            }
        }
        else
        {
            sender.sendMessage(new Color().translate("&cYou can not execute this command on console."));
        }
        return true;
    }
}
