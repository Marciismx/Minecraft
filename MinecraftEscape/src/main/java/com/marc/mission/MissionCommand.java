package com.marc.mission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MissionCommand implements CommandExecutor {
    private MissionManager missionManager;
    private com.marc.economy.EconomyManager economyManager;

    public MissionCommand(MissionManager missionManager, com.marc.economy.EconomyManager economyManager) {
        this.missionManager = missionManager;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                Mission activeMission = missionManager.getActiveMission(player);
                if (activeMission != null) {
                    player.sendMessage("Active Mission: " + activeMission.getName() + " - " + activeMission.getDescription());
                } else {
                    player.sendMessage("You have no active missions.");
                }
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("complete")) {
                missionManager.completeMission(player, economyManager);
                return true;
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return false;
    }
}
