package com.marc;

import com.marc.commands.EscapeCommand;
import com.marc.config.ConfigManager;
import com.marc.economy.EconomyManager;
import com.marc.mission.MissionCommand;
import com.marc.mission.MissionManager;
import com.marc.mission.MissionUI;
import com.marc.player.PlayerCollectListener;
import com.marc.player.PlayerJoinListener;
import com.marc.storage.FileMissionStorage;
import com.marc.storage.MissionStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ConfigManager configManager;
    private MissionManager missionManager;
    private MissionUI missionUI;
    private EconomyManager economyManager;
    private MissionStorage missionStorage;

    @Override
    public void onEnable() {
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.economyManager = new EconomyManager();
        this.missionStorage = new FileMissionStorage(this);
        this.missionUI = new MissionUI(missionManager, configManager);
        this.missionManager = new MissionManager(missionStorage, configManager, missionUI);

        // Register event listeners
        getServer().getPluginManager().registerEvents(missionManager, this);
        getServer().getPluginManager().registerEvents(missionUI, this);
        getServer().getPluginManager().registerEvents(new PlayerCollectListener(missionManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(missionManager), this);

        // Register commands
        getCommand("mission").setExecutor(new MissionCommand(missionManager, economyManager, configManager));
        getCommand("escape").setExecutor(new EscapeCommand(missionManager, economyManager, configManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MissionManager getMissionManager() {
        return missionManager;
    }

    public MissionUI getMissionUI() {
        return missionUI;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public MissionStorage getMissionStorage() {
        return missionStorage;
    }
}
