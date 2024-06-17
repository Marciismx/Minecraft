package com.marc.Mission;

import com.marc.areas.AreaManager;
import com.marc.rewards.RewardManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        for (Mission mission : missionManager.getMissions()) {
            if (areaManager.isInArea(mission.getAreaName(), event.getPlayer().getLocation())) {
                rewardManager.giveReward(event.getPlayer(), mission.getReward());
                event.getPlayer().sendMessage("Mission complete: " + mission.getName());
                missionManager.completeMission(mission, event.getPlayer());
                break;
            }
        }
    }
}
