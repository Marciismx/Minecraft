package com.marc.tarkovescape.data;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootTableManager {

    private final TarkovEscape plugin;

    public LootTableManager(TarkovEscape plugin) {
        this.plugin = plugin;
    }

    public void loadLootTable() {
        // Laad de loottabel uit de database
        try (Connection connection = plugin.getDatabaseConnection()) {
            // Controleer en laad de loottabel
            String query = "SELECT * FROM loottable";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    // Verwerk elke rij in de loottabel
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemStack> generateLoot() {
        List<ItemStack> loot = new ArrayList<>();
        Random random = new Random();

        // Eerste roll: Bepaal aantal items
        int numItems = random.nextInt(5) + 1;

        // Tweede roll: Bepaal zeldzaamheid en items
        for (int i = 1; i <= numItems; i++) {
            String rarity = determineRarity(i);
            ItemStack item = getRandomItemFromRarity(rarity);
            if (item != null) {
                loot.add(item);
            }
        }

        return loot;
    }

    private String determineRarity(int rollNumber) {
        switch (rollNumber) {
            case 1:
                return "normal";
            case 2:
                return "rare";
            case 3:
                return "epic";
            case 4:
                return "legendary";
            case 5:
                return "mythic";
            default:
                return "normal";
        }
    }

    private ItemStack getRandomItemFromRarity(String rarity) {
        try (Connection connection = plugin.getDatabaseConnection()) {
            String query = "SELECT item_name, item_material FROM loottable WHERE rarity = ? ORDER BY RAND() LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, rarity);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String itemName = resultSet.getString("item_name");
                        Material material = Material.getMaterial(resultSet.getString("item_material"));
                        return new ItemStack(material, 1); // Pas aan indien hoeveelheid varieert
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
