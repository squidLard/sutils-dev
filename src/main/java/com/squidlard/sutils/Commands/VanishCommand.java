package com.squidlard.sutils.Commands;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static com.squidlard.sutils.Core.*;

public class VanishCommand implements CommandExecutor {

    private final Core core = Core.getInstance();

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {

        ItemStack enabled = new ItemStack(Material.INK_SACK, 1, (short) 8);
        ItemStack disabled = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ArrayList<String> enabledlore = new ArrayList();
        ArrayList<String> disabledlore = new ArrayList();

        for (String svanish : Core.getInstance().getConfig().getStringList("VanishEnableObject.Lore")) {
            enabledlore.add(Color.translate(svanish));
        }
        for (String svanish2 : Core.getInstance().getConfig().getStringList("VanishDisableObject.Lore")) {
            disabledlore.add(Color.translate(svanish2));
        }
        ItemMeta enabledmeta = enabled.getItemMeta();
        ItemMeta disabledmeta = disabled.getItemMeta();
        enabledmeta.setDisplayName(Color.translate(Core.getInstance().getConfig().getString("VanishEnableObject.Name")));
        disabledmeta.setDisplayName(Color.translate(Core.getInstance().getConfig().getString("VanishDisableObject.Name")));
        enabledmeta.setLore(enabledlore);
        disabledmeta.setLore(disabledlore);
        enabled.setItemMeta(enabledmeta);
        disabled.setItemMeta(disabledmeta);

        Player player = (Player) sender;
        if (!(player.hasPermission("util.command.basic"))) {
            sender.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }

        if (args.length < 1) {
            if (this.core.getVanishListener().isVanished(player)) {
                if (this.core.getModModeListener().isStaffModeActive(player)) {
                    this.core.getVanishListener().setVanished(player, false);
                    player.sendMessage(Color.translate(core.getConfig().getString("VanishDisableMsg").replace("%player%", player.getName())));
                    player.getInventory().setItem(this.core.getConfig().getInt("VanishDisableObject.InventorySlot") - 1, disabled);
                    return true;
                }
                this.core.getVanishListener().setVanished(player, false);
                player.sendMessage(Color.translate(core.getConfig().getString("VanishDisableMsg").replace("%player%", player.getName())));
                return true;
            }
            else
            {
                if (this.core.getModModeListener().isStaffModeActive(player)) {
                    this.core.getVanishListener().setVanished(player, true);
                    player.sendMessage(Color.translate(core.getConfig().getString("VanishEnableMsg").replace("%player%", player.getName())));
                    player.getInventory().setItem(this.core.getConfig().getInt("VanishEnableObject.InventorySlot") - 1, enabled);
                    return true;
                }
                this.core.getVanishListener().setVanished(player, true);
                player.sendMessage(Color.translate(core.getConfig().getString("VanishEnableMsg").replace("%player%", player.getName())));
                return true;
            }
        }
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Could not find player.");
            return true;
        }
        if (!(target.hasPermission("util.command.basic"))) {
            sender.sendMessage(Color.translate("&cThat player does not have access to vanish."));
            return true;
        }
        if (args.length == 1) {
            if (isAdmin(player)) {
                if (this.core.getVanishListener().isVanished(target)) {
                        if (this.core.getModModeListener().isStaffModeActive(target)) {
                        this.core.getVanishListener().setVanished(target, false);
                        target.getInventory().setItem(8, disabled);
                        player.sendMessage(Color.translate(core.getConfig().getString("VanishDisableMsg").replace("%player%", target.getName())));
                        return true;
                    }
                    this.core.getVanishListener().setVanished(target, false);
                    player.sendMessage(Color.translate(core.getConfig().getString("VanishDisableMsg").replace("%player%", target.getName())));
                    return true;
                }
                else
                {
                    if (this.core.getModModeListener().isStaffModeActive(target)) {
                        this.core.getVanishListener().setVanished(target, true);
                        player.sendMessage(Color.translate(core.getConfig().getString("VanishEnableMsg").replace("%player%", target.getName())));
                        target.getInventory().setItem(8, enabled);
                        return true;
                    }
                    this.core.getVanishListener().setVanished(target, true);
                    player.sendMessage(Color.translate(core.getConfig().getString("VanishEnableMsg").replace("%player%", target.getName())));
                    return true;
                }
            }
        } else {
            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        return false;
    }
}
