package com.marc;

import org.bukkit.plugin.java.JavaPlugin;

import com.marc.Mission.MissionCommand;
import com.marc.Mission.MissionManager;

public class Main extends JavaPlugin {
    private MissionManager missionManager;

    @Override
    public void onEnable() {
        missionManager = new MissionManager();
        this.getCommand("mission").setExecutor(new MissionCommand(missionManager));
        getLogger().info("MinecraftEFT plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("MinecraftEFT plugin disabled");
    }
}