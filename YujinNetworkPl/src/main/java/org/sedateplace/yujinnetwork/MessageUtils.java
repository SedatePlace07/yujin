package org.sedateplace.yujinnetwork;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageUtils extends JavaPlugin {
    public String no_perms() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("no_perms"));
    }
    public String giocatore_non_trovato() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("giocatore_non_trovato"));
    }
    public String non_abbastanza_argomenti() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("non_abbastanza_argomenti"));
    }
    public String valore_non_valido() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("valore_non_valido"));
    }
    public String eco_command() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("eco_command"));
    }
    public String solo_giocatori() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("solo_giocatori"));
    }
    public String modalita_non_valida() {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("modalita_non_valida"));
    }
    public String imposta_modalita(Player target, GameMode gameMode) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("imposta_modalita")
                .replace("%target%", target.getName())
                .replace("%gamemode%", gameMode.toString().toLowerCase()));
    }
    public String bilancio (String balance){
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString("bilancio")).replace("%soldi%", balance);
    }
}
