package org.sedateplace.yujinnetwork;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class YujinNetworkPl extends JavaPlugin implements Listener {

    private EconomyManager economyManager;
    private ConfigManager configManager;
    private Gems gems;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        economyManager = new EconomyManager(configManager);
        gems = new Gems(economyManager, configManager);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(economyManager, configManager), this);
        getServer().getPluginManager().registerEvents(gems, this);

        getCommand("eco").setExecutor(new EcoCommandExecutor(economyManager, configManager));
        getCommand("balance").setExecutor(new MoneyCommandExecutor(economyManager));
        getCommand("gamemode").setExecutor(new Comandi());
        getCommand("yujinNetwork").setExecutor(new ReloadCommandExecutor(this));
        getLogger().info(ChatColor.GREEN + getName() + " versione " + getDescription().getVersion() + " è stato abilitato");
    }

    @Override
    public void onDisable() {
        configManager.savePlayerData(economyManager.getPlayerBalances());
        configManager.saveBlackListData();
        configManager.saveMessagesData();
        PluginDisableEvent disableEvent = new PluginDisableEvent(this);
        getServer().getPluginManager().callEvent(disableEvent);
        getLogger().info(ChatColor.RED + getName() + " versione " + getDescription().getVersion() + " è stato disabilitato");
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        gems.onBlockBreak(event);
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        gems.onBlockPlace(event);
    }
}