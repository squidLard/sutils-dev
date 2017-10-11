package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Dalton on 4/30/2017.
 */
public class ReloadCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (!commandSender.hasPermission("util.command.admin")) {
            commandSender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length < 1) {
            commandSender.sendMessage(Color.translate("&cUsage: /" + s + " reload"));
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                Core.getInstance().reloadConfig();
                commandSender.sendMessage(Color.translate("&aConfig file successfully reloaded."));
            }
        }
        return false;
    }
}
