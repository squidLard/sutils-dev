package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * Created by Dalton on 4/30/2017.
 */
public class HelpCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length >= 0) {
            if (!sender.hasPermission("util.command.player")) {
                sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
                return true;
            } else {
                for (String string : Core.getInstance().getStringList("messages.help")) {
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
