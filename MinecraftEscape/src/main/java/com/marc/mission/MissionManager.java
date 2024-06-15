package com.marc.mission;

import org.bukkit.entity.Player;
import com.marc.economy.EconomyManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MissionManager {
    private Map<UUID, Mission> activeMissions = new HashMap<>();
    private Map<UUID, Map<String, Boolean>> completedMissions = new HashMap<>();

    public MissionManager() {}

    public void assignMission(Player player, Mission mission) {
        activeMissions.put(player.getUniqueId(), mission);
    }

    public Mission getActiveMission(Player player) {
        return activeMissions.get(player.getUniqueId());
    }

    public void completeMission(Player player, EconomyManager economyManager) {
        Mission mission = activeMissions.remove(player.getUniqueId());
        if (mission != null) {
            economyManager.addBalance(player, mission.getRewardMoney());
            if (mission.getRewardItem() != null) {
                player.getInventory().addItem(mission.getRewardItem());
            }
            completedMissions.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>()).put(mission.getName(), true);
            player.sendMessage("Mission " + mission.getName() + " completed! Reward: " + mission.getRewardMoney() + " money and " + mission.getRewardItem().getAmount() + " " + mission.getRewardItem().getType().toString());
        } else {
            player.sendMessage("You have no active missions.");
        }
    }

    public boolean hasCompletedMission(Player player, String missionName) {
        return completedMissions.getOrDefault(player.getUniqueId(), new HashMap<>()).getOrDefault(missionName, false);
    }
}
