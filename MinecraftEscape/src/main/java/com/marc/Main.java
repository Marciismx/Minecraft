package com.marc;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("MinecraftEscape has been enabled!");
        this.getCommand("escape").setExecutor(new EscapeCommand());

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MinecraftEscape has been disabled.");
    }
}