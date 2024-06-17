package com.marc.Mission;

import com.marc.config.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MissionManager {
    private List<Mission> missions;
    private List<UUID> completedMissions;
    private ConfigManager configManager;

    public MissionManager(ConfigManager configManager) {
        this.missions = new ArrayList<>();
        this.completedMissions = new ArrayList<>();
        this.configManager = configManager;
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
        saveMissions();
    }

    private void loadMissions() {
        FileConfiguration config = configManager.getMissionsConfig();
        for (String key : config.getKeys(false)) {
            String name = config.getString(key + ".name");
            String description = config.getString(key + ".description");
            String areaName = config.getString(key + ".areaName");
            String rewardMaterial = config.getString(key + ".reward");
            ItemStack reward = new ItemStack(Material.getMaterial(rewardMaterial.toUpperCase()));
            missions.add(new Mission(name, description, areaName, reward));
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
            config.set(key + ".reward", mission.getReward().getType().name());
        }
        configManager.saveConfigs();
    }
}
