package com.squidlard.hcf.Listeners;

import com.squidlard.hcf.Core;
import com.squidlard.hcf.Utilities.TimeFormat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ModModeListener implements Listener{

    private final Core core = Core.getInstance();
    private final Set<UUID> staffmode = new HashSet<>();
    private final HashMap<String, ItemStack[]> armorcontents = new HashMap<>();
    private final HashMap<String, ItemStack[]> inventorycontents = new HashMap<>();
    private final HashMap<String, Integer> xplevel = new HashMap<>();
    public Set<UUID> staff = new HashSet<>();
    public Set<UUID> miner = new HashSet<>();

    public void setStaffmode(Player player, Boolean status) {
        if (status) {
            if (this.core.isMod(player)) {
                this.staffmode.add(player.getUniqueId());
                saveInventory(player);
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(this.core.color(this.core.getConfig().getString("ModEnableMsg").replace("{player}", player.getName())));
                this.core.getVanishListener().setVanished(player, true);
                player.getInventory().clear();
                giveModItems(player);
            } else {
                player.sendMessage(this.core.color(this.core.getConfig().getString("NoPerm")));
            }
        } else {
            this.staffmode.remove(player.getUniqueId());
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(this.core.color(this.core.getConfig().getString("ModDisableMsg").replace("{player}", player.getName())));
            loadInventory(player);
            this.core.getVanishListener().setVanished(player, false);
        }
    }

    public boolean isStaffModeActive(Player player) {
        return this.staffmode.contains(player.getUniqueId());
    }

    public void saveInventory(Player player) {
        armorcontents.put(player.getName(), player.getInventory().getArmorContents());
        inventorycontents.put(player.getName(), player.getInventory().getContents());
        xplevel.put(player.getName(), Integer.valueOf(player.getLevel()));
    }

    public void loadInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents((ItemStack[])inventorycontents.get(player.getName()));
        player.getInventory().setArmorContents((ItemStack[])armorcontents.get(player.getName()));
        player.setLevel(((Integer)xplevel.get(player.getName())).intValue());

        inventorycontents.remove(player.getName());
        armorcontents.remove(player.getName());
        xplevel.remove(player.getName());
    }

    public void giveModItems(Player player) {
        ItemStack Compass = new ItemStack(Material.COMPASS);
        ItemStack Book = new ItemStack(Material.BOOK);
        ItemStack RandomTP = new ItemStack(Material.NETHER_STAR);
        ItemStack Air = new ItemStack(Material.AIR);
        ItemStack vanish = new ItemStack(Material.INK_SACK, 1, (short)8);
        ItemStack Staff = new ItemStack(Material.SKULL_ITEM, staff.size());
        ItemStack carpet = new ItemStack(Material.CARPET, 1, (short)3);
        ItemStack Pick = new ItemStack(Material.DIAMOND_PICKAXE, miner.size());

        ItemMeta carpetmeta = carpet.getItemMeta();
        ItemMeta StaffMeta = Staff.getItemMeta();
        ItemMeta Compassmeta = Compass.getItemMeta();
        ItemMeta Bookmeta = Book.getItemMeta();
        ItemMeta RandomTPmeta = RandomTP.getItemMeta();
        ItemMeta Vanishmeta = vanish.getItemMeta();
        ItemMeta airmeta = Air.getItemMeta();
        ItemMeta Pickmeta = Pick.getItemMeta();

        Pickmeta.setDisplayName(this.core.translate("MinerObject.Name"));
        carpetmeta.setDisplayName(this.core.translate("CarpetObject.Name"));
        StaffMeta.setDisplayName(this.core.translate("StaffObject.Name"));
        Vanishmeta.setDisplayName(this.core.translate("VanishEnableObject.Name"));
        Compassmeta.setDisplayName(this.core.translate("BlockTeleportObject.Name"));
        Bookmeta.setDisplayName(this.core.translate("InspectObject.Name"));
        RandomTPmeta.setDisplayName(this.core.translate("RandomTPObject.Name"));

        ArrayList<String> carpetlore = new ArrayList<>();
        ArrayList<String> Stafflore = new ArrayList<>();
        ArrayList<String> vanishlore = new ArrayList<>();
        ArrayList<String> Compasslore = new ArrayList<>();
        ArrayList<String> Booklore = new ArrayList<>();
        ArrayList<String> RandomTPlore = new ArrayList<>();
        ArrayList<String> PickLore = new ArrayList<>();

        for (String scompass : Core.getInstance().getConfig().getStringList("BlockTeleportObject.Lore")) {
            Compasslore.add(this.core.color(scompass));
        }
        for (String sbook : Core.getInstance().getConfig().getStringList("InspectObject.Lore")) {
            Booklore.add(this.core.color(sbook));
        }
        for (String svanish : Core.getInstance().getConfig().getStringList("VanishEnableObject.Lore")) {
            vanishlore.add(this.core.color(svanish));
        }
        for (String srandom : Core.getInstance().getConfig().getStringList("RandomTPObject.Lore")) {
            RandomTPlore.add(this.core.color(srandom));
        }
        for (String shead : Core.getInstance().getConfig().getStringList("StaffObject.Lore")) {
            Stafflore.add(this.core.color(shead));
        }
        for (String scarpet : Core.getInstance().getConfig().getStringList("CarpetObject.Lore")) {
            carpetlore.add(this.core.color(scarpet));
        }
        for (String spick : Core.getInstance().getConfig().getStringList("MinerObject.Lore")) {
            PickLore.add(this.core.color(spick));
        }

        Pickmeta.setLore(PickLore);
        StaffMeta.setLore(Stafflore);
        Vanishmeta.setLore(vanishlore);
        Compassmeta.setLore(Compasslore);
        Bookmeta.setLore(Booklore);
        RandomTPmeta.setLore(RandomTPlore);
        carpetmeta.setLore(carpetlore);

        Pick.setItemMeta(Pickmeta);
        Staff.setItemMeta(StaffMeta);
        Compass.setItemMeta(Compassmeta);
        Book.setItemMeta(Bookmeta);
        RandomTP.setItemMeta(RandomTPmeta);
        vanish.setItemMeta(Vanishmeta);
        Air.setItemMeta(airmeta);
        carpet.setItemMeta(carpetmeta);

        if (this.core.getConfig().getBoolean("MinerObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("MinerObject.InventorySlot") - 1, Pick);
        }
        if (this.core.getConfig().getBoolean("BlockTeleportObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("BlockTeleportObject.InventorySlot") - 1, Compass);
        }
        if (this.core.getConfig().getBoolean("InspectObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("InspectObject.InventorySlot") -1, Book);
        }
        if (this.core.getConfig().getBoolean("RandomTPObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("RandomTPObject.InventorySlot") - 1, RandomTP);
        }
        if (this.core.getConfig().getBoolean("CarpetObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("CarpetObject.InventorySlot") - 1, carpet);
        }
        if (this.core.getConfig().getBoolean("StaffObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("StaffObject.InventorySlot") - 1, Staff);
        }
        if (this.core.getConfig().getBoolean("VanishEnableObject.Enabled") == true) {
            player.getInventory().setItem(Core.getInstance().getConfig().getInt("VanishEnableObject.InventorySlot") - 1, vanish);
        }

        player.getInventory().setHelmet(Air);
        player.getInventory().setChestplate(Air);
        player.getInventory().setLeggings(Air);
        player.getInventory().setBoots(Air);
    }

    @EventHandler
    public void InteractEvents(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack itemstack = p.getItemInHand();

        ArrayList<String> enabledlore = new ArrayList();
        ArrayList<String> disabledlore = new ArrayList();

        for (String svanish : Core.getInstance().getConfig().getStringList("VanishEnableObject.Lore")) {
            enabledlore.add(this.core.color(svanish));
        }
        for (String svanish2 : Core.getInstance().getConfig().getStringList("VanishDisableObject.Lore")) {
            disabledlore.add(this.core.color(svanish2));
        }


        ItemStack disabled = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemStack enabled = new ItemStack(Material.INK_SACK, 1, (short) 8);

        ItemMeta enabledmeta = enabled.getItemMeta();
        ItemMeta disabledmeta = disabled.getItemMeta();

        disabledmeta.setDisplayName(this.core.color(Core.getInstance().getConfig().getString("VanishDisableObject.Name")));
        enabledmeta.setDisplayName(this.core.color(Core.getInstance().getConfig().getString("VanishEnableObject.Name")));

        enabledmeta.setLore(enabledlore);
        disabledmeta.setLore(disabledlore);

        disabled.setItemMeta(disabledmeta);
        enabled.setItemMeta(enabledmeta);
        if (isStaffModeActive(p)) {
            if (event.getAction().toString().contains("RIGHT")) {
                if ((itemstack.getType() == Material.INK_SACK) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(this.core.color(Core.getInstance().getConfig().getString("VanishEnableObject.Name"))))) {
                    this.core.getVanishListener().setVanished(p, false);
                    p.sendMessage(this.core.color(this.core.getConfig().getString("VanishDisableMsg").replace("{player}", p.getName())));
                    p.getInventory().setItem(this.core.getConfig().getInt("VanishDisableObject.InventorySlot") - 1, disabled);
                    return;
                }
                if ((itemstack.getType() == Material.INK_SACK) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(this.core.color(Core.getInstance().getConfig().getString("VanishDisableObject.Name"))))) {
                    this.core.getVanishListener().setVanished(p, true);
                    p.sendMessage(this.core.color(this.core.getConfig().getString("VanishEnableMsg").replace("{player}", p.getName())));
                    p.getInventory().setItem(this.core.getConfig().getInt("VanishEnableObject.InventorySlot") - 1, enabled);
                    return;
                }
                if ((itemstack.getType() == Material.NETHER_STAR) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(this.core.color(Core.getInstance().getConfig().getString("RandomTPObject.Name"))))) {
                    if (Bukkit.getOnlinePlayers().length > 1) {
                        Random random = new Random();
                        int online = Bukkit.getOnlinePlayers().length;
                        int randomp = random.nextInt(online);
                        int staff = this.staff.size();
                        Player Random = Bukkit.getOnlinePlayers()[randomp];
                        if (online == staff) {
                            p.sendMessage(this.core.color("&cThere are not enough players online to use this."));
                            return;
                        }
                        if (online > 1) {
                            if ((Random == p) || (this.core.isMod(Random))) {
                                random.nextInt(online);
                                InteractEvents(event);
                            } else {
                                p.sendMessage(this.core.color(this.core.getConfig().getString("RandomTpMsg").replace("{player}", Random.getName())));
                                p.teleport(Random);
                            }
                        }
                    }
                    else
                    {
                        p.sendMessage(this.core.color("&cThere are not enough players online to use this."));
                    }
                }
                if ((itemstack.getType() == Material.SKULL_ITEM) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(this.core.color(Core.getInstance().getConfig().getString("StaffObject.Name"))))) {
                    if (this.staff.size() >= 1) {
                        openStaff(p);
                    } else {
                        p.sendMessage(this.core.color("&cNo staff online."));
                    }
                }
                if ((itemstack.getType() == Material.DIAMOND_PICKAXE) && (itemstack.getItemMeta().getDisplayName().equalsIgnoreCase(this.core.color(Core.getInstance().getConfig().getString("MinerObject.Name"))))) {
                    if (this.miner.size() >= 1) {
                        openMiner(p);
                    } else {
                        p.sendMessage(this.core.color("&cNo miners online."));
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
            Inventory inv = Bukkit.createInventory(null, 54, this.core.color("&cSilent Chest"));
            Chest chest = (Chest)block.getState();
            inv.setContents(chest.getInventory().getContents());
            player.openInventory(inv);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player instanceof Player) {
            if (this.isStaffModeActive(player)) {
                event.getDrops().clear();
            }
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
                    this.core.getInspectListener().InspectPlayerEvent(player, target);
                }
            }
        } else {
            return;
        }
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (this.staffmode.contains(player)) {
            this.setStaffmode(player, false);
            this.setStaffmode(player, true);
        }
    }

    @EventHandler
    public void on(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (this.staffmode.contains(player)) {
            event.setCancelled(true);
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
            e.setCancelled(true);
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
                    player.sendMessage(this.core.color("That staff member is no longer online."));
                    player.closeInventory();
                    return;
                }
                if (click == player) {
                    player.sendMessage(this.core.color("&cYou can not teleport to yourself."));
                    return;
                }
                player.closeInventory();
                player.teleport(click);
                player.sendMessage(this.core.color("&aYou were teleported to " + click.getName() + "."));
            }
        }
    }

    @EventHandler
    public void onSelectMiner(InventoryClickEvent e) {
        Player player = (Player)e.getWhoClicked();
        Inventory inv = e.getInventory();
        ItemStack clicked = e.getCurrentItem();
        if (inv.getName().equalsIgnoreCase("§cOnline Miners")) {
            if ((clicked.getType() == Material.AIR) ||(clicked.getType() == null)) {
                return;
            }
            if (clicked.getType() == Material.SKULL_ITEM) {
                Player click = Bukkit.getPlayer(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()));
                if (click == null) {
                    player.sendMessage(this.core.color("That player is no longer online."));
                    player.closeInventory();
                    return;
                }
                if (click == player) {
                    player.sendMessage(this.core.color("&cYou can not teleport to yourself."));
                    return;
                }
                player.closeInventory();
                player.teleport(click);
                player.sendMessage(this.core.color("&aYou were teleported to " + click.getName() + "."));
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
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (isStaffModeActive(player)) {
            event.setCancelled(true);
        }
    }

    public void openStaff(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, this.core.color("&cOnline Staff"));
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
        for (int i = 0; i < j; i++) {
            Player staff = arrayOfPlayer[i];
            if (staff.hasPermission("util.command.basic")) {
                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
                ItemMeta headmeta = head.getItemMeta();
                ItemMeta fillermeta = filler.getItemMeta();
                headmeta.setDisplayName(this.core.color("&b" + staff.getName()));
                fillermeta.setDisplayName(" ");
                ArrayList<String> headlore = new ArrayList();
                headlore.add(this.core.color("&7Playtime: &e" + TimeFormat.getTime(staff.getStatistic(Statistic.PLAY_ONE_TICK) / 20)));
                headlore.add(this.core.color("&7Click to teleport to player."));
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

    public void openMiner(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, this.core.color("&cOnline Miners"));
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
        for (int i = 0; i < j; i++) {
            Player miner = arrayOfPlayer[i];
            if ((miner.getLocation().getBlockY() <= 30) && (!miner.hasPermission("util.command.basic"))) {
                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                ItemMeta headmeta = head.getItemMeta();
                ItemMeta fillermeta = filler.getItemMeta();
                headmeta.setDisplayName(this.core.color("&b" + miner.getName()));
                fillermeta.setDisplayName(" ");
                ArrayList<String> headlore = new ArrayList();
                headlore.add(this.core.color("&7Diamonds: &e" + miner.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE)));
                headlore.add(this.core.color("&7Y Level: &e" + miner.getLocation().getBlockY()));
                headlore.add(this.core.color("&7Click to teleport to player."));
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
                gui.addItem(new ItemStack[]{head});
            }
        }
        player.openInventory(gui);
    }

    @EventHandler
    public void onJoinMod(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(null);
        if (player.hasPermission("util.command.basic")) {
            this.setStaffmode(player, true);
            staff.add(player.getUniqueId());
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if ((staff.hasPermission("util.command.basic") && (this.core.getConfig().getBoolean("JoinLeaveMessages")))) {
                    staff.sendMessage(this.core.color(this.core.getConfig().getString("StaffPrefix") + player.getName() + " has joined the server."));
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (miner.contains(player.getUniqueId())) {
            miner.remove(player.getUniqueId());
        }
        if (staff.contains(player.getUniqueId())) {
            staff.remove(player.getUniqueId());
        }
        if (player.hasPermission("util.command.basic")) {
            if (this.isStaffModeActive(player)) {
                this.setStaffmode(player, false);
            } else {
                return;
            }
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if ((staff.hasPermission("util.command.basic") && (this.core.getConfig().getBoolean("JoinLeaveMessages")))) {
                    staff.sendMessage(this.core.color(this.core.getConfig().getString("StaffPrefix") + player.getName() + " has left the server."));
                }
            }
        }
    }

    public void updateStaffItem(Player p) {
        ItemStack Staff = new ItemStack(Material.SKULL_ITEM, staff.size());
        ItemMeta StaffMeta = Staff.getItemMeta();
        StaffMeta.setDisplayName(this.core.translate("StaffObject.Name"));
        ArrayList<String> Stafflore = new ArrayList<>();
        for (String shead : this.core.getConfig().getStringList("StaffObject.Lore")) {
            Stafflore.add(this.core.color(shead));
        }
        StaffMeta.setLore(Stafflore);
        Staff.setItemMeta(StaffMeta);
        if (this.core.getConfig().getBoolean("StaffObject.Enabled") == true) {
            p.getInventory().setItem(this.core.getConfig().getInt("StaffObject.InventorySlot") - 1, Staff);
        }
        this.staff.add(p.getUniqueId());
    }

    public void updateMinerItem(Player p) {
        ItemStack Staff = new ItemStack(Material.DIAMOND_PICKAXE, miner.size());
        ItemMeta StaffMeta = Staff.getItemMeta();
        StaffMeta.setDisplayName(this.core.translate("MinerObject.Name"));
        ArrayList<String> Stafflore = new ArrayList<>();
        for (String shead : this.core.getConfig().getStringList("MinerObject.Lore")) {
            Stafflore.add(this.core.color(shead));
        }
        StaffMeta.setLore(Stafflore);
        Staff.setItemMeta(StaffMeta);
        if (this.core.getConfig().getBoolean("MinerObject.Enabled") == true) {
            p.getInventory().setItem(this.core.getConfig().getInt("MinerObject.InventorySlot") - 1, Staff);
        } else {
            return;
        }
    }

    public void updateStaffInventory(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, this.core.color("&cOnline Staff"));
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
        for (int i = 0; i < j; i++) {
            Player staff = arrayOfPlayer[i];
            if (staff.hasPermission("util.command.basic")) {
                ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
                ItemMeta headmeta = head.getItemMeta();
                ItemMeta fillermeta = filler.getItemMeta();
                headmeta.setDisplayName(this.core.color("&b" + staff.getName()));
                fillermeta.setDisplayName(" ");
                ArrayList<String> headlore = new ArrayList();
                headlore.add(this.core.color("&7Playtime: &e" + TimeFormat.getTime(staff.getStatistic(Statistic.PLAY_ONE_TICK) / 20)));
                headlore.add(this.core.color("&7Click to teleport to player."));
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
}
