package com.marc.mission;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Mission {
    public enum ActionType { COLLECT, KILL, SURVIVE }
    public enum RewardType { MONEY, ITEM }

    private String name;
    private String description;
    private ActionType action;
    private Material target;
    private int amount;
    private RewardType rewardType;
    private double rewardMoney;
    private ItemStack rewardItem;
    private Material icon;

    public Mission(String name, String description, ActionType action, Material target, int amount, RewardType rewardType, double rewardMoney, ItemStack rewardItem, Material icon) {
        this.name = name;
        this.description = description;
        this.action = action;
        this.target = target;
        this.amount = amount;
        this.rewardType = rewardType;
        this.rewardMoney = rewardMoney;
        this.rewardItem = rewardItem;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ActionType getAction() { return action; }
    public Material getTarget() { return target; }
    public int getAmount() { return amount; }
    public RewardType getRewardType() { return rewardType; }
    public double getRewardMoney() { return rewardMoney; }
    public ItemStack getRewardItem() { return rewardItem; }
    public Material getIcon() { return icon; }
}
