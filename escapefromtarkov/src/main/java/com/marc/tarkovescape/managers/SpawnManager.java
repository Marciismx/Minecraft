package com.marc.tarkovescape.managers;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnManager {
    private final TarkovEscape plugin;
    private final List<Location> spawnLocations;
    private final List<Location> extractionPoints;
    private final Random random;

    public SpawnManager(TarkovEscape plugin) {
        this.plugin = plugin;
        this.spawnLocations = new ArrayList<>();
        this.extractionPoints = new ArrayList<>();
        this.random = new Random();
        loadLocations();
    }

    private void loadLocations() {
        FileConfiguration config = plugin.getConfig();
        List<String> spawnLocationStrings = config.getStringList("spawnLocations");
        for (String loc : spawnLocationStrings) {
            String[] parts = loc.split(",");
            if (parts.length == 4) {
                World world = Bukkit.getWorld(parts[0]);
                if (world != null) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    spawnLocations.add(new Location(world, x, y, z));
                }
            }
        }

        List<String> extractionPointStrings = config.getStringList("extractionPoints");
        for (String loc : extractionPointStrings) {
            String[] parts = loc.split(",");
            if (parts.length == 4) {
                World world = Bukkit.getWorld(parts[0]);
                if (world != null) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    extractionPoints.add(new Location(world, x, y, z));
                }
            }
        }
    }

    public Location getRandomSpawnLocation() {
        if (spawnLocations.isEmpty()) {
            return null; // Of een standaardlocatie
        }
        return spawnLocations.get(random.nextInt(spawnLocations.size()));
    }

    public Location getRandomExtractionPoint() {
        if (extractionPoints.isEmpty()) {
            return null; // Of een standaardlocatie
        }
        return extractionPoints.get(random.nextInt(extractionPoints.size()));
    }

    public void spawnPlayer(Player player) {
        Location spawnLocation = getRandomSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            player.sendMessage("Je bent gespawned op een willekeurige locatie.");
        } else {
            player.sendMessage("Kon geen spawnlocatie vinden. Neem contact op met de serverbeheerder.");
        }
    }
}
