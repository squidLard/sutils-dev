package com.squidlard.hcf;

import com.squidlard.hcf.Commands.*;
import com.squidlard.hcf.Listeners.*;
import com.squidlard.hcf.Utilities.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Core extends JavaPlugin implements Listener {

    private static Core instance;
    private VanishListener vanishListener;
    private FreezeListener freezeListener;
    private ModModeListener modModeListener;
    private ChatListener chatListener;
    private InspectListener inspectListener;
    private Set<UUID> donators = new HashSet<>();

    public FreezeListener getFreezeListener() {
        return this.freezeListener;
    }

    public ModModeListener getModModeListener() {
        return this.modModeListener;
    }

    public VanishListener getVanishListener() {
        return this.vanishListener;
    }

    public ChatListener getChatListener() {
        return this.chatListener;
    }

    public InspectListener getInspectListener() {
        return this.inspectListener;
    }

    public static Core getInstance()
    {
        if (instance == null) {
            instance = new Core();
        }
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registerListeners();
        registerCommands();
        CallDonators();
        refreshOnlineStaff();
        setMiner();
        saveDefaultConfig();
        onEnableMod();
    }

    @Override
    public void onDisable() {
        instance = null;

        onDisableMod();
    }

    public void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        this.freezeListener = new FreezeListener();
        this.modModeListener = new ModModeListener();
        this.vanishListener = new VanishListener();
        this.chatListener = new ChatListener();
        pm.registerEvents(this, this);
        pm.registerEvents(this.modModeListener, this);
        pm.registerEvents(this.freezeListener, this);
        pm.registerEvents(this.vanishListener, this);
        pm.registerEvents(this.chatListener, this);
        pm.registerEvents(new VanishListener(), this);
        pm.registerEvents(new MapKitCommand(), this);
        pm.registerEvents(new InspectCommand(), this);
        pm.registerEvents(new CrowbarListener(this), this);
        pm.registerEvents(new FlyCommand(), this);
    }

    public void registerCommands() {
        getCommand("playtime").setExecutor(new PlayTimeCommand());
        getCommand("gmc").setExecutor(new GameModeCommand());
        getCommand("gms").setExecutor(new GameModeCommand());
        getCommand("tphere").setExecutor(new TeleportCommand());
        getCommand("tp").setExecutor(new TeleportCommand());
        getCommand("ores").setExecutor(new OresCommand());
        getCommand("crowgive").setExecutor(new CrowBarCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("inspect").setExecutor(new InspectCommand());
        getCommand("copyinv").setExecutor(new CopyInvCommand());
        getCommand("mapkit").setExecutor(new MapKitCommand());
        getCommand("help").setExecutor(new HelpCommand());
        getCommand("sutils").setExecutor(new ReloadCommand());
        getCommand("coords").setExecutor(new CoordsCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("request").setExecutor(new RequestCommand());
        getCommand("staffchat").setExecutor(new StaffChatCommand());
        getCommand("rename").setExecutor(new RenameCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("mutechat").setExecutor(new MuteChatCommand());
        getCommand("vanish").setExecutor(new VanishCommand());
        getCommand("mod").setExecutor(new ModCommand());
        getCommand("cc").setExecutor(new ClearChatCommand());
        getCommand("blacklist").setExecutor(new BlackListCommand());
        getCommand("list").setExecutor(new ListCommand());
    }

    public static  String translate(String text) {
        return ChatColor.translateAlternateColorCodes('&', Core.getInstance().getConfig().getString(text));
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void setDonator(Player player) {
        this.donators.add(player.getUniqueId());
    }

    public void removeDonator(UUID uuid, Player player) {
        this.donators.remove(player.getUniqueId());
    }

    public boolean isDonor(Player player) {
        return this.donators.contains(player.getUniqueId());
    }

    public List<String> getStringList(String path)
    {
        if (getConfig().contains(path))
        {
            ArrayList<String> lines = new ArrayList();
            for (String text : getConfig().getStringList(path)) {
                lines.add(new Color().translate(text));
            }
            return lines;
        }
        return Arrays.asList(new String[] { new Color().translate("&cString list not found: '" + path + "'") });
    }

    public static boolean isMod(Player player) {
        if (player.hasPermission("util.command.basic")) {
            return true;
        }
        return false;
    }

    public static boolean isAdmin(Player player) {
        if (player.hasPermission("util.command.admin")) {
            return true;
        }
        return false;
    }

    public void refreshOnlineStaff() {
        new BukkitRunnable() {
            public void run() {
                Player[] arrayOfPlayer;
                int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
                for (int i = 0; i < j; i++) {
                    Player online = arrayOfPlayer[i];
                    if (online.hasPermission("util.command.basic")) {
                        if (getModModeListener().isStaffModeActive(online)) {
                            getModModeListener().updateStaffItem(online);
                            getModModeListener().updateMinerItem(online);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);
    }

    public void setMiner() {
        new BukkitRunnable() {
            public void run() {
                Player[] arrayOfPlayer;
                int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
                for (int i = 0; i < j; i++) {
                    Player online = arrayOfPlayer[i];
                    if ((online.getLocation().getBlockY() <= 30) && (!online.hasPermission("util.command.basic"))) {
                        getModModeListener().miner.add(online.getUniqueId());
                    } else {
                        getModModeListener().miner.remove(online.getUniqueId());
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);
    }

    public void onEnableMod() {
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
        for (int i = 0; i < j; i++) {
            Player online = arrayOfPlayer[i];
            if ((isMod(online)) && (!this.getModModeListener().isStaffModeActive(online))) {
                this.getModModeListener().setStaffmode(online, true);
                this.getModModeListener().staff.add(online.getUniqueId());
            }
        }
    }

    public void onDisableMod() {
        Player[] arrayOfPlayer;
        int j = (arrayOfPlayer = Bukkit.getOnlinePlayers()).length;
        for (int i = 0; i < j; i++) {
            Player online = arrayOfPlayer[i];
            if ((isMod(online)) && (this.getModModeListener().isStaffModeActive(online))) {
                this.getModModeListener().setStaffmode(online, false);
                this.getModModeListener().staff.remove(online.getUniqueId());
                online.sendMessage(Color.translate("&cMod mode was disabled due to a reload."));
            }
        }
    }

    public void CallDonators() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (donators.size() == 0) {
                    return;
                }
                if (donators.size() == 1) {
                    Bukkit.broadcastMessage(Color.translate ("&6Online Top Donator: " + "&7" + donators.toString().replace("[", "").replace("]", "") + "&6."));
                    return;
                }
                if (donators.size() > 1) {
                    Bukkit.broadcastMessage(Color.translate("&6Online Top Donator's: " + "&7" + donators.toString().replace("[", "").replace("]", "") + "&6."));
                    return;
                }
            }
        }, 5 * 20, 300 * 20);
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) {
            this.donators.remove(player.getUniqueId());
            return;
        }
        if (player.hasPermission("*")) {
            this.donators.remove(player.getUniqueId());
            return;
        }
        if (player.hasPermission("util.donator.set")) {
            this.donators.add(player.getUniqueId());
            return;
        }
    }

    @EventHandler
    public void on(PlayerQuitEvent e) {
        Player player = e.getPlayer();
            if (this.donators.contains(player.getUniqueId())) {
                this.donators.remove(player.getUniqueId());
                return;
            }
    }
}
