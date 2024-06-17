package com.marc.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileMissionStorage implements MissionStorage {
    private File file;
    private FileConfiguration config;

    public FileMissionStorage(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void saveMissionProgress(UUID playerId, Map<String, Boolean> progress) {
        for (Map.Entry<String, Boolean> entry : progress.entrySet()) {
            config.set(playerId.toString() + "." + entry.getKey(), entry.getValue());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Boolean> loadMissionProgress(UUID playerId) {
        Map<String, Boolean> progress = new HashMap<>();
        if (config.contains(playerId.toString())) {
            for (String key : config.getConfigurationSection(playerId.toString()).getKeys(false)) {
                progress.put(key, config.getBoolean(playerId.toString() + "." + key));
            }
        }
        return progress;
    }
}
