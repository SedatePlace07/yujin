package org.sedateplace.yujinnetwork;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommandExecutor implements CommandExecutor {

    private final EconomyManager economyManager;
    MessageUtils messageUtils = new MessageUtils();
    String solo_giocatori = messageUtils.solo_giocatori();

    public MoneyCommandExecutor(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(solo_giocatori);
            return true;
        }

        Player player = (Player) sender;
        double balance = economyManager.getBalance(player);
        String bilancio = messageUtils.bilancio(String.valueOf(balance));
        if (player.hasPermission("yujin.balance"))
        player.sendMessage(bilancio);
        return true;
    }
}