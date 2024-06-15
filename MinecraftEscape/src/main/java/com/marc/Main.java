package com.marc;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private com.marc.economy.EconomyManager economyManager;
    private com.marc.mission.MissionManager missionManager;

    @Override
    public void onEnable() {
        getLogger().info("MinecraftEscape has been enabled!");

        economyManager = new com.marc.economy.EconomyManager();
        missionManager = new com.marc.mission.MissionManager();

        this.getCommand("escape").setExecutor(new EscapeCommand());
        this.getCommand("economy").setExecutor(new com.marc.economy.EconomyCommand(economyManager));
        this.getCommand("mission").setExecutor(new com.marc.mission.MissionCommand(missionManager, economyManager));

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinecraftEscape has been disabled.");
    }
}
