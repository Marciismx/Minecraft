package com.marc;

import com.marc.config.ConfigManager;
import com.marc.economy.EconomyManager;
import com.marc.leaderboard.LeaderboardManager;
import com.marc.mission.MissionCommand;
import com.marc.mission.MissionManager;
import com.marc.mission.MissionUI;
import com.marc.player.PlayerJoinListener;
import com.marc.player.PlayerCollectListener;
import org.bukkit.plugin.java.JavaPlugin;

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

        // Initialize managers
        economyManager = new EconomyManager();
        missionManager = new MissionManager();
        leaderboardManager = new LeaderboardManager();

        // Initialize Mission UI
        missionUI = new MissionUI(missionManager, configManager);

        // Register commands
        getCommand("mission").setExecutor(new MissionCommand(missionManager, economyManager, configManager, missionUI));

        // Register event listeners
        getServer().getPluginManager().registerEvents(missionUI, this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerCollectListener(missionManager, economyManager), this);

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