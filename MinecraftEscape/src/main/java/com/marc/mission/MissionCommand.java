package com.marc.mission;

import com.marc.config.ConfigManager;
import com.marc.economy.EconomyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissionCommand implements CommandExecutor {

    private final MissionManager missionManager;

    public MissionCommand(MissionManager missionManager, EconomyManager economyManager, ConfigManager configManager) {
        this.missionManager = missionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            missionManager.getMissionUI().openMissionMenu(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("archive")) {
            missionManager.getMissionUI().openArchiveMenu(player);
            return true;
        }

        sender.sendMessage("Invalid command usage.");
        return false;
    }
}
