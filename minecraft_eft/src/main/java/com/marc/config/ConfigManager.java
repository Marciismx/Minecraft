package com.marc.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private File missionsFile;
    private FileConfiguration missionsConfig;
    private File rewardsFile;
    private FileConfiguration rewardsConfig;
    private File areasFile;
    private FileConfiguration areasConfig;

    public ConfigManager() {
        missionsFile = new File("plugins/MinecraftEFT/missions.yml");
        rewardsFile = new File("plugins/MinecraftEFT/rewards.yml");
        areasFile = new File("plugins/MinecraftEFT/areas.yml");

        missionsConfig = YamlConfiguration.loadConfiguration(missionsFile);
        rewardsConfig = YamlConfiguration.loadConfiguration(rewardsFile);
        areasConfig = YamlConfiguration.loadConfiguration(areasFile);
    }

    public FileConfiguration getMissionsConfig() {
        return missionsConfig;
    }

    public FileConfiguration getRewardsConfig() {
        return rewardsConfig;
    }

    public FileConfiguration getAreasConfig() {
        return areasConfig;
    }

    public void saveConfigs() {
        try {
            missionsConfig.save(missionsFile);
            rewardsConfig.save(rewardsFile);
            areasConfig.save(areasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
