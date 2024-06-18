package com.marc.Mission;

import com.marc.config.ConfigManager;
import com.marc.progress.PlayerProgressManager;
import com.marc.rewards.Reward;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MissionManager {
    private List<Mission> missions;
    private List<UUID> completedMissions;
    private PlayerProgressManager playerProgressManager;
    private ConfigManager configManager;

    public MissionManager(ConfigManager configManager, PlayerProgressManager playerProgressManager) {
        this.missions = new ArrayList<>();
        this.completedMissions = new ArrayList<>();
        this.configManager = configManager;
        this.playerProgressManager = playerProgressManager;
        loadMissions();
    }

    public void addMission(Mission mission) {
        missions.add(mission);
        saveMissions();
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public Mission getMissionByName(String name) {
        for (Mission mission : missions) {
            if (mission.getName().equals(name)) {
                return mission;
            }
        }
        return null;
    }

    public void completeMission(Mission mission, Player player) {
        completedMissions.add(player.getUniqueId());
        missions.remove(mission);
        playerProgressManager.setPlayerProgress(player.getUniqueId(), 0);  // Clear progress on completion
        saveMissions();
    }

    public void startMission(Mission mission, Player player) {
        playerProgressManager.setPlayerProgress(player.getUniqueId(), mission.getExperience());
    }

    private void loadMissions() {
        FileConfiguration config = configManager.getMissionsConfig();
        for (String key : config.getKeys(false)) {
            String name = config.getString(key + ".name");
            String description = config.getString(key + ".description");
            String areaName = config.getString(key + ".areaName");
            String rewardItemsString = config.getString(key + ".reward.items");
            int experience = config.getInt(key + ".reward.experience");

            if (name == null || description == null || areaName == null || rewardItemsString == null) {
                System.out.println("Missing configuration for mission: " + key);
                continue;
            }

            String[] rewardMaterials = rewardItemsString.split(",");
            List<ItemStack> rewards = new ArrayList<>();
            for (String rewardMaterial : rewardMaterials) {
                Material material = Material.getMaterial(rewardMaterial.toUpperCase());
                if (material != null) {
                    rewards.add(new ItemStack(material, 1));
                }
            }
            Reward reward = new Reward(rewards, experience);
            missions.add(new Mission(name, description, areaName, rewards, experience));
        }
    }

    private void saveMissions() {
        FileConfiguration config = configManager.getMissionsConfig();
        config.set("missions", null);
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            String key = "mission" + i;
            config.set(key + ".name", mission.getName());
            config.set(key + ".description", mission.getDescription());
            config.set(key + ".areaName", mission.getAreaName());
            StringBuilder rewards = new StringBuilder();
            for (ItemStack reward : mission.getRewards()) {
                rewards.append(reward.getType().name()).append(",");
            }
            config.set(key + ".reward.items", rewards.toString());
            config.set(key + ".reward.experience", mission.getExperience());
        }
        configManager.saveMissionsConfig();
    }
}
