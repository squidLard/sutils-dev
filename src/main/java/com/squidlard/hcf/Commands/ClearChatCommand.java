package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        if (p.hasPermission("util.command.basic")) {
            if (args.length == 0) {
                p.sendMessage(Color.translate("&cUsage: /" + label + " <reason>"));
            }
            else {
                for (Player online : Bukkit.getServer().getOnlinePlayers()) {
                    if (online.hasPermission("util.bypass")) {
                        online.sendMessage(Color.translate("&eYour chat was not cleared cause you have bypass."));
                        online.sendMessage(Color.translate("&a" + p.getName() + " &ahas cleared the chat for &7" + StringUtils.join(args, ' ') + "&a."));
                    } else {
                        online.sendMessage(new String[101]);
                        online.sendMessage(Color.translate("&a" + p.getName() + " &ahas cleared the chat for &7" + args[0] + "&a."));
                    }
                }
            }
        } else {
            p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
        }
        return true;
    }

}

