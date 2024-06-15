package com.marc.mission;

import com.marc.config.ConfigManager;
import com.marc.economy.EconomyManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MissionCommand implements CommandExecutor {
    private MissionManager missionManager;
    private EconomyManager economyManager;
    private ConfigManager configManager;
    private MissionUI missionUI;

    public MissionCommand(MissionManager missionManager, EconomyManager economyManager, ConfigManager configManager, MissionUI missionUI) {
        this.missionManager = missionManager;
        this.economyManager = economyManager;
        this.configManager = configManager;
        this.missionUI = missionUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                missionUI.openMissionMenu(player);
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("complete")) {
                missionManager.completeMission(player, economyManager);
                return true;
            }
            if (args.length == 2 && args[0].equalsIgnoreCase("assign")) {
                String missionType = args[1].toLowerCase();
                Mission mission = createMissionFromConfig(missionType);
                if (mission != null) {
                    missionManager.assignMission(player, mission);
                    player.sendMessage("Mission assigned: " + mission.getName());
                } else {
                    player.sendMessage("Invalid mission type.");
                }
                return true;
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return false;
    }

    private Mission createMissionFromConfig(String missionType) {
        String path = "missions." + missionType + ".";
        String name = configManager.getConfig().getString(path + "name");
        String description = configManager.getConfig().getString(path + "description");
        double rewardMoney = configManager.getConfig().getDouble(path + "rewardMoney");
        String rewardItemStr = configManager.getConfig().getString(path + "rewardItem");
        int rewardItemAmount = configManager.getConfig().getInt(path + "rewardItemAmount");
        int targetAmount = configManager.getConfig().getInt(path + "targetAmount");

        if (name != null && description != null && rewardItemStr != null) {
            Material rewardItemMaterial = Material.getMaterial(rewardItemStr.toUpperCase());
            ItemStack rewardItem = new ItemStack(rewardItemMaterial, rewardItemAmount);
            return new Mission(name, description, rewardMoney, rewardItem, Mission.MissionType.valueOf(missionType.toUpperCase()), targetAmount);
        }
        return null;
    }
}
