package com.marc.tarkovescape;

import com.marc.tarkovescape.commands.CommandManager;
import com.marc.tarkovescape.data.PlayerDataManager;
import com.marc.tarkovescape.listeners.LootSysteem;
import com.marc.tarkovescape.listeners.PlayerJoinListener;
import com.marc.tarkovescape.managers.GroupManager;
import com.marc.tarkovescape.managers.SpawnManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TarkovEscape extends JavaPlugin {
    private LootSysteem lootSysteem;
    private PlayerDataManager playerDataManager;
    private Connection databaseConnection;
    private SpawnManager spawnManager;
    private GroupManager groupManager;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Zorgt ervoor dat config.yml wordt geladen
        getLogger().info("TarkovEscape is ingeschakeld!");

        lootSysteem = new LootSysteem(this);
        getServer().getPluginManager().registerEvents(lootSysteem, this);

        // Initialize PlayerDataManager
        playerDataManager = new PlayerDataManager(this);

        // Initialize SpawnManager
        spawnManager = new SpawnManager(this);

        // Initialize GroupManager
        groupManager = new GroupManager();

        // Register commands
        new CommandManager(this);

        // Initialize log systeem
        initializeLogSystem();

        // Setup database connection
        setupDatabaseConnection();

        // Register PlayerJoinListener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("TarkovEscape is uitgeschakeld!");
        // Sluit de databaseverbinding
        closeDatabaseConnection();
    }

    private void initializeLogSystem() {
        // Implementeer een log systeem voor het bijhouden van spelersgegevens
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                playerDataManager.loadPlayerData(event.getPlayer());
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                playerDataManager.savePlayerData(event.getPlayer());
            }
        }, this);
    }

    private void setupDatabaseConnection() {
        FileConfiguration config = getConfig();
        String url = config.getString("database.url");
        String username = config.getString("database.username");
        String password = config.getString("database.password");

        try {
            databaseConnection = DriverManager.getConnection(url, username, password);
            getLogger().info("Verbonden met de database!");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Kon geen verbinding maken met de database.");
        }
    }

    private void closeDatabaseConnection() {
        if (databaseConnection != null) {
            try {
                databaseConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getDatabaseConnection() {
        return databaseConnection;
    }

    public LootSysteem getLootSysteem() {
        return lootSysteem;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
