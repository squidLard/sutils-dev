package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {

    private final Core core = Core.getInstance();

    @Override
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
            Player[] arrayOfPlayer;
            int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
            for (int i = 0; i < j; i++) {
                Player staff = arrayOfPlayer[i];
                if (this.core.isMod(staff)) {
                    staff.sendMessage(this.core.color(this.core.getConfig().getString("StaffChatMsg").replace("{player}", player.getName()).replace("{msg}", StringUtils.join(args, ' '))));
                    Bukkit.getServer().getConsoleSender().sendMessage(Core.getInstance().getConfig().getString("StaffChatMsg").replace("{msg}", StringUtils.join(args, ' ').replace("{player}", player.getName())));
                }
            }
        }
        return true;
    }

}
