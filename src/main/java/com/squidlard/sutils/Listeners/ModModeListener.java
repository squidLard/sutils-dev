package com.squidlard.sutils.Listeners;

import com.squidlard.sutils.Core;
import com.squidlard.sutils.Utilities.*;
import com.squidlard.sutils.Utilities.Color;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static com.squidlard.sutils.Core.*;

public class ModModeListener implements Listener{

    private final Core core = Core.getInstance();
    private final Set<UUID> staffmode = new HashSet<>();
    private final HashMap<String, ItemStack[]> armorContents = new HashMap();
    private final HashMap<String, ItemStack[]> inventoryContents = new HashMap();
    private final HashMap<String, Integer> xplevel = new HashMap();
    public static Set<UUID> staff = new HashSet<>();

    public boolean isStaffModeActive(Player player) {
        return this.staffmode.contains(player.getUniqueId());
    }

    public void setStaffMode(Player player, boolean status) {
        if (status) {
            if (isMod(player)) {
                this.staffmode.add(player.getUniqueId());
                saveInventory(player);
                player.setGameMode(GameMode.CREATIVE);
                this.core.getVanishListener().setVanished(player, true);
                player.sendMessage(Color.translate(core.getConfig().getString("ModEnableMsg").replace("%player%", player.getName())));
                player.getInventory().clear();
                ModItems.giveItems(player);
            }
            else
            {
                player.sendMessage(core.getConfig().getString("NoPerm"));
            }
        }
        else
        {
            this.staffmode.remove(player.getUniqueId());
            player.getInventory().clear();
            loadInventory(player);
            player.setGameMode(GameMode.SURVIVAL);
            this.core.getVanishListener().setVanished(player, false);
            player.sendMessage(Color.translate(core.getConfig().getString("ModDisableMsg").replace("%player%", player.getName())));
        }
    }

    public void saveInventory(Player p)
    {
        armorContents.put(p.getName(), p.getInventory().getArmorContents());
        inventoryContents.put(p.getName(), p.getInventory().getContents());
        xplevel.put(p.getName(), Integer.valueOf(p.getLevel()));
    }

    public void loadInventory(Player p)
    {
        p.getInventory().clear();
        p.getInventory().setContents((ItemStack[])inventoryContents.get(p.getName()));
        p.getInventory().setArmorContents((ItemStack[])armorContents.get(p.getName()));
        p.setLevel(((Integer)xplevel.get(p.getName())).intValue());

        inventoryContents.remove(p.getName());
        armorContents.remove(p.getName());
        xplevel.remove(p.getName());
    }

    @EventHandler
    public void InteractEvents(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack itemstack = p.getItemInHand();

        ArrayList<String> enabledlore = new ArrayList();
        ArrayList<String> disabledlore = new ArrayList();

        for (String svanish : Core.getInstance().getConfig().getStringList("VanishEnableObject.Lore")) {
            enabledlore.add(Color.translate(svanish));
        }
        for (String svanish2 : Core.getInstance().getConfig().getStringList("VanishDisableObject.Lore")) {
            disabledlore.add(Color.translate(svanish2));
        }


        ItemStack disabled = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemStack enabled = new ItemStack(Material.INK_SACK, 1, (short) 8);

        ItemMeta enabledmeta = enabled.getItemMeta();
        ItemMeta disabledmeta = disabled.getItemMeta();

        disabledmeta.setDisplayName(Color.translate(Core.getInstance().getConfig().getString("VanishDisableObject.Name")));
        enabledmeta.setDisplayName(Color.translate(Core.getInstance().getConfig().getString("VanishEnableObject.Name")));

        enabledmeta.setLore(enabledlore);
        disabledmeta.setLore(disabledlore);

        disabled.setItemMeta(disabledmeta);
        enabled.setItemMeta(enabledmeta);
        if (isStaffModeActive(p)) {
            if (event.getAction().toString().contains("RIGHT")) {
                if ((itemstack.getType() == Material.INK_SACK) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(Core.getInstance().getConfig().getString("VanishEnableObject.Name"))))) {
                    this.core.getVanishListener().setVanished(p, false);
                    p.sendMessage(com.squidlard.sutils.Utilities.Color.translate(Core.getInstance().getConfig().getString("VanishDisableMsg").replace("%player%", p.getName())));
                    p.getInventory().setItem(this.core.getConfig().getInt("VanishDisableObject.InventorySlot") - 1, disabled);
                    return;
                }
                if ((itemstack.getType() == Material.INK_SACK) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(Core.getInstance().getConfig().getString("VanishDisableObject.Name"))))) {
                    this.core.getVanishListener().setVanished(p, true);
                    p.sendMessage(com.squidlard.sutils.Utilities.Color.translate(Core.getInstance().getConfig().getString("VanishEnableMsg").replace("%player%", p.getName())));
                    p.getInventory().setItem(this.core.getConfig().getInt("VanishEnableObject.InventorySlot") - 1, enabled);
                    return;
                }
                if ((itemstack.getType() == Material.NETHER_STAR) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(Core.getInstance().getConfig().getString("RandomTPObject.Name"))))) {
                    if (Bukkit.getOnlinePlayers().length > 1) {
                        Random random = new Random();
                        int online = Bukkit.getOnlinePlayers().length;
                        int randomp = random.nextInt(online);
                        Player Random = Bukkit.getOnlinePlayers()[randomp];
                        if (online > 1) {
                            if (Random == p) {
                                random.nextInt(online);
                                InteractEvents(event);
                            } else {
                                p.sendMessage(Color.translate(this.core.getConfig().getString("RandomTpMsg").replace("%target%", Random.getName())));
                                p.teleport(Random);
                            }
                        }
                    }
                    else
                    {
                        p.sendMessage(Color.translate("&cThere are not enough players online to use this."));
                    }
                }
                if ((itemstack.getType() == Material.SKULL_ITEM) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(Core.getInstance().getConfig().getString("StaffObject.Name"))))) {
                    if (this.staff.size() >= 1) {
                        openStaff(p);
                    } else {
                        p.sendMessage(Color.translate("&cNo Staff Online"));
                    }
                }
                if ((itemstack.getType() == Material.DIAMOND_PICKAXE) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(Core.getInstance().getConfig().getString("DiamondPick"))))) {
                    Player random = Bukkit.getOnlinePlayers()[new java.util.Random().nextInt(Bukkit.getOnlinePlayers().length)];
                    if (Bukkit.getOnlinePlayers().length == 1) {
                        p.sendMessage(ChatColor.RED + "There are not enough players online to use this.");
                    }
                    if (Bukkit.getOnlinePlayers().length > 1) {
                        if (random.getLocation().getY() <= 25) {
                            if (p != random) {
                                p.teleport(random);
                                p.sendMessage(Color.translate(Core.getInstance().getConfig().getString("RandomTpMsg").replace("%target%", random.getName())));
                            }
                        }
                        if (random.getLocation().getY() > 25) {
                            event.setCancelled(true);
                        }
                        if (p == random) {
                            event.setCancelled(true);
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) &&
                ((block.getType() == Material.CHEST) || (block.getType() == Material.TRAPPED_CHEST)) &&
                (this.isStaffModeActive(player)))
        {
            e.setCancelled(true);
            Inventory inv = Bukkit.createInventory(null, 54, Color.translate("&cSilent Chest"));
            Chest chest = (Chest)block.getState();
            inv.setContents(chest.getInventory().getContents());
            player.openInventory(inv);
        }
    }

    @EventHandler
    public void rightClick(PlayerInteractEntityEvent e)
    {
        Player player = e.getPlayer();
        Player target = (Player)e.getRightClicked();
        if ((target instanceof Player) && player instanceof Player) {
            if (isStaffModeActive(player)) {
                if ((player.getItemInHand().getType() == Material.BOOK) || (player.getItemInHand().getType() == Material.AIR)) {
                    InspectListener.InspectPlayerEvent(player, target);
                }
            }
        } else {
            return;
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if ((e.getWorld().getEnvironment() == World.Environment.NORMAL) &&
                (e.getWorld().getWeatherDuration() > 0)) {
            e.setCancelled(true);
            e.getWorld().setWeatherDuration(0);
        }
    }

    @EventHandler
    public void onTag(EntityDamageByEntityEvent e) {
        if ((!(e.getEntity() instanceof Player)) || (!(e.getDamager() instanceof Player))) {
            return;
        }
        Player player = (Player) e.getDamager();
        if (isStaffModeActive(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (isStaffModeActive(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (isStaffModeActive(player)) {
            if (isAdmin(player)) {
                return;
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onStaffClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().contains("§cOnline ")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSelecthead(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack clicked = e.getCurrentItem();
        if (inv.getName().equalsIgnoreCase("§cOnline Staff")) {
            if ((clicked.getType() == Material.AIR) ||(clicked.getType() == null)) {
                return;
            }
            if (clicked.getType() == Material.SKULL_ITEM) {
                Player click = Bukkit.getPlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                if (click == null) {
                    player.sendMessage(Color.translate("That staff member is no longer online."));
                    player.closeInventory();
                    return;
                }
                if (click == player) {
                    player.sendMessage(Color.translate("&cYou can not teleport to yourself."));
                    return;
                }
                player.closeInventory();
                player.teleport(click);
                player.sendMessage(Color.translate("&aYou were teleported to " + click.getName() + "."));
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        if(isStaffModeActive(player)) {
            ItemStack item = e.getItemDrop().getItemStack().clone();
            e.getItemDrop().remove();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (isStaffModeActive(player)) {
            if (isAdmin(player)) {
                return;
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (isStaffModeActive(player)) {
            if (isAdmin(player)) {
                return;
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityTag(EntityDamageByEntityEvent e) {
        if ((!(e.getEntity() instanceof Entity)) || (!(e.getDamager() instanceof Player))) {
            return;
        }
        Player player = (Player) e.getDamager();
        if (isStaffModeActive(player)) {
            e.setCancelled(true);
        }
    }

    public void openStaff(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, Color.translate("&cOnline Staff"));
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
        for (int i = 0; i < j; i++) {
            Player staff = arrayOfPlayer[i];
            if (staff.hasPermission("util.command.basic")) {
                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
                ItemMeta headmeta = head.getItemMeta();
                ItemMeta fillermeta = filler.getItemMeta();
                headmeta.setDisplayName(Color.translate("&b" + staff.getName()));
                fillermeta.setDisplayName(" ");
                ArrayList<String> headlore = new ArrayList();
                headlore.add(Color.translate("&7Click to teleport to player."));
                headmeta.setLore(headlore);
                filler.setItemMeta(fillermeta);
                head.setItemMeta(headmeta);
                gui.setItem(0, filler);
                gui.setItem(1, filler);
                gui.setItem(2, filler);
                gui.setItem(3, filler);
                gui.setItem(4, filler);
                gui.setItem(5, filler);
                gui.setItem(6, filler);
                gui.setItem(7, filler);
                gui.setItem(8, filler);
                gui.setItem(45, filler);
                gui.setItem(46, filler);
                gui.setItem(47, filler);
                gui.setItem(48, filler);
                gui.setItem(49, filler);
                gui.setItem(50, filler);
                gui.setItem(51, filler);
                gui.setItem(52, filler);
                gui.setItem(53, filler);
                gui.addItem(new ItemStack[] { head });
            }
        }
        player.openInventory(gui);
    }

    @EventHandler
    public void onJoinMod(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        if (player.hasPermission("util.command.basic")) {
            this.setStaffMode(player, true);
            staff.add(player.getUniqueId());
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if ((staff.hasPermission("util.command.basic") && (this.core.getConfig().getBoolean("JoinLeaveMessages")))) {
                    staff.sendMessage(Color.translate(Core.getInstance().getConfig().getString("StaffPrefix") + player.getName() + " has joined the server."));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (staff.contains(player.getUniqueId())) {
            staff.remove(player.getUniqueId());
        }
        if (player.hasPermission("util.command.basic")) {
            this.setStaffMode(player, false);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if ((staff.hasPermission("util.command.basic") && (this.core.getConfig().getBoolean("JoinLeaveMessages")))) {
                    staff.sendMessage(Color.translate(Core.getInstance().getConfig().getString("StaffPrefix") + player.getName() + " has left the server."));
                }
            }
        }
    }
}
