package com.marc.rewards;

import com.marc.config.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class RewardManager {
    private ConfigManager configManager;
    private List<Reward> rewards;

    public RewardManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.rewards = new ArrayList<>();
        loadRewards();
    }

    public void giveReward(Player player, Reward reward) {
        for (ItemStack item : reward.getItems()) {
            player.getInventory().addItem(item);
        }
        player.giveExp(reward.getExperience());
        player.sendMessage("You have received your rewards!");
    }

    private void loadRewards() {
        FileConfiguration config = configManager.getRewardsConfig();
        for (String key : config.getKeys(false)) {
            String[] rewardMaterials = config.getString(key + ".items").split(",");
            int experience = config.getInt(key + ".experience");
            List<ItemStack> items = new ArrayList<>();
            for (String rewardMaterial : rewardMaterials) {
                Material material = Material.getMaterial(rewardMaterial.toUpperCase());
                if (material != null) {
                    items.add(new ItemStack(material, 1));
                }
            }
            rewards.add(new Reward(items, experience));
        }
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
