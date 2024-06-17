package com.marc.Mission;

import org.bukkit.inventory.ItemStack;

public class Mission {
    private String name;
    private String description;
    private String areaName;
    private ItemStack reward;

    public Mission(String name, String description, String areaName, ItemStack reward) {
        this.name = name;
        this.description = description;
        this.areaName = areaName;
        this.reward = reward;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public ItemStack getReward() {
        return reward;
    }

    public void setReward(ItemStack reward) {
        this.reward = reward;
    }
}
