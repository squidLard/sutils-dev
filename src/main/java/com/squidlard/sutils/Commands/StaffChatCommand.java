package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    private final Core core = Core.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (!sender.hasPermission("util.command.basic")) {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }

        if (args.length == 0) {
            if (this.core.getChatListener().isStaffChatActive(player)) {
                this.core.getChatListener().setStaffchat(player, false);
                return true;
            }
            this.core.getChatListener().setStaffchat(player, true);
            return true;
        }
        if (args.length > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("util.command.basic")) {
                    p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("StaffPrefix") + sender.getName() + " &8»&b " + StringUtils.join(args, ' ')));
                    Bukkit.getServer().getConsoleSender().sendMessage(Color.translate(Core.getInstance().getConfig().getString("StaffPrefix") + sender.getName() + " &8»&b" + StringUtils.join(args, ' ')));
                }
            }
        }
        return true;
    }

}
