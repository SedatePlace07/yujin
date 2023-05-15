package org.sedateplace.yujinnetwork;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Comandi implements CommandExecutor{
    MessageUtils messageUtils = new MessageUtils();
    String no_perms = messageUtils.no_perms();
    String solo_giocatori = messageUtils.solo_giocatori();
    String modalita_non_valida = messageUtils.modalita_non_valida();
    String giocatore_non_trovato = messageUtils.giocatore_non_trovato();
    String non_abbastanza_argomenti = messageUtils.non_abbastanza_argomenti();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("gamemode")) {
            if (args.length == 0) {
                sender.sendMessage(non_abbastanza_argomenti);
                return true;
            }

            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(solo_giocatori);
                    return true;
                }
                GameMode gameMode = getGameMode(args[0]);

                if (gameMode == null) {
                    sender.sendMessage(modalita_non_valida);
                    return true;
                }
                Player player = (Player) sender;
                if (!player.hasPermission("yujin.gamemode." + gameMode.toString().toLowerCase())) {
                    player.sendMessage(no_perms);
                    return true;
                }

                player.setGameMode(gameMode);
                player.sendMessage(ChatColor.GREEN + "Modalità di gioco impostata su " + gameMode.toString().toLowerCase());
                return true;
            }

            if (args.length == 2) {
                GameMode gameMode = getGameMode(args[0]);

                if (gameMode == null) {
                    sender.sendMessage(modalita_non_valida);
                    return true;
                }
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    if (!player.hasPermission("yujin.gamemode.set." + gameMode.toString().toLowerCase())) {
                        player.sendMessage(no_perms);
                        return true;
                    }
                }
                Player target = Bukkit.getPlayer(args[1]);
                String imposta_modalita = messageUtils.imposta_modalita(target, gameMode);
                if (target == null) {
                    sender.sendMessage(giocatore_non_trovato);
                    return true;
                }
                target.setGameMode(gameMode);
                if (player != null) {
                    player.sendMessage(imposta_modalita);
                } else {
                    Bukkit.getLogger().info(imposta_modalita);
                }

                target.sendMessage(ChatColor.GREEN + "La tua modalità di gioco è stata impostata su " + gameMode.toString().toLowerCase());
                return true;
            }

            return false;
        }
        return false;
    }


    private GameMode getGameMode(String arg) {
        if (arg.equalsIgnoreCase("survival") || arg.equals("0") || arg.equals("s")) {
            return GameMode.SURVIVAL;
        } else if (arg.equalsIgnoreCase("creative") || arg.equals("1") || arg.equals("c")) {
            return GameMode.CREATIVE;
        } else if (arg.equalsIgnoreCase("adventure") || arg.equals("2") || arg.equals("a")) {
            return GameMode.ADVENTURE;
        } else if (arg.equalsIgnoreCase("spectator") || arg.equals("3") || arg.equals("sp")) {
            return GameMode.SPECTATOR;
        } else {
            return null;
        }
    }
}
