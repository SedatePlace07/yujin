package org.sedateplace.yujinnetwork;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;


    public ReloadCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("yujin.reload")) {
                plugin.getLogger().info("Ricaricando " + plugin.getName());
                plugin.onDisable();
                plugin.onEnable();
                sender.sendMessage(ChatColor.GREEN + "Plugin " + plugin.getName() + " ricaricato");
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Non hai il permesso per usare questo comando");
                return true;
            }
        }
        return false;
    }
}
