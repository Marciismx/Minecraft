package com.marc.tarkovescape.data;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PlayerDataManager {
    private final TarkovEscape plugin;
    private final File dataFolder;
    private Connection connection;

    public PlayerDataManager(TarkovEscape plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");

        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }

        if (plugin.getConfig().getString("storage.method").equalsIgnoreCase("database")) {
            setupDatabase();
        }
    }

    private void setupDatabase() {
        String url = plugin.getConfig().getString("storage.database.url");
        String username = plugin.getConfig().getString("storage.database.username");
        String password = plugin.getConfig().getString("storage.database.password");

        try {
            connection = DriverManager.getConnection(url, username, password);
            String createTable = "CREATE TABLE IF NOT EXISTS player_data (uuid VARCHAR(36) PRIMARY KEY, data TEXT)";
            connection.prepareStatement(createTable).execute();
            plugin.getLogger().info("Database connection established and table created if not exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerData(Player player) {
        plugin.getLogger().info("Saving player data for " + player.getName());
        if (plugin.getConfig().getString("storage.method").equalsIgnoreCase("database")) {
            savePlayerDataToDatabase(player);
        } else {
            savePlayerDataToFile(player);
        }
    }

    private void savePlayerDataToFile(Player player) {
        plugin.getLogger().info("Saving player data to file for " + player.getName());
        File playerFile = new File(dataFolder, player.getUniqueId().toString() + ".yml");
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("inventory", player.getInventory().getContents());

        try {
            yamlConfiguration.save(playerFile);
            plugin.getLogger().info("Player data saved to file successfully for " + player.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player data to file for " + player.getName());
            e.printStackTrace();
        }
    }

    private void savePlayerDataToDatabase(Player player) {
        plugin.getLogger().info("Saving player data to database for " + player.getName());
        UUID uuid = player.getUniqueId();
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.set("inventory", player.getInventory().getContents());
        String data = yamlConfiguration.saveToString();

        try {
            PreparedStatement ps = connection.prepareStatement("REPLACE INTO player_data (uuid, data) VALUES (?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, data);
            ps.executeUpdate();
            plugin.getLogger().info("Player data saved to database successfully for " + player.getName());
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to save player data to database for " + player.getName());
            e.printStackTrace();
        }
    }

    public void loadPlayerData(Player player) {
        plugin.getLogger().info("Loading player data for " + player.getName());
        if (plugin.getConfig().getString("storage.method").equalsIgnoreCase("database")) {
            loadPlayerDataFromDatabase(player);
        } else {
            loadPlayerDataFromFile(player);
        }
    }

    private void loadPlayerDataFromFile(Player player) {
        plugin.getLogger().info("Loading player data from file for " + player.getName());
        File playerFile = new File(dataFolder, player.getUniqueId().toString() + ".yml");
        if (!playerFile.exists()) {
            plugin.getLogger().info("No data file found for player " + player.getName());
            return;
        }

        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerFile);
        List<ItemStack> inventory = (List<ItemStack>) yamlConfiguration.getList("inventory");
        if (inventory != null) {
            player.getInventory().setContents(inventory.toArray(new ItemStack[0]));
            plugin.getLogger().info("Player data loaded from file successfully for " + player.getName());
        } else {
            plugin.getLogger().warning("Failed to load inventory from file for " + player.getName());
        }
    }

    private void loadPlayerDataFromDatabase(Player player) {
        plugin.getLogger().info("Loading player data from database for " + player.getName());
        UUID uuid = player.getUniqueId();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT data FROM player_data WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String data = rs.getString("data");
                YamlConfiguration yamlConfiguration = new YamlConfiguration();
                yamlConfiguration.loadFromString(data);
                List<ItemStack> inventory = (List<ItemStack>) yamlConfiguration.getList("inventory");
                if (inventory != null) {
                    player.getInventory().setContents(inventory.toArray(new ItemStack[0]));
                    plugin.getLogger().info("Player data loaded from database successfully for " + player.getName());
                } else {
                    plugin.getLogger().warning("Failed to load inventory from database for " + player.getName());
                }
            } else {
                plugin.getLogger().info("No data found in database for player " + player.getName());
            }
        } catch (SQLException | InvalidConfigurationException e) {
            plugin.getLogger().severe("Failed to load player data from database for " + player.getName());
            e.printStackTrace();
        }
    }
}
