package com.marc.mission;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.marc.config.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class MissionUI implements Listener {
    private MissionManager missionManager;
    private ConfigManager configManager;

    public MissionUI(MissionManager missionManager, ConfigManager configManager) {
        this.missionManager = missionManager;
        this.configManager = configManager;
    }

    public void openMissionMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Available Missions");

        for (String key : configManager.getConfig().getConfigurationSection("missions").getKeys(false)) {
            String path = "missions." + key + ".";
            String name = configManager.getConfig().getString(path + "name");
            String description = configManager.getConfig().getString(path + "description");
            Material rewardItemMaterial = Material.getMaterial(configManager.getConfig().getString(path + "rewardItem").toUpperCase());
            int rewardItemAmount = configManager.getConfig().getInt(path + "rewardItemAmount");
            List<String> dependencies = configManager.getConfig().getStringList(path + "dependencies");

            // Check of speler de afhankelijkheden heeft voltooid
            boolean dependenciesCompleted = dependencies.stream()
                    .allMatch(dep -> missionManager.hasCompletedMission(player, dep));

            // Als de afhankelijkheden voltooid zijn, toon de missie in de UI
            if (dependenciesCompleted) {
                ItemStack item = new ItemStack(rewardItemMaterial, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                List<String> lore = new ArrayList<>();
                lore.add(description);
                lore.add("Reward: " + rewardItemAmount + " " + rewardItemMaterial);
                meta.setLore(lore);
                item.setItemMeta(meta);

                inventory.addItem(item);
            }
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Available Missions")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                String missionName = event.getCurrentItem().getItemMeta().getDisplayName();
                Player player = (Player) event.getWhoClicked();
                assignMissionToPlayer(player, missionName);
                player.closeInventory();
            }
        }
    }

    private void assignMissionToPlayer(Player player, String missionName) {
        for (String key : configManager.getConfig().getConfigurationSection("missions").getKeys(false)) {
            String path = "missions." + key + ".";
            String name = configManager.getConfig().getString(path + "name");
            if (name.equals(missionName)) {
                Mission mission = createMissionFromConfig(key);
                missionManager.assignMission(player, mission);
                player.sendMessage("Mission assigned: " + mission.getName());
                return;
            }
        }
    }

    private Mission createMissionFromConfig(String missionType) {
        String path = "missions." + missionType + ".";
        String name = configManager.getConfig().getString(path + "name");
        String description = configManager.getConfig().getString(path + "description");
        double rewardMoney = configManager.getConfig().getDouble(path + "rewardMoney");
        String rewardItemStr = configManager.getConfig().getString(path + "rewardItem");
        int rewardItemAmount = configManager.getConfig().getInt(path + "rewardItemAmount");
        int targetAmount = configManager.getConfig().getInt(path + "targetAmount");

        if (name != null && description != null && rewardItemStr != null) {
            Material rewardItemMaterial = Material.getMaterial(rewardItemStr.toUpperCase());
            ItemStack rewardItem = new ItemStack(rewardItemMaterial, rewardItemAmount);
            return new Mission(name, description, rewardMoney, rewardItem, Mission.MissionType.valueOf(missionType.toUpperCase()), targetAmount);
        }
        return null;
    }
}
