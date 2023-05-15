package org.sedateplace.yujinnetwork;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration settingsConfig;
    private FileConfiguration playerDataConfig;
    private FileConfiguration blacklistConfig;
    private FileConfiguration messagesConfig;
    private File playerDataFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        createSettingsConfig();
        createPlayerDataConfig();
        createBlackListConfig();
        createMessagesConfig();
    }
    private void createSettingsConfig() {
        settingsConfig = plugin.getConfig();
        settingsConfig.addDefault("DefaultMoney", 1000);
        settingsConfig.addDefault("BlockBP", 100);
        settingsConfig.addDefault("GemsRichieste", 10);
        settingsConfig.options().copyDefaults(true);
        plugin.saveConfig();
    }
    private void createPlayerDataConfig() {
        playerDataFile = new File(plugin.getDataFolder(), "playerData.yml");
        if (!playerDataFile.exists()) {
            try {
                getLogger().info("Creazione del file playerData.yml...");
                playerDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void createBlackListConfig() {
        File blacklistFile = new File(plugin.getDataFolder(), "blacklist.yml");
        if (!blacklistFile.exists()) {
            getLogger().info("Creazione del file blacklist.yml...");
            plugin.saveResource("blacklist.yml", false);
        }
        blacklistConfig = YamlConfiguration.loadConfiguration(blacklistFile);
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        List<String> blockedBlockNames = blacklistConfig.getStringList("blockedBlocks");
    }

    public void saveBlackListData() {
        File blacklistFile = new File(plugin.getDataFolder(), "blacklist.yml");
        try {
            blacklistConfig.save(blacklistFile);
        } catch (IOException e) {
            getLogger().severe("Errore durante il salvataggio del file blacklist.yml!");
            e.printStackTrace();
        }
    }
    public boolean isBlacklisted(Material material) {
        List<String> blockedBlockNames = blacklistConfig.getStringList("blockedBlocks");
        for (String blockedBlockName : blockedBlockNames) {
            Material blockedBlockType = Material.getMaterial(blockedBlockName);
            if (material == blockedBlockType) {
                return true;
            }
        }
        return false;
    }

    public void savePlayerData(HashMap<UUID, Double> playerBalances) {
        playerBalances.forEach((uuid, balance) -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                String playerName = player.getName();
                playerDataConfig.set(uuid.toString() + ".name", playerName);
            }
            playerDataConfig.set(uuid.toString() + ".balance", balance);
        });
        try {
            playerDataConfig.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createMessagesConfig() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            getLogger().info("Creazione del file messages.yml...");
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        messagesConfig.addDefault("valore_non_valido", "&cValore non valido!");
        messagesConfig.addDefault("no_perms", "&cNon hai il permesso di fare questo comando");
        messagesConfig.addDefault("non_abbastanza_argomenti", "&cNon abbastanza argomenti");
        messagesConfig.addDefault("giocatore_non_trovato", "&cGiocatore non trovato");
        messagesConfig.addDefault("eco_command", "&fUso: /eco <give|remove|set|reset> <player> <amount>");
        messagesConfig.addDefault("solo_giocatori", "&cSolo i giocatori possono usare questo comando!");
        messagesConfig.addDefault("modalita_non_valida", "&cModalità di gioco non valida");
        messagesConfig.addDefault("imposta_modalita", "&fModalità di gioco di %target% impostata su %modalita%");
        messagesConfig.addDefault("bilancio", "&aBilancio: &f%soldi%");
        messagesConfig.options().copyDefaults(true);

    }

    public void saveMessagesData() {
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            getLogger().severe("Errore durante il salvataggio del file messages.yml!");
            e.printStackTrace();
        }

    }

    public FileConfiguration getSettingsConfig() {
        return settingsConfig;
    }

    public FileConfiguration getPlayerDataConfig() {
        return playerDataConfig;
    }
}