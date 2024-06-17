package com.marc.mission;

import com.marc.economy.EconomyManager;
import com.marc.storage.MissionStorage;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MissionManager implements Listener {
    private MissionStorage missionStorage;
    private Map<UUID, Mission> activeMissions = new HashMap<>();
    private Map<UUID, Map<String, Boolean>> completedMissions = new HashMap<>();
    private Map<UUID, Integer> zombieKillCount = new HashMap<>();

    public MissionManager(MissionStorage missionStorage) {
        this.missionStorage = missionStorage;
    }

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
            Map<String, Boolean> progress = completedMissions.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
            progress.put(mission.getName(), true);
            missionStorage.saveMissionProgress(player.getUniqueId(), progress);
            player.sendMessage("Mission " + mission.getName() + " completed! Reward: " + mission.getRewardMoney() + " money and " + mission.getRewardItem().getAmount() + " " + mission.getRewardItem().getType().toString());
        } else {
            player.sendMessage("You have no active missions.");
        }
    }

    public boolean hasCompletedMission(Player player, String missionName) {
        Map<String, Boolean> progress = completedMissions.computeIfAbsent(player.getUniqueId(), k -> missionStorage.loadMissionProgress(player.getUniqueId()));
        return progress.getOrDefault(missionName, false);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            if (event.getEntityType() == EntityType.ZOMBIE) {
                int count = zombieKillCount.getOrDefault(player.getUniqueId(), 0) + 1;
                zombieKillCount.put(player.getUniqueId(), count);

                Mission activeMission = getActiveMission(player);
                if (activeMission != null && activeMission.getName().equals("Defeat Zombies")) {
                    if (count >= activeMission.getTargetAmount()) {
                        completeMission(player, new EconomyManager());  // Assuming you have an instance of EconomyManager
                        zombieKillCount.remove(player.getUniqueId());
                    }
                }
            }
        }
    }
}
