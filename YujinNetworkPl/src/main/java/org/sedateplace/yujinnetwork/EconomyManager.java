package org.sedateplace.yujinnetwork;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class EconomyManager {

    private final HashMap<UUID, Double> playerBalances;
    private final ConfigManager configManager;

    public EconomyManager(ConfigManager configManager) {
        this.configManager = configManager;
        playerBalances = new HashMap<>();
        FileConfiguration playerDataConfig = configManager.getPlayerDataConfig();

        for (String key : playerDataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            double balance = playerDataConfig.getDouble(key);
            playerBalances.put(uuid, balance);
        }
    }

    public double getBalance(Player player) {
        double balance = playerBalances.getOrDefault(player.getUniqueId(), 0.0);
        Bukkit.getLogger().info("[YujinNetwork] Bilancio di " + player.getName() + ": " + balance);
        return balance;
    }

    public void setBalance(Player player, double amount) {
        playerBalances.put(player.getUniqueId(), amount);
    }

    public void addBalance(Player player, double amount) {
        double currentBalance = getBalance(player);
        double newBalance = currentBalance + amount;
        setBalance(player, currentBalance + amount);
        Bukkit.getLogger().info("[YujinNetwork] Aggiunte " + amount + " gemme a " + player.getName() + ". Nuovo bilancio: " + newBalance);
    }

    public void subtractBalance(Player player, double amount) {
        double currentBalance = getBalance(player);
        setBalance(player, currentBalance - amount);
    }

    public void resetBalance(Player player) {
        double defaultMoney = configManager.getSettingsConfig().getDouble("DefaultMoney");
        setBalance(player, defaultMoney);
    }

    public HashMap<UUID, Double> getPlayerBalances() {
        return playerBalances;
    }
}