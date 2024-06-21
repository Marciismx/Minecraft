package com.marc.tarkovescape.loot;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LootTable {
    private final Map<ItemStack, Double> lootTable = new HashMap<>();
    private final Random random = new Random();

    public LootTable() {
        // Voeg loot items toe met hun respectieve kansen (bijvoorbeeld 0.1 voor 10% kans)
        lootTable.put(new ItemStack(Material.DIAMOND, 1), 0.1);
        lootTable.put(new ItemStack(Material.GOLD_INGOT, 5), 0.2);
        lootTable.put(new ItemStack(Material.IRON_INGOT, 10), 0.3);
        // Voeg meer items toe zoals nodig
    }

    public ItemStack getRandomLoot() {
        double roll = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Map.Entry<ItemStack, Double> entry : lootTable.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (roll <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        return null; // Fallback, zou eigenlijk nooit moeten gebeuren
    }
}
