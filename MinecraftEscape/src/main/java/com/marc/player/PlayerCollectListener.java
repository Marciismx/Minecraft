package com.marc.player;

import com.marc.economy.EconomyManager;
import com.marc.mission.Mission;
import com.marc.mission.MissionManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PlayerCollectListener implements Listener {
    private MissionManager missionManager;

    public PlayerCollectListener(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @EventHandler
    public void onPlayerPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        Mission activeMission = missionManager.getActiveMission(player);
    
        if (activeMission != null && activeMission.getAction() == Mission.ActionType.COLLECT) {
            if (event.getItem().getItemStack().getType() == activeMission.getTarget()) {
                int count = missionManager.getItemCounts().getOrDefault(player.getUniqueId(), 0) + event.getItem().getItemStack().getAmount();
                missionManager.getItemCounts().put(player.getUniqueId(), count);
                if (count >= activeMission.getAmount()) {
                    missionManager.completeMission(player, new EconomyManager());
                }
            }
        }
    }
}
