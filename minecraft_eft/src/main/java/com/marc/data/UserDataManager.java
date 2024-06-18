package com.marc.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

public class UserDataManager {
    private FileConfiguration userDataConfig;
    private File userDataFile;
    private Map<UUID, UserData> userDataMap;

    public UserDataManager(JavaPlugin plugin) {
        userDataFile = new File(plugin.getDataFolder(), "userdata.yml");
        if (!userDataFile.exists()) {
            try {
                userDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userDataConfig = YamlConfiguration.loadConfiguration(userDataFile);
        userDataMap = new HashMap<>();
        loadUserData();
    }

    private void loadUserData() {
        for (String key : userDataConfig.getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            int experience = userDataConfig.getInt(key + ".experience");
            double balance = userDataConfig.getDouble(key + ".balance");
            List<String> completedMissions = userDataConfig.getStringList(key + ".completedMissions");
            UserData userData = new UserData(uuid, experience, balance, completedMissions);
            userDataMap.put(uuid, userData);
        }
    }

    public void saveUserData() {
        for (UUID uuid : userDataMap.keySet()) {
            UserData userData = userDataMap.get(uuid);
            String key = uuid.toString();
            userDataConfig.set(key + ".experience", userData.getExperience());
            userDataConfig.set(key + ".balance", userData.getBalance());
            userDataConfig.set(key + ".completedMissions", userData.getCompletedMissions());
        }
        try {
            userDataConfig.save(userDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserData getUserData(UUID uuid) {
        return userDataMap.get(uuid);
    }

    public void setUserData(UserData userData) {
        userDataMap.put(userData.getUuid(), userData);
    }
}
