package com.marc.Mission;

import com.marc.rewards.Reward;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Mission {
    private String name;
    private String description;
    private String areaName;
    private List<ItemStack> rewards;
    private int experience;
    private Reward reward;

    public Mission(String name, String description, String areaName, List<ItemStack> rewards, int experience) {
        this.name = name;
        this.description = description;
        this.areaName = areaName;
        this.rewards = rewards;
        this.experience = experience;
        this.reward = new Reward(rewards, experience);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAreaName() {
        return areaName;
    }

    public List<ItemStack> getRewards() {
        return rewards;
    }

    public int getExperience() {
        return experience;
    }

    public Reward getReward() {
        return reward;
    }
}
