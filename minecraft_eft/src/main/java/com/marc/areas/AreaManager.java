package com.marc.areas;

import com.marc.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class AreaManager {
    private HashMap<String, Location> areas;
    private ConfigManager configManager;

    public AreaManager(ConfigManager configManager) {
        this.areas = new HashMap<>();
        this.configManager = configManager;
        loadAreas();
    }

    public void addArea(String name, Location location) {
        areas.put(name, location);
        saveAreas();
    }

    public Location getArea(String name) {
        return areas.get(name);
    }

    public boolean isInArea(String name, Location location) {
        Location areaLocation = areas.get(name);
        if (areaLocation == null) {
            return false;
        }
        double radius = 5.0;
        return areaLocation.getWorld().equals(location.getWorld())
                && areaLocation.distance(location) <= radius;
    }

    private void loadAreas() {
        FileConfiguration config = configManager.getAreasConfig();
        for (String key : config.getKeys(false)) {
            String name = config.getString(key + ".name");
            double x = config.getDouble(key + ".x");
            double y = config.getDouble(key + ".y");
            double z = config.getDouble(key + ".z");
            String world = config.getString(key + ".world");
            Location location = new Location(Bukkit.getWorld(world), x, y, z);
            areas.put(name, location);
        }
    }

    private void saveAreas() {
        FileConfiguration config = configManager.getAreasConfig();
        config.set("areas", null);
        for (String name : areas.keySet()) {
            Location location = areas.get(name);
            String key = "area." + name;
            config.set(key + ".name", name);
            config.set(key + ".x", location.getX());
            config.set(key + ".y", location.getY());
            config.set(key + ".z", location.getZ());
            config.set(key + ".world", location.getWorld().getName());
        }
        configManager.saveAreasConfig();
    }
}
