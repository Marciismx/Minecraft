package com.marc;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("TestPluginTemplate has been enabled!");
        this.getCommand("hello").setExecutor(new HelloCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("TestPluginTemplate has been disabled.");
    }
}