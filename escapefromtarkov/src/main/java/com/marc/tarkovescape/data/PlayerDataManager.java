package com.marc.tarkovescape.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final TarkovEscape plugin;
    private final Gson gson;
    private final File dataFolder;
    private Connection connection;

    public PlayerDataManager(TarkovEscape plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ItemStack[].class, new ItemStackArrayTypeAdapter())
                .setPrettyPrinting()
                .create();
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        if (plugin.getConfig().getBoolean("database.enabled")) {
            setupDatabase();
        }
    }

    private void setupDatabase() {
        String url = plugin.getConfig().getString("database.url");
        String username = plugin.getConfig().getString("database.username");
        String password = plugin.getConfig().getString("database.password");

        try {
            connection = DriverManager.getConnection(url, username, password);
            String createTable = "CREATE TABLE IF NOT EXISTS player_data (uuid VARCHAR(36) PRIMARY KEY, data TEXT)";
            connection.prepareStatement(createTable).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData(Player player) {
        if (plugin.getConfig().getBoolean("database.enabled")) {
            savePlayerDataToDatabase(player);
        } else {
            savePlayerDataToFile(player);
        }
    }

    private void savePlayerDataToFile(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId().toString() + ".json");
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("inventory", gson.toJson(player.getInventory().getContents()));
        // Voeg meer spelersgegevens toe die je wilt opslaan

        try (FileWriter writer = new FileWriter(playerFile)) {
            gson.toJson(playerData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void savePlayerDataToDatabase(Player player) {
        UUID uuid = player.getUniqueId();
        Map<String, Object> playerData = new HashMap<>();
        playerData.put("inventory", gson.toJson(player.getInventory().getContents()));
        // Voeg meer spelersgegevens toe die je wilt opslaan

        String json = gson.toJson(playerData);
        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO player_data (uuid, data) VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, json);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerData(Player player) {
        if (plugin.getConfig().getBoolean("database.enabled")) {
            loadPlayerDataFromDatabase(player);
        } else {
            loadPlayerDataFromFile(player);
        }
    }

    private void loadPlayerDataFromFile(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId().toString() + ".json");
        if (!playerFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(playerFile)) {
            Map<String, Object> playerData = gson.fromJson(reader, Map.class);
            ItemStack[] inventory = gson.fromJson((String) playerData.get("inventory"), ItemStack[].class);
            player.getInventory().setContents(inventory);
            // Laad andere spelersgegevens
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPlayerDataFromDatabase(Player player) {
        UUID uuid = player.getUniqueId();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT data FROM player_data WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String json = rs.getString("data");
                Map<String, Object> playerData = gson.fromJson(json, Map.class);
                ItemStack[] inventory = gson.fromJson((String) playerData.get("inventory"), ItemStack[].class);
                player.getInventory().setContents(inventory);
                // Laad andere spelersgegevens
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
