package com.marc.commands;

import com.marc.economy.EconomyManager;
import com.marc.mission.MissionManager;
import com.marc.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EscapeCommand implements CommandExecutor {

    private final MissionManager missionManager;
    private final EconomyManager economyManager;

    public EscapeCommand(MissionManager missionManager, EconomyManager economyManager, ConfigManager configManager) {
        this.missionManager = missionManager;
        this.economyManager = economyManager;
       
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        
        if (args.length == 0) {
            player.sendMessage("Escape commands: /escape status, /escape help");
            return true;
        }

        if (args[0].equalsIgnoreCase("status")) {
            showStatus(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            showHelp(player);
            return true;
        }

        sender.sendMessage("Invalid command usage. Use /escape help for a list of commands.");
        return false;
    }

    private void showStatus(Player player) {
        player.sendMessage("Your current mission: " + (missionManager.getActiveMission(player) != null ? missionManager.getActiveMission(player).getName() : "No active mission"));
        player.sendMessage("Your balance: " + economyManager.getBalance(player));
    }

    private void showHelp(Player player) {
        player.sendMessage("Escape Commands:");
        player.sendMessage("/escape status - Shows your current status.");
        player.sendMessage("/escape help - Shows this help message.");
    }
}
