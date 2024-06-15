package com.marc.mission;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Mission {
    public enum MissionType {
        COLLECT, PVP, SURVIVAL
    }

    private String name;
    private String description;
    private double rewardMoney;
    private ItemStack rewardItem;
    private MissionType type;
    private int targetAmount;

    public Mission(String name, String description, double rewardMoney, ItemStack rewardItem, MissionType type, int targetAmount) {
        this.name = name;
        this.description = description;
        this.rewardMoney = rewardMoney;
        this.rewardItem = rewardItem;
        this.type = type;
        this.targetAmount = targetAmount;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getRewardMoney() {
        return rewardMoney;
    }

    public ItemStack getRewardItem() {
        return rewardItem;
    }

    public MissionType getType() {
        return type;
    }

    public int getTargetAmount() {
        return targetAmount;
    }
}
