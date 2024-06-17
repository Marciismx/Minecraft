package com.marc.mission;

import com.marc.config.ConfigManager;
import com.marc.economy.EconomyManager;
import com.marc.mission.MissionUI; // Import statement for MissionUI
import com.marc.storage.MissionStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MissionManager implements Listener {
    private ConfigManager configManager;
    private MissionUI missionUI;
    private Map<UUID, Mission> activeMissions = new HashMap<>();
    private Map<UUID, Map<String, Boolean>> completedMissions = new HashMap<>();
    private Map<UUID, Integer> itemCounts = new HashMap<>();
    private EconomyManager economyManager;
    private MissionStorage missionStorage;

    public MissionManager(MissionStorage missionStorage, ConfigManager configManager, MissionUI missionUI, EconomyManager economyManager) {
        this.missionStorage = missionStorage;
        this.configManager = configManager;
        this.missionUI = missionUI;
        this.economyManager = economyManager;
    }


    public void assignMission(Player player, Mission mission) {
        activeMissions.put(player.getUniqueId(), mission);
        itemCounts.put(player.getUniqueId(), 0); // Initialize item count for collection missions
        player.sendMessage("New mission assigned: " + mission.getName());
    }

    public Mission getActiveMission(Player player) {
        return activeMissions.get(player.getUniqueId());
    }

    public void completeMission(Player player, EconomyManager economyManager) {
        Mission mission = activeMissions.remove(player.getUniqueId());
        itemCounts.remove(player.getUniqueId()); // Reset item count when mission is completed
        if (mission != null) {
            if (mission.getRewardType() == Mission.RewardType.MONEY) {
                economyManager.addBalance(player, mission.getRewardMoney());
            } else if (mission.getRewardType() == Mission.RewardType.ITEM) {
                player.getInventory().addItem(mission.getRewardItem());
            }
            Map<String, Boolean> progress = completedMissions.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
            progress.put(mission.getName(), true);
            missionStorage.saveMissionProgress(player.getUniqueId(), progress);
            player.sendMessage("Mission " + mission.getName() + " completed! Reward: " + (mission.getRewardType() == Mission.RewardType.MONEY ? mission.getRewardMoney() + " money" : mission.getRewardItem().getAmount() + " " + mission.getRewardItem().getType().toString()));

            // Assign the next mission
            assignNextMission(player);
        } else {
            player.sendMessage("You have no active missions.");
        }
    }

    public void assignNextMission(Player player) {
        for (String key : configManager.getConfig().getConfigurationSection("missions").getKeys(false)) {
            String path = "missions." + key + ".";
            String name = configManager.getConfig().getString(path + "name");
            boolean dependenciesCompleted = canStartMission(player, name);
            if (dependenciesCompleted && !hasCompletedMission(player, name) && getActiveMission(player) == null) {
                Mission nextMission = createMissionFromConfig(key);
                assignMission(player, nextMission);
                return;
            }
        }
    }

    public boolean hasCompletedMission(Player player, String missionName) {
        Map<String, Boolean> progress = completedMissions.computeIfAbsent(player.getUniqueId(), k -> missionStorage.loadMissionProgress(player.getUniqueId()));
        return progress.getOrDefault(missionName, false);
    }

    public Map<String, Boolean> getCompletedMissions(Player player) {
        return completedMissions.computeIfAbsent(player.getUniqueId(), k -> missionStorage.loadMissionProgress(player.getUniqueId()));
    }

    public boolean canStartMission(Player player, String missionName) {
        List<String> dependencies = getDependencies(missionName);
        for (String dependency : dependencies) {
            if (!hasCompletedMission(player, dependency)) {
                return false;
            }
        }
        return true;
    }

    private List<String> getDependencies(String missionName) {
        for (String key : configManager.getConfig().getConfigurationSection("missions").getKeys(false)) {
            String name = configManager.getConfig().getString("missions." + key + ".name");
            if (name.equals(missionName)) {
                return configManager.getConfig().getStringList("missions." + key + ".dependencies");
            }
        }
        return null;
    }

    private Mission createMissionFromConfig(String missionType) {
        String path = "missions." + missionType + ".";
        String name = configManager.getConfig().getString(path + "name");
        String description = configManager.getConfig().getString(path + "description");
        String actionStr = configManager.getConfig().getString(path + "action");
        String targetStr = configManager.getConfig().getString(path + "target");
        int amount = configManager.getConfig().getInt(path + "amount");
        String rewardTypeStr = configManager.getConfig().getString(path + "reward.type");
        double rewardMoney = rewardTypeStr.equalsIgnoreCase("MONEY") ? configManager.getConfig().getDouble(path + "reward.amount") : 0;
        String rewardItemStr = rewardTypeStr.equalsIgnoreCase("ITEM") ? configManager.getConfig().getString(path + "reward.item") : null;
        int rewardItemAmount = rewardTypeStr.equalsIgnoreCase("ITEM") ? configManager.getConfig().getInt(path + "reward.amount") : 0;
        Material iconMaterial = Material.getMaterial(configManager.getConfig().getString(path + "icon").toUpperCase());
        Material target = targetStr.isEmpty() ? null : Material.getMaterial(targetStr.toUpperCase());

        if (name != null && description != null && actionStr != null && rewardTypeStr != null && iconMaterial != null) {
            Mission.ActionType action = Mission.ActionType.valueOf(actionStr.toUpperCase());
            Mission.RewardType rewardType = Mission.RewardType.valueOf(rewardTypeStr.toUpperCase());
            ItemStack rewardItem = rewardType == Mission.RewardType.ITEM ? new ItemStack(Material.valueOf(rewardItemStr.toUpperCase()), rewardItemAmount) : null;
            return new Mission(name, description, action, target, amount, rewardType, rewardMoney, rewardItem, iconMaterial);
        }
        return null;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player player = event.getEntity().getKiller();
            Mission activeMission = getActiveMission(player);
            if (activeMission != null && activeMission.getAction() == Mission.ActionType.KILL) {
                if (event.getEntityType().name().equalsIgnoreCase(activeMission.getTarget().name())) {
                    int count = itemCounts.getOrDefault(player.getUniqueId(), 0) + 1;
                    itemCounts.put(player.getUniqueId(), count);
                    if (count >= activeMission.getAmount()) {
                        completeMission(player, new EconomyManager());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Mission activeMission = getActiveMission(player);
        if (activeMission != null && activeMission.getAction() == Mission.ActionType.COLLECT) {
            if (block.getType() == activeMission.getTarget()) {
                int count = itemCounts.getOrDefault(player.getUniqueId(), 0) + 1; // consider changing 1 to the number of blocks broken
                itemCounts.put(player.getUniqueId(), count);
                if (count == activeMission.getAmount()) {
                    completeMission(player, economyManager);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Mission activeMission = getActiveMission(player);
            if (activeMission != null && activeMission.getAction() == Mission.ActionType.COLLECT) {
                if (event.getItem().getItemStack().getType() == activeMission.getTarget()) {
                    int count = itemCounts.getOrDefault(player.getUniqueId(), 0) + event.getItem().getItemStack().getAmount();
                    itemCounts.put(player.getUniqueId(), count);
                    if (count == activeMission.getAmount()) {
                        completeMission(player, economyManager);
                    }
                }
            }
        }
    }

    public MissionUI getMissionUI() {
        return missionUI;
    }

    public MissionStorage getMissionStorage() {
        return missionStorage;
    }

    public Map<UUID, Integer> getItemCounts() {
        return itemCounts;
    }
}
