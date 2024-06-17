package com.marc.storage;

import com.marc.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileMissionStorage implements MissionStorage {

    private final Main plugin;
    private File storageFile;
    private FileConfiguration storageConfig;

    public FileMissionStorage(Main plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        if (storageFile == null) {
            storageFile = new File(plugin.getDataFolder(), "data.yml");
        }
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        storageConfig = YamlConfiguration.loadConfiguration(storageFile);
    }

    public void saveMissionProgress(UUID playerUUID, Map<String, Boolean> progress) {
        storageConfig.set(playerUUID.toString(), progress);
        save();
    }

    public Map<String, Boolean> loadMissionProgress(UUID playerUUID) {
        if (storageConfig.contains(playerUUID.toString())) {
            Map<String, Object> progressMap = storageConfig.getConfigurationSection(playerUUID.toString()).getValues(false);
            Map<String, Boolean> progress = new HashMap<>();
            for (Map.Entry<String, Object> entry : progressMap.entrySet()) {
                progress.put(entry.getKey(), (Boolean) entry.getValue());
            }
            return progress;
        } else {
            return new HashMap<>();
        }
    }

    public void save() {
        try {
            storageConfig.save(storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
