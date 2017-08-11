package com.squidlard.sutils.Utilities;

import com.squidlard.sutils.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

import static com.squidlard.sutils.Listeners.ModModeListener.staff;

public class ModItems implements Listener {

    public static  String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString(text));
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void giveItems(Player p) {

        ItemStack Compass = new ItemStack(Material.COMPASS);
        ItemStack Book = new ItemStack(Material.BOOK);
        ItemStack RandomTP = new ItemStack(Material.NETHER_STAR);
        ItemStack Air = new ItemStack(Material.AIR);
        ItemStack vanish = new ItemStack(Material.INK_SACK, 1, (short)8);
        ItemStack Staff = new ItemStack(Material.SKULL_ITEM, staff.size());
        ItemStack carpet = new ItemStack(Material.CARPET, 1, (short)3);

        ItemMeta carpetmeta = carpet.getItemMeta();
        ItemMeta StaffMeta = Staff.getItemMeta();
        ItemMeta Compassmeta = Compass.getItemMeta();
        ItemMeta Bookmeta = Book.getItemMeta();
        ItemMeta RandomTPmeta = RandomTP.getItemMeta();
        ItemMeta Vanishmeta = vanish.getItemMeta();
        ItemMeta airmeta = Air.getItemMeta();

        carpetmeta.setDisplayName(translate("CarpetObject.Name"));
        StaffMeta.setDisplayName(translate("StaffObject.Name"));
        Vanishmeta.setDisplayName(translate("VanishEnableObject.Name"));
        Compassmeta.setDisplayName(translate("BlockTeleportObject.Name"));
        Bookmeta.setDisplayName(translate("InspectObject.Name"));
        RandomTPmeta.setDisplayName(translate("RandomTPObject.Name"));

        ArrayList<String> carpetlore = new ArrayList<>();
        ArrayList<String> Stafflore = new ArrayList<>();
        ArrayList<String> vanishlore = new ArrayList<>();
        ArrayList<String> Compasslore = new ArrayList<>();
        ArrayList<String> Booklore = new ArrayList<>();
        ArrayList<String> RandomTPlore = new ArrayList<>();

        for (String scompass : Core.getInstance().getConfig().getStringList("BlockTeleportObject.Lore")) {
            Compasslore.add(color(scompass));
        }
        for (String sbook : Core.getInstance().getConfig().getStringList("InspectObject.Lore")) {
            Booklore.add(color(sbook));
        }
        for (String svanish : Core.getInstance().getConfig().getStringList("VanishEnableObject.Lore")) {
            vanishlore.add(color(svanish));
        }
        for (String srandom : Core.getInstance().getConfig().getStringList("RandomTPObject.Lore")) {
            RandomTPlore.add(color(srandom));
        }
        for (String shead : Core.getInstance().getConfig().getStringList("StaffObject.Lore")) {
            Stafflore.add(color(shead));
        }
        for (String scarpet : Core.getInstance().getConfig().getStringList("CarpetObject.Lore")) {
            carpetlore.add(color(scarpet));
        }

        StaffMeta.setLore(Stafflore);
        Vanishmeta.setLore(vanishlore);
        Compassmeta.setLore(Compasslore);
        Bookmeta.setLore(Booklore);
        RandomTPmeta.setLore(RandomTPlore);
        carpetmeta.setLore(carpetlore);

        Staff.setItemMeta(StaffMeta);
        Compass.setItemMeta(Compassmeta);
        Book.setItemMeta(Bookmeta);
        RandomTP.setItemMeta(RandomTPmeta);
        vanish.setItemMeta(Vanishmeta);
        Air.setItemMeta(airmeta);
        carpet.setItemMeta(carpetmeta);

        p.getInventory().setItem(Core.getInstance().getConfig().getInt("BlockTeleportObject.InventorySlot") - 1, Compass);
        p.getInventory().setItem(Core.getInstance().getConfig().getInt("InspectObject.InventorySlot") -1, Book);
        p.getInventory().setItem(Core.getInstance().getConfig().getInt("RandomTPObject.InventorySlot") - 1, RandomTP);
        p.getInventory().setItem(Core.getInstance().getConfig().getInt("CarpetObject.InventorySlot") - 1, carpet);
        p.getInventory().setItem(Core.getInstance().getConfig().getInt("StaffObject.InventorySlot") - 1, Staff);
        p.getInventory().setItem(Core.getInstance().getConfig().getInt("VanishEnableObject.InventorySlot") - 1, vanish);

        p.getInventory().setHelmet(Air);
        p.getInventory().setChestplate(Air);
        p.getInventory().setLeggings(Air);
        p.getInventory().setBoots(Air);

    }

}
