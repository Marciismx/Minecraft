package com.marc.rewards;

import org.bukkit.inventory.ItemStack;
import java.util.List;

public class Reward {
    private List<ItemStack> items;
    private int experience;

    public Reward(List<ItemStack> items, int experience) {
        this.items = items;
        this.experience = experience;

    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
