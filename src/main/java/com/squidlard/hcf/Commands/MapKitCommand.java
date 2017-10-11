package com.squidlard.hcf.Commands;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class MapKitCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        Inventory mapkit = Bukkit.createInventory(null, 54, Color.translate("&4&lMap Kit"));

        Potion poison = new Potion(PotionType.POISON, 1);
        poison.setHasExtendedDuration(false);
        poison.setSplash(true);

        Potion invis = new Potion(PotionType.INVISIBILITY, 1);
        invis.setHasExtendedDuration(true);
        invis.setSplash(false);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack ph = new ItemStack(Material.STAINED_GLASS_PANE, 1);
        ItemStack poisonpot = new ItemStack(poison.toItemStack(1));
        ItemStack invispot = new ItemStack(invis.toItemStack(1));

        ItemMeta phmeta = ph.getItemMeta();
        ItemMeta poisonmeta = poisonpot.getItemMeta();
        ItemMeta invismeta = invispot.getItemMeta();
        ItemMeta helmetmeta = helmet.getItemMeta();
        ItemMeta chestmeta = chestplate.getItemMeta();
        ItemMeta leggmeta = leggings.getItemMeta();
        ItemMeta bootmeta = boots.getItemMeta();
        ItemMeta swordmeta = sword.getItemMeta();
        ItemMeta bowmeta = bow.getItemMeta();

        helmetmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Core.getInstance().getConfig().getInt("Prot"), true);
        helmetmeta.addEnchant(Enchantment.DURABILITY, Core.getInstance().getConfig().getInt("Unbreaking"), true);

        chestmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Core.getInstance().getConfig().getInt("Prot"), true);
        chestmeta.addEnchant(Enchantment.DURABILITY, Core.getInstance().getConfig().getInt("Unbreaking"), true);

        leggmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Core.getInstance().getConfig().getInt("Prot"), true);
        leggmeta.addEnchant(Enchantment.DURABILITY, Core.getInstance().getConfig().getInt("Unbreaking"), true);

        bootmeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, Core.getInstance().getConfig().getInt("Prot"), true);
        bootmeta.addEnchant(Enchantment.DURABILITY, Core.getInstance().getConfig().getInt("Unbreaking"), true);
        bootmeta.addEnchant(Enchantment.PROTECTION_FALL, Core.getInstance().getConfig().getInt("FeatherFalling"), true);

        swordmeta.addEnchant(Enchantment.DAMAGE_ALL, Core.getInstance().getConfig().getInt("Sharpness"), true);
        swordmeta.addEnchant(Enchantment.DURABILITY, Core.getInstance().getConfig().getInt("Unbreaking"), true);

        bowmeta.addEnchant(Enchantment.ARROW_DAMAGE, Core.getInstance().getConfig().getInt("Power"), true);
        bowmeta.addEnchant(Enchantment.DURABILITY, Core.getInstance().getConfig().getInt("Unbreaking"), true);
        bowmeta.addEnchant(Enchantment.ARROW_FIRE, 1, true);
        bowmeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

        ArrayList<String> PotionLore = new ArrayList<String>();

        ArrayList<String> InvisLore = new ArrayList<String>();

        PotionLore.add(Color.translate("&7Extended: &cFalse"));

        InvisLore.add(Color.translate("&7Extended: &aTrue"));

        poisonmeta.setLore(PotionLore);
        invismeta.setLore(InvisLore);
        phmeta.setDisplayName(" ");

        ph.setItemMeta(phmeta);
        invispot.setItemMeta(invismeta);
        poisonpot.setItemMeta(poisonmeta);
        helmet.setItemMeta(helmetmeta);
        chestplate.setItemMeta(chestmeta);
        leggings.setItemMeta(leggmeta);
        boots.setItemMeta(bootmeta);
        sword.setItemMeta(swordmeta);
        bow.setItemMeta(bowmeta);

        mapkit.setItem(13, helmet);
        mapkit.setItem(21, sword);
        mapkit.setItem(22, chestplate);
        mapkit.setItem(23, bow);
        mapkit.setItem(30, poisonpot);
        mapkit.setItem(31, leggings);
        mapkit.setItem(32, invispot);
        mapkit.setItem(40, boots);
        mapkit.setItem(11, ph);
        mapkit.setItem(12, ph);
        mapkit.setItem(14, ph);
        mapkit.setItem(15, ph);
        mapkit.setItem(20, ph);
        mapkit.setItem(24, ph);
        mapkit.setItem(29, ph);
        mapkit.setItem(33, ph);
        mapkit.setItem(38, ph);
        mapkit.setItem(39, ph);
        mapkit.setItem(41, ph);
        mapkit.setItem(42, ph);

        if (!player.hasPermission("util.command.player")) {
            player.sendMessage(Color.translate(Core.getInstance().getConfig().getString("NoPerm")));
            return true;
        }
        if (args.length >= 0) {
            sender.sendMessage(Color.translate("&cOpening kitmap GUI..."));
            player.openInventory(mapkit);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (e.getInventory().getName().equals("§4§lMap Kit")) {
            e.setCancelled(true);
        }
    }

}
