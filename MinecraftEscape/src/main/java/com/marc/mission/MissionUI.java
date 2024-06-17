package com.marc.mission;

import com.marc.config.ConfigManager;
import com.marc.mission.MissionManager; 
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            String action = configManager.getConfig().getString(path + "action");
            String target = configManager.getConfig().getString(path + "target");
            int amount = configManager.getConfig().getInt(path + "amount");
            String rewardType = configManager.getConfig().getString(path + "reward.type");
            String rewardItemStr = configManager.getConfig().getString(path + "reward.item");
            double rewardMoney = configManager.getConfig().getDouble(path + "reward.amount");
            int rewardItemAmount = configManager.getConfig().getInt(path + "reward.itemAmount");
            Material iconMaterial = Material.getMaterial(configManager.getConfig().getString(path + "icon").toUpperCase());

            boolean dependenciesCompleted = missionManager.canStartMission(player, name);

            if (dependenciesCompleted && !missionManager.hasCompletedMission(player, name)) {
                ItemStack item = new ItemStack(iconMaterial);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                List<String> lore = new ArrayList<>();
                lore.add(description);
                lore.add("Action: " + action + " " + amount + " " + target);
                lore.add("Reward: " + (rewardType.equalsIgnoreCase("MONEY") ? rewardMoney + " money" : rewardItemAmount + " " + rewardItemStr));
                meta.setLore(lore);
                item.setItemMeta(meta);

                inventory.addItem(item);
            }
        }

        ItemStack archiveItem = new ItemStack(Material.BOOK);
        ItemMeta archiveMeta = archiveItem.getItemMeta();
        archiveMeta.setDisplayName("Mission Archive");
        archiveItem.setItemMeta(archiveMeta);
        inventory.setItem(26, archiveItem);

        player.openInventory(inventory);
    }

    public void openConfirmationMenu(Player player, String missionName) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Start Mission?");
        
        ItemStack yesItem = new ItemStack(Material.GREEN_WOOL);
        ItemMeta yesMeta = yesItem.getItemMeta();
        yesMeta.setDisplayName("Yes");
        yesItem.setItemMeta(yesMeta);
        
        ItemStack noItem = new ItemStack(Material.RED_WOOL);
        ItemMeta noMeta = noItem.getItemMeta();
        noMeta.setDisplayName("No");
        noItem.setItemMeta(noMeta);

        inventory.setItem(3, yesItem);
        inventory.setItem(5, noItem);

        player.setMetadata("selectedMission", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("MinecraftEscape"), missionName));
        player.openInventory(inventory);
    }

    public void openArchiveMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Mission Archive");

        Map<String, Boolean> completedMissions = missionManager.getCompletedMissions(player);
        for (String missionName : completedMissions.keySet()) {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(missionName);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equals("Available Missions")) {
            event.setCancelled(true); // Prevent items from being moved
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                String missionName = event.getCurrentItem().getItemMeta().getDisplayName();
                if (missionName.equals("Mission Archive")) {
                    openArchiveMenu(player);
                } else {
                    openConfirmationMenu(player, missionName);
                }
            }
        } else if (event.getView().getTitle().equals("Start Mission?")) {
            event.setCancelled(true); // Prevent items from being moved
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                String choice = event.getCurrentItem().getItemMeta().getDisplayName();
                if (choice.equals("Yes")) {
                    String missionName = player.getMetadata("selectedMission").get(0).asString();
                    assignMissionToPlayer(player, missionName);
                }
                player.closeInventory();
                openMissionMenu(player);
            }
        } else if (event.getView().getTitle().equals("Mission Archive")) {
            event.setCancelled(true); // Prevent items from being moved
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
                player.sendTitle("Mission Assigned", mission.getName(), 10, 70, 20);
                return;
            }
        }
    }

    private Mission createMissionFromConfig(String missionType) {
        String path = "missions." + missionType + ".";
        String name = configManager.getConfig().getString(path + "name");
        String description = configManager.getConfig().getString(path + "description");
        String actionStr = configManager.getConfig().getString(path + "action");
        String targetStr = configManager.getConfig().getString(path + "target");
        int amount = configManager.getConfig().getInt(path + "amount");
        String rewardTypeStr = configManager.getConfig().getString(path + "reward.type");
        double rewardMoney = rewardTypeStr.equalsIgnoreCase("MONEY") ? configManager.getConfig().getDouble(path + "reward.amount") : 0;
        String rewardItemStr = rewardTypeStr.equalsIgnoreCase("ITEM") ? configManager.getConfig().getString(path + "reward.item") : null;
        int rewardItemAmount = rewardTypeStr.equalsIgnoreCase("ITEM") ? configManager.getConfig().getInt(path + "reward.itemAmount") : 0;
        Material iconMaterial = Material.getMaterial(configManager.getConfig().getString(path + "icon").toUpperCase());
        Material target = targetStr.isEmpty() ? null : Material.getMaterial(targetStr.toUpperCase());

        if (name != null && description != null && actionStr != null && rewardTypeStr != null && iconMaterial != null) {
            Mission.ActionType action = Mission.ActionType.valueOf(actionStr.toUpperCase());
            Mission.RewardType rewardType = Mission.RewardType.valueOf(rewardTypeStr.toUpperCase());
            ItemStack rewardItem = rewardType == Mission.RewardType.ITEM ? new ItemStack(Material.valueOf(rewardItemStr.toUpperCase()), rewardItemAmount) : null;
            return new Mission(name, description, action, target, amount, rewardType, rewardMoney, rewardItem, iconMaterial);
        }
        return null;
    }
}
