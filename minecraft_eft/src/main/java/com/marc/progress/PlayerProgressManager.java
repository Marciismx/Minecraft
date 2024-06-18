package com.marc.progress;

import com.marc.config.ConfigManager;
import com.marc.rewards.Reward;
import com.marc.rewards.RewardManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerProgressManager {
    private HashMap<UUID, Integer> playerProgress;
    private ConfigManager configManager;
    private RewardManager rewardManager;

    public PlayerProgressManager(ConfigManager configManager, RewardManager rewardManager) {
        this.configManager = configManager;
        this.rewardManager = rewardManager;
        this.playerProgress = new HashMap<>();
        loadProgress();
    }

    public void setPlayerProgress(UUID playerId, int experience) {
        playerProgress.put(playerId, experience);
        saveProgress();
    }

    public int getPlayerProgress(UUID playerId) {
        return playerProgress.getOrDefault(playerId, 0);
    }

    private void loadProgress() {
        FileConfiguration config = configManager.getRewardsConfig();
        for (String key : config.getKeys(false)) {
            try {
                UUID playerId = UUID.fromString(key);
                int experience = config.getInt(key);
                playerProgress.put(playerId, experience);
            } catch (IllegalArgumentException e) {
                // Log the invalid UUID and continue
                System.out.println("Invalid UUID string: " + key);
            }
        }
    }

    private void saveProgress() {
        FileConfiguration config = configManager.getRewardsConfig();
        for (UUID playerId : playerProgress.keySet()) {
            int experience = playerProgress.get(playerId);
            config.set(playerId.toString(), experience);
        }
        configManager.saveRewardsConfig();
    }

    public void checkAndRewardPlayer(Player player) {
        int experience = getPlayerProgress(player.getUniqueId());
        for (Reward reward : rewardManager.getRewards()) {
            if (experience >= reward.getExperience()) {
                rewardManager.giveReward(player, reward);
                setPlayerProgress(player.getUniqueId(), experience - reward.getExperience());
            }
        }
    }
}
