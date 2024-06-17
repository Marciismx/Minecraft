package com.marc.rewards;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RewardManager {

    public void giveReward(Player player, ItemStack reward) {
        player.getInventory().addItem(reward);
        player.sendMessage("You have received a reward!");
    }
}
