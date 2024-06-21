package com.marc.tarkovescape.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class LootSysteem implements Listener {
    private final JavaPlugin plugin;
    private final Random random;

    public LootSysteem(JavaPlugin plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        ItemStack[] drops = player.getInventory().getContents();

        for (ItemStack item : drops) {
            if (item != null && random.nextDouble() < plugin.getConfig().getDouble("lootDropChance")) {
                event.getDrops().remove(item);
            }
        }
    }

    public void spawnLoot(Player player) {
        ItemStack[] loot = {
                new ItemStack(Material.DIAMOND, random.nextInt(3)),
                new ItemStack(Material.GOLD_INGOT, random.nextInt(5)),
                new ItemStack(Material.IRON_INGOT, random.nextInt(10))
        };

        for (ItemStack item : loot) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
        }
    }
}
