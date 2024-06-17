package com.marc.player;

import com.marc.mission.MissionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class PlayerJoinListener implements Listener {
    private MissionManager missionManager;

    public PlayerJoinListener(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load player's mission progress from storage
        missionManager.getMissionStorage().loadMissionProgress(player.getUniqueId());
    }
}