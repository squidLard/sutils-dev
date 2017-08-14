package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import com.squidlard.sutils.Utilities.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Dalton on 8/14/2017.
 */
public class PlayTimeCommand implements CommandExecutor{

    private final Core core = Core.getInstance();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("playtime")) {
                if (!sender.hasPermission("util.command.player")) {
                    p.sendMessage(Color.translate(core.getConfig().getString("NoPerm")));
                    return true;
                }
                if (args.length == 0) {
                    sender.sendMessage(Color.translate("&7" + p.getName() + "&e's total playtime is &7" + TimeFormat.getTime(p.getStatistic(Statistic.PLAY_ONE_TICK) / 20)));
                    return true;
                }
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(Color.translate("&cThat player is not online."));
                    return true;
                }
                if (args.length >= 1) {
                    sender.sendMessage(Color.translate("&7" + target.getName() + "&e's total playtime is &7" + TimeFormat.getTime(target.getStatistic(Statistic.PLAY_ONE_TICK) / 20)));
                    return true;
                }
            }
        }
        return true;
    }
}
