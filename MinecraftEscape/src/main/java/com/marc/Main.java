package com.marc;

import com.marc.config.ConfigManager;
import com.marc.economy.EconomyManager;
import com.marc.leaderboard.LeaderboardManager;
import com.marc.mission.MissionCommand;
import com.marc.mission.MissionManager;
import com.marc.mission.MissionUI;
import com.marc.player.PlayerJoinListener;
import com.marc.player.PlayerCollectListener;
import com.marc.storage.FileMissionStorage;
import com.marc.storage.MissionStorage;
import com.marc.storage.DatabaseMissionStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private ConfigManager configManager;
    private EconomyManager economyManager;
    private MissionManager missionManager;
    private LeaderboardManager leaderboardManager;
    private MissionUI missionUI;

    @Override
    public void onEnable() {
        // Initialize config manager
        configManager = new ConfigManager(this);
        configManager.saveDefaultConfig();

        // Initialize storage
        MissionStorage missionStorage;
        String storageType = configManager.getConfig().getString("storage.type");
        if ("database".equalsIgnoreCase(storageType)) {
            String url = configManager.getConfig().getString("storage.database.url");
            String username = configManager.getConfig().getString("storage.database.username");
            String password = configManager.getConfig().getString("storage.database.password");
            missionStorage = new DatabaseMissionStorage(url, username, password);
        } else {
            String path = configManager.getConfig().getString("storage.file.path");
            if (path == null) {
                getLogger().severe("File path for mission storage is not set in config.yml");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            missionStorage = new FileMissionStorage(new File(path));
        }

        // Initialize managers
        economyManager = new EconomyManager();
        missionManager = new MissionManager(missionStorage);
        leaderboardManager = new LeaderboardManager();

        // Initialize Mission UI
        missionUI = new MissionUI(missionManager, configManager);

        // Register commands
        getCommand("mission").setExecutor(new MissionCommand(missionManager, economyManager, configManager, missionUI));
        getCommand("leaderboard").setExecutor(new com.marc.leaderboard.LeaderboardCommand(leaderboardManager));

        // Register event listeners
        getServer().getPluginManager().registerEvents(missionUI, this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCollectListener(missionManager, economyManager), this);
        getServer().getPluginManager().registerEvents(missionManager, this);

        getLogger().info("MinecraftEscape has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MinecraftEscape has been disabled.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public MissionManager getMissionManager() {
        return missionManager;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public MissionUI getMissionUI() {
        return missionUI;
    }
}
