package com.marc.Mission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class MissionCommand implements CommandExecutor {
    private MissionManager missionManager;

    public MissionCommand(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 6) {
            sender.sendMessage("Usage: /mission add <name> <areaName> <description> <rewardItems> <experience>");
            return false;
        }
        
        if (args[0].equalsIgnoreCase("add")) {
            String name = args[1];
            String areaName = args[2];
            String description = String.join(" ", Arrays.asList(args).subList(3, args.length - 2));
            String[] rewardMaterials = args[args.length - 2].split(",");
            
            int experience;
            try {
                experience = Integer.parseInt(args[args.length - 1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Experience must be a valid number.");
                return false;
            }
            
            List<ItemStack> rewards = new ArrayList<>();
            for (String rewardMaterial : rewardMaterials) {
                Material material = Material.getMaterial(rewardMaterial.toUpperCase());
                if (material != null) {
                    rewards.add(new ItemStack(material, 1));
                } else {
                    sender.sendMessage("Invalid material: " + rewardMaterial);
                    return false;
                }
            }
            
            Mission mission = new Mission(name, description, areaName, rewards, experience);
            missionManager.addMission(mission);
            sender.sendMessage("Mission added successfully: " + name);
            return true;
        }

        sender.sendMessage("Invalid subcommand. Usage: /mission add <name> <areaName> <description> <rewardItems> <experience>");
        return false;
    }
}
