package com.marc.tarkovescape.listeners;

import com.marc.tarkovescape.TarkovEscape;
import com.marc.tarkovescape.managers.SpawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final SpawnManager spawnManager;

    public PlayerJoinListener(TarkovEscape plugin) {
        this.spawnManager = plugin.getSpawnManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        spawnManager.spawnPlayer(player);
    }
}
