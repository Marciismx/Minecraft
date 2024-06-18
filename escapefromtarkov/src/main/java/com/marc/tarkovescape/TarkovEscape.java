package com.marc.tarkovescape;

import com.marc.tarkovescape.commands.CommandManager;
import com.marc.tarkovescape.data.PlayerDataManager;
import com.marc.tarkovescape.listeners.LootSysteem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TarkovEscape extends JavaPlugin {
    private LootSysteem lootSysteem;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        saveDefaultConfig(); // Zorgt ervoor dat config.yml wordt geladen
        getLogger().info("TarkovEscape is ingeschakeld!");
        lootSysteem = new LootSysteem(this);
        getServer().getPluginManager().registerEvents(lootSysteem, this);

        // Initialize PlayerDataManager
        playerDataManager = new PlayerDataManager(this);

        // Register commands
        new CommandManager(this);

        // Initialize log systeem
        initializeLogSystem();
    }

    @Override
    public void onDisable() {
        getLogger().info("TarkovEscape is uitgeschakeld!");
        // Plugin opschoon logica hier
    }

    private void initializeLogSystem() {
        // Implementeer een log systeem voor het bijhouden van spelersgegevens
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                playerDataManager.loadPlayerData(event.getPlayer());
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                playerDataManager.savePlayerData(event.getPlayer());
            }
        }, this);
    }

    public LootSysteem getLootSysteem() {
        return lootSysteem;
    }
}
