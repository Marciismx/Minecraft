package com.marc;

import org.bukkit.plugin.java.JavaPlugin;
import com.marc.Mission.MissionManager;
import com.marc.Mission.MissionCommand;
import com.marc.Mission.MissionCompleteListener;
import com.marc.economy.EconomyManager;
import com.marc.economy.EconomyCommand;
import com.marc.leaderboard.LeaderboardManager;
import com.marc.leaderboard.LeaderboardCommand;
import com.marc.areas.AreaManager;
import com.marc.areas.AreaCommand;
import com.marc.rewards.RewardManager;
import com.marc.config.ConfigManager;

public class Main extends JavaPlugin {
    private MissionManager missionManager;
    private EconomyManager economyManager;
    private LeaderboardManager leaderboardManager;
    private AreaManager areaManager;
    private RewardManager rewardManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager();
        missionManager = new MissionManager(configManager);
        economyManager = new EconomyManager();
        leaderboardManager = new LeaderboardManager();
        areaManager = new AreaManager(configManager);
        rewardManager = new RewardManager();

        this.getCommand("mission").setExecutor(new MissionCommand(missionManager));
        this.getCommand("balance").setExecutor(new EconomyCommand(economyManager));
        this.getCommand("leaderboard").setExecutor(new LeaderboardCommand(leaderboardManager));
        this.getCommand("area").setExecutor(new AreaCommand(areaManager));

        getServer().getPluginManager().registerEvents(new MissionCompleteListener(missionManager, areaManager, rewardManager), this);

        getLogger().info("MinecraftEFT plugin enabled");
    }

    @Override
    public void onDisable() {
        configManager.saveConfigs();
        getLogger().info("MinecraftEFT plugin disabled");
    }
}
