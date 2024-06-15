package com.marc.player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import com.marc.mission.MissionManager;
import com.marc.economy.EconomyManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class PlayerCollectListener implements Listener {
    private MissionManager missionManager;
    private EconomyManager economyManager;

    public PlayerCollectListener(MissionManager missionManager, EconomyManager economyManager) {
        this.missionManager = missionManager;
        this.economyManager = economyManager;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (item.getType() == Material.DIAMOND) {
            if (missionManager.getActiveMission(event.getPlayer()) != null) {
                missionManager.completeMission(event.getPlayer(), economyManager);
            }
        }
    }
}
