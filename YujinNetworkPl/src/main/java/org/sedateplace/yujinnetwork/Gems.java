package org.sedateplace.yujinnetwork;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;


public class Gems implements Listener {

    private final EconomyManager economyManager;
    private final ConfigManager configManager;
    private final HashMap<UUID, Double> blocksBrokenMap;
    private final HashMap<UUID, Double> blocksPlacedMap;
    private final HashMap<UUID, BossBar> bossBarMap;
    private Plugin plugin;
    public int gemsGiven;

    public Gems(EconomyManager economyManager, ConfigManager configManager) {
        this.economyManager = economyManager;
        this.configManager = configManager;
        this.blocksPlacedMap = new HashMap<>();
        this.blocksBrokenMap = new HashMap<>();
        this.bossBarMap = new HashMap<>();
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Material blockType = event.getBlock().getType();
        if (!configManager.isBlacklisted(blockType)) {
            double blocksBroken = blocksBrokenMap.getOrDefault(playerUUID, 0d) + 1;
            player.sendMessage("Hai rotto un blocco di " + blockType.toString());
            // Aggiorna la BossBar con il progresso dei blocchi rotti
            BossBar playerBossBar = getPlayerBossBar(player);
            playerBossBar.setProgress(blocksBroken / configManager.getSettingsConfig().getDouble("BlockBP"));
            // Aggiungi la BossBar al giocatore, se necessario
            if (!bossBarMap.containsKey(playerUUID)) {
                bossBarMap.put(playerUUID, playerBossBar);
                playerBossBar.addPlayer(player);
            }
            // Se il giocatore ha rotto abbastanza blocchi, dargli le gemme e azzerare il contatore
            if (blocksBroken >= configManager.getSettingsConfig().getDouble("BlockBP")) {
                gemsGiven = configManager.getSettingsConfig().getInt("GemsRichieste");
                player.sendMessage("Hai rotto 100 blocchi ed hai ottenuto " + gemsGiven + " gemme");
                // Aggiunge le gemme al giocatore
                economyManager.addBalance(player, gemsGiven);
                // Salva i dati del giocatore
                configManager.savePlayerData(economyManager.getPlayerBalances());
                // Rimuove la BossBar dal giocatore
                playerBossBar.removePlayer(player);
                bossBarMap.remove(playerUUID);

                // Azzera il contatore dei blocchi rotti per questo giocatore
                blocksBrokenMap.put(playerUUID, 0d);
            } else {
                blocksBrokenMap.put(playerUUID, blocksBroken);
            }
        }
    }

    private BossBar getPlayerBossBar(Player player) {
        return bossBarMap.computeIfAbsent(player.getUniqueId(), uuid ->
                Bukkit.createBossBar("Progresso", BarColor.BLUE, BarStyle.SEGMENTED_10));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Material blockType = event.getBlock().getType();
        if (!configManager.isBlacklisted(blockType)) {
            double blocksPlaced = blocksPlacedMap.getOrDefault(playerUUID, 0d) + 1;
            player.sendMessage("Hai piazzato un blocco di " + blockType.toString());
            // Aggiorna la BossBar con il progresso dei blocchi piazzati
            BossBar playerBossBar = getPlayerBossBar(player);
            playerBossBar.setProgress(blocksPlaced / configManager.getSettingsConfig().getDouble("BlockBP"));
            // Aggiungi la BossBar al giocatore, se necessario
            if (!bossBarMap.containsKey(playerUUID)) {
                bossBarMap.put(playerUUID, playerBossBar);
                playerBossBar.addPlayer(player);
            }
            // Se il giocatore ha rotto abbastanza blocchi, dargli le gemme e azzerare il contatore
            if (blocksPlaced >= configManager.getSettingsConfig().getDouble("BlockBP")) {
                int gemsGiven = configManager.getSettingsConfig().getInt("GemsRichieste");
                player.sendMessage("Hai piazzato 100 blocchi ed hai ottenuto " + gemsGiven + " gemme");
                // Aggiunge le gemme al giocatore
                economyManager.addBalance(player, gemsGiven);
                // Salva i dati del giocatore
                configManager.savePlayerData(economyManager.getPlayerBalances());
                // Rimuove la BossBar dal giocatore
                playerBossBar.removePlayer(player);
                bossBarMap.remove(playerUUID);

                // Azzera il contatore dei blocchi rotti per questo giocatore
                blocksPlacedMap.put(playerUUID, 0d);
            } else {
                blocksPlacedMap.put(playerUUID, blocksPlaced);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        // Rimuove la BossBar dal giocatore, se necessario
        if (bossBarMap.containsKey(playerUUID)) {
            getPlayerBossBar(player).removeAll();
            bossBarMap.remove(playerUUID);
            blocksBrokenMap.put(playerUUID, 0d);
            blocksPlacedMap.put(playerUUID, 0d);
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        // Rimuove le BossBar da tutti i giocatori e le cancella
        if (event.getPlugin().equals(plugin)) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID playerUUID = player.getUniqueId();
                blocksBrokenMap.put(playerUUID, 0d);
                blocksPlacedMap.put(playerUUID, 0d);
                BossBar playerBossBar = bossBarMap.get(playerUUID);
                if (playerBossBar != null) {
                    playerBossBar.removePlayer(player);
                    bossBarMap.remove(playerUUID);
                }
            }
        }
    }


}
