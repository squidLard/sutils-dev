package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Created by Dalton on 4/30/2017.
 */
public class CoordsCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length >= 0) {
            if (!sender.hasPermission("util.command.player")) {
                sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            } else {
                for (String string : Core.getInstance().getStringList("messages.coords")) {
                    sender.sendMessage(string);
                }
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        return Collections.emptyList();
    }
}
