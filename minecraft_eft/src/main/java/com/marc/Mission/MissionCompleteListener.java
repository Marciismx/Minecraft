package com.marc.Mission;

import com.marc.areas.AreaManager;
import com.marc.rewards.RewardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;

public class MissionCompleteListener implements Listener {
    private MissionManager missionManager;
    private AreaManager areaManager;
    private RewardManager rewardManager;

    public MissionCompleteListener(MissionManager missionManager, AreaManager areaManager, RewardManager rewardManager) {
        this.missionManager = missionManager;
        this.areaManager = areaManager;
        this.rewardManager = rewardManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Mission mission : missionManager.getMissions()) {
            if (areaManager.isInArea(mission.getAreaName(), player.getLocation())) {
                rewardManager.giveReward(player, mission.getReward());
                player.sendMessage("Mission complete: " + mission.getName());
                missionManager.completeMission(mission, player);
                break;
            }
        }
    }
}
