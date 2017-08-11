package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Dalton on 8/2/2017.
 */
public class ListCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        int players = Bukkit.getOnlinePlayers().length;
        int maxplayers = Bukkit.getServer().getMaxPlayers();
        String players2 = Integer.toString(players);
        String maxplayers2 = Integer.toString(maxplayers);

        for (String string : Core.getInstance().getConfig().getStringList("List")) {
            sender.sendMessage(Color.translate(string.replace("%players_online%", players2).replace("%max_players%", maxplayers2)));
        }
        return true;
    }
}
