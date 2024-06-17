package com.marc.Mission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.Arrays;

public class MissionCommand implements CommandExecutor {
    private MissionManager missionManager;

    public MissionCommand(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (args.length > 3 && args[0].equalsIgnoreCase("add")) {
                String name = args[1];
                String areaName = args[2];
                String description = String.join(" ", Arrays.asList(args).subList(3, args.length - 1));
                Material rewardMaterial = Material.getMaterial(args[args.length - 1].toUpperCase());
                if (rewardMaterial == null) {
                    sender.sendMessage("Invalid reward material: " + args[args.length - 1]);
                    return false;
                }
                ItemStack reward = new ItemStack(rewardMaterial, 1);
                Mission mission = new Mission(name, description, areaName, reward);
                missionManager.addMission(mission);
                sender.sendMessage("Mission added: " + name);
                return true;
            }
        } catch (Exception e) {
            sender.sendMessage("An error occurred while executing the command. Check the server logs for more details.");
            e.printStackTrace();
        }
        return false;
    }
}
