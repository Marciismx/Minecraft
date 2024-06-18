package com.marc;

import org.bukkit.plugin.java.JavaPlugin;
import com.marc.data.UserDataManager;
import com.marc.data.DatabaseManager;
import com.marc.config.ConfigManager;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private UserDataManager userDataManager;
    private DatabaseManager databaseManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        String storageType = configManager.getConfig().getString("storage.type");

        if (storageType.equalsIgnoreCase("database")) {
            String url = configManager.getConfig().getString("storage.database.url");
            String username = configManager.getConfig().getString("storage.database.username");
            String password = configManager.getConfig().getString("storage.database.password");
            databaseManager = new DatabaseManager();
            try {
                databaseManager.connect(url, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            userDataManager = new UserDataManager(this);
        }

        getLogger().info("MinecraftEFT plugin enabled");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            try {
                databaseManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            userDataManager.saveUserData();
        }
        getLogger().info("MinecraftEFT plugin disabled");
    }
}
