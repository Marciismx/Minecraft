package com.marc.tarkovescape.managers;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BunkerManager {
    private final TarkovEscape plugin;
    private final Map<UUID, Location> playerBunkers = new HashMap<>();

    public BunkerManager(TarkovEscape plugin) {
        this.plugin = plugin;
        // Laad hier de bunkers vanuit de opslag, bijvoorbeeld een bestand of database
    }

    public void setupBunker(Player player) {
        UUID playerUUID = player.getUniqueId();
        Location bunkerLocation = playerBunkers.get(playerUUID);
        if (bunkerLocation == null) {
            // Als er nog geen bunker is, maak er een aan
            bunkerLocation = new Location(Bukkit.getWorld("world"), 0, 100, 0); // Voorbeeldlocatie
            playerBunkers.put(playerUUID, bunkerLocation);
            // Sla de bunker op in de opslag
        }
        player.teleport(bunkerLocation);
    }

    public Location getBunker(UUID playerUUID) {
        return playerBunkers.get(playerUUID);
    }
}
