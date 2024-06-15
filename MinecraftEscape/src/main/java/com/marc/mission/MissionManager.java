package com.marc.mission;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MissionManager {
    private Map<UUID, Mission> activeMissions = new HashMap<>();

    public void assignMission(Player player, Mission mission) {
        activeMissions.put(player.getUniqueId(), mission);
    }

    public Mission getActiveMission(Player player) {
        return activeMissions.get(player.getUniqueId());
    }

    public void completeMission(Player player, com.marc.economy.EconomyManager economyManager) {
        Mission mission = activeMissions.remove(player.getUniqueId());
        if (mission != null) {
            economyManager.addBalance(player, mission.getReward());
            player.sendMessage("Mission " + mission.getName() + " completed! Reward: " + mission.getReward());
        } else {
            player.sendMessage("You have no active missions.");
        }
    }
}
