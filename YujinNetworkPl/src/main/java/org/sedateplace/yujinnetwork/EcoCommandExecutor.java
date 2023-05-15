package org.sedateplace.yujinnetwork;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EcoCommandExecutor implements CommandExecutor {
    private final EconomyManager economyManager;
    private final ConfigManager configManager;
    MessageUtils messageUtils = new MessageUtils();
    String giocatore_non_trovato = messageUtils.giocatore_non_trovato();
    String valore_non_valido = messageUtils.valore_non_valido();
    String eco_command = messageUtils.eco_command();
    String no_perms = messageUtils.no_perms();


    public EcoCommandExecutor(EconomyManager economyManager, ConfigManager configManager) {
        this.economyManager = economyManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String action = args[0];
        String playerName = args[1];
        Player target = Bukkit.getPlayer(playerName);

        if (args.length < 3) {
            sender.sendMessage(eco_command);
            return true;
        }

        if (target == null) {
            sender.sendMessage(giocatore_non_trovato);
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(valore_non_valido);
            return true;
        }

        switch (action.toLowerCase()) {
            case "give":
                if (!sender.hasPermission("yujin.eco.give")) {
                    sender.sendMessage(no_perms);
                    return true;
                }
                economyManager.addBalance(target, amount);
                sender.sendMessage(ChatColor.GREEN + "Aggiunte " + amount + " gemme a " + target.getName());
                configManager.savePlayerData(economyManager.getPlayerBalances());
                break;
            case "remove":
                if (!sender.hasPermission("yujin.eco.remove")) {
                    sender.sendMessage(no_perms);
                    configManager.savePlayerData(economyManager.getPlayerBalances());
                    return true;
                }
                economyManager.subtractBalance(target, amount);
                sender.sendMessage(ChatColor.GREEN + "Rimosse " + amount + " gemme a" + target.getName());
                configManager.savePlayerData(economyManager.getPlayerBalances());
                break;
            case "set":
                if (!sender.hasPermission("yujin.eco.set")) {
                    sender.sendMessage(no_perms);
                    return true;
                }
                economyManager.setBalance(target, amount);
                sender.sendMessage(ChatColor.GREEN + "Impostate " + amount + " gemme a " + target.getName());
                configManager.savePlayerData(economyManager.getPlayerBalances());
                break;
            case "reset":
                if (!sender.hasPermission("yujin.eco.reset")) {
                    sender.sendMessage(no_perms);
                    return true;
                }
                economyManager.resetBalance(target);
                sender.sendMessage(ChatColor.GREEN + "Resettate le gemme di " + target.getName());
                configManager.savePlayerData(economyManager.getPlayerBalances());
                break;
            default:
                sender.sendMessage(eco_command);
        }
        return true;
    }
}
