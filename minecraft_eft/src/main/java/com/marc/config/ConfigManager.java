package com.marc.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private JavaPlugin plugin;
    private FileConfiguration areasConfig;
    private FileConfiguration missionsConfig;
    private FileConfiguration rewardsConfig;
    private File areasConfigFile;
    private File missionsConfigFile;
    private File rewardsConfigFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadAreasConfig();
        loadMissionsConfig();
        loadRewardsConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getAreasConfig() {
        if (areasConfig == null) {
            loadAreasConfig();
        }
        return areasConfig;
    }

    private void loadAreasConfig() {
        areasConfigFile = new File(plugin.getDataFolder(), "areas.yml");
        if (!areasConfigFile.exists()) {
            plugin.saveResource("areas.yml", false);
        }
        areasConfig = YamlConfiguration.loadConfiguration(areasConfigFile);
    }

    public void saveAreasConfig() {
        try {
            areasConfig.save(areasConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMissionsConfig() {
        if (missionsConfig == null) {
            loadMissionsConfig();
        }
        return missionsConfig;
    }

    private void loadMissionsConfig() {
        missionsConfigFile = new File(plugin.getDataFolder(), "missions.yml");
        if (!missionsConfigFile.exists()) {
            plugin.saveResource("missions.yml", false);
        }
        missionsConfig = YamlConfiguration.loadConfiguration(missionsConfigFile);
    }

    public void saveMissionsConfig() {
        try {
            missionsConfig.save(missionsConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getRewardsConfig() {
        if (rewardsConfig == null) {
            loadRewardsConfig();
        }
        return rewardsConfig;
    }

    private void loadRewardsConfig() {
        rewardsConfigFile = new File(plugin.getDataFolder(), "rewards.yml");
        if (!rewardsConfigFile.exists()) {
            plugin.saveResource("rewards.yml", false);
        }
        rewardsConfig = YamlConfiguration.loadConfiguration(rewardsConfigFile);
    }

    public void saveRewardsConfig() {
        try {
            rewardsConfig.save(rewardsConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfigs() {
        plugin.saveConfig();
        saveAreasConfig();
        saveMissionsConfig();
        saveRewardsConfig();
    }
}
