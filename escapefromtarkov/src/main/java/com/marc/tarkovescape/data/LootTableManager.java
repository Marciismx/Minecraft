package com.marc.tarkovescape.data;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class LootTableManager {
    private final TarkovEscape plugin;
    private final Map<String, List<ItemStack>> lootTable = new HashMap<>();
    private final Random random = new Random();
    private final Map<String, Double> rarityChances = new HashMap<>();
    private final Map<Integer, Double> rollChances = new HashMap<>();

    public LootTableManager(TarkovEscape plugin) {
        this.plugin = plugin;
        loadConfig();
        loadLootTableFromFile();
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        // Load rarity chances
        ConfigurationSection raritySection = config.getConfigurationSection("rarity_chances");
        if (raritySection != null) {
            for (String key : raritySection.getKeys(false)) {
                rarityChances.put(key, raritySection.getDouble(key));
                plugin.getLogger().info("Loaded rarity chance: " + key + " = " + raritySection.getDouble(key));
            }
        } else {
            plugin.getLogger().warning("Rarity chances section missing in config.yml");
        }

        // Load roll chances
        ConfigurationSection rollSection = config.getConfigurationSection("roll_chances");
        if (rollSection != null) {
            for (String key : rollSection.getKeys(false)) {
                rollChances.put(Integer.parseInt(key), rollSection.getDouble(key));
                plugin.getLogger().info("Loaded roll chance: " + key + " = " + rollSection.getDouble(key));
            }
        } else {
            plugin.getLogger().warning("Roll chances section missing in config.yml");
        }
    }

    private void loadLootTableFromFile() {
        File lootFile = new File(plugin.getDataFolder(), "loottable.yml");
        if (!lootFile.exists()) {
            plugin.saveResource("loottable.yml", false);
            plugin.getLogger().info("Saved default loottable.yml");
        }

        FileConfiguration lootConfig = YamlConfiguration.loadConfiguration(lootFile);
        List<Map<?, ?>> items = lootConfig.getMapList("items");
        if (items != null) {
            for (Map<?, ?> itemData : items) {
                String itemName = (String) itemData.get("item_name");
                String itemMaterial = (String) itemData.get("item_material");
                double dropChance = (double) itemData.get("drop_chance");
                String rarity = (String) itemData.get("rarity");
                if (itemName != null && itemMaterial != null && rarity != null) {
                    Material material = Material.getMaterial(itemMaterial.toUpperCase());
                    if (material != null) {
                        ItemStack item = new ItemStack(material, 1);
                        lootTable.computeIfAbsent(rarity, k -> new ArrayList<>()).add(item);
                        plugin.getLogger().info("Added item to loot table: " + itemName + " (" + itemMaterial + ") with drop chance: " + dropChance + " and rarity: " + rarity);
                    } else {
                        plugin.getLogger().warning("Material not found: " + itemMaterial);
                    }
                } else {
                    plugin.getLogger().warning("Invalid item entry in loottable.yml");
                }
            }
        } else {
            plugin.getLogger().warning("Items section missing in loottable.yml");
        }
    }

    public List<ItemStack> generateLoot() {
        List<ItemStack> loot = new ArrayList<>();
        int numItems = getRandomRoll();

        plugin.getLogger().info("Number of items to generate: " + numItems);

        for (int i = 1; i <= numItems; i++) {
            String rarity = determineRarity();
            ItemStack item = getRandomItemFromRarity(rarity);
            if (item != null) {
                loot.add(item);
                plugin.getLogger().info("Generated item: " + item.getType() + " with rarity: " + rarity);
            } else {
                plugin.getLogger().warning("No items found for rarity: " + rarity);
            }
        }

        return loot;
    }

    private int getRandomRoll() {
        double roll = random.nextDouble() * 100;
        double cumulativeProbability = 0.0;
        for (Map.Entry<Integer, Double> entry : rollChances.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (roll <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        plugin.getLogger().warning("No roll chance matched, defaulting to 1");
        return 1; // fallback, should not happen if config is set properly
    }

    private String determineRarity() {
        double roll = random.nextDouble() * 100;
        double cumulativeProbability = 0.0;
        for (Map.Entry<String, Double> entry : rarityChances.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (roll <= cumulativeProbability) {
                return entry.getKey();
            }
        }
        plugin.getLogger().warning("No rarity chance matched, defaulting to normal");
        return "normal"; // fallback, should not happen if config is set properly
    }

    private ItemStack getRandomItemFromRarity(String rarity) {
        List<ItemStack> items = lootTable.get(rarity);
        if (items != null && !items.isEmpty()) {
            return items.get(random.nextInt(items.size()));
        }
        plugin.getLogger().warning("Loot table is empty or null for rarity: " + rarity);
        return null;
    }
}
