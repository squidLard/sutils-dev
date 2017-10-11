package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import com.squidlard.hcf.Utilities.TimeUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MuteChatCommand
        implements CommandExecutor
{
    private final Core core = Core.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments)
    {
        if (sender.hasPermission("util.command.basic"))
        {
            long currentTicks = this.core.getChatListener().getMillisecondLeft();
            Long newTicks;
            if (currentTicks > 0L)
            {
                newTicks = Long.valueOf(0L);
            }
            else
            {
                if (arguments.length < 1)
                {
                    newTicks = Long.valueOf(TimeUtils.parse("10m"));
                }
                else
                {
                    newTicks = Long.valueOf(TimeUtils.parse(arguments[0]));
                    if (newTicks.longValue() == -1L)
                    {
                        sender.sendMessage(Color.translate("&cInvalid duration, use the correct format: 5m5s [5 minutes and 15 second]"));
                        return true;
                    }
                }
            }
            this.core.getChatListener().setMuteChatMillis(newTicks.longValue());
            if (this.core.getChatListener().isChatMuted()) {
                Bukkit.getServer().broadcastMessage(Color.translate("&a" + sender.getName() + " &7has disabled the global chat for &a" + DurationFormatUtils.formatDurationWords(newTicks.longValue(), true, true) + "&7."));
            } else {
                Bukkit.getServer().broadcastMessage(Color.translate("&a" + sender.getName() + " &7has enabled the global chat."));
            }
        }
        else
        {
            sender.sendMessage(Color.translate(core.getConfig().getString("NoPerm")));
        }
        return true;
    }
}
