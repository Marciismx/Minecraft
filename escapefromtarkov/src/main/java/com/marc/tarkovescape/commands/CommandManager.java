package com.marc.tarkovescape.commands;

import com.marc.tarkovescape.TarkovEscape;
import com.marc.tarkovescape.data.LootTableManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandManager implements CommandExecutor {

    private final TarkovEscape plugin;
    private final LootTableManager lootTableManager;

    public CommandManager(TarkovEscape plugin) {
        this.plugin = plugin;
        this.lootTableManager = new LootTableManager(plugin);
        plugin.getCommand("tarkov").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dit commando kan alleen door een speler worden uitgevoerd.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                showHelp(player);
                break;
            case "loot":
                handleLootCommand(player);
                break;
            default:
                player.sendMessage("Onbekend commando. Gebruik /tarkov help voor een lijst met commando's.");
                break;
        }
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("TarkovEscape Commando's:");
        player.sendMessage("/tarkov help - Toon deze helpboodschap");
        player.sendMessage("/tarkov loot - Voer het loot commando uit");
        // Voeg hier meer commando's toe als nodig
    }

    private void handleLootCommand(Player player) {
        List<ItemStack> loot = lootTableManager.generateLoot();
        for (ItemStack item : loot) {
            player.getInventory().addItem(item);
        }
        player.sendMessage("Je hebt loot ontvangen!");
    }
}
