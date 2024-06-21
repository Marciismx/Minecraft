package com.marc.tarkovescape.commands;

import com.marc.tarkovescape.TarkovEscape;
import com.marc.tarkovescape.data.LootTableManager;
import com.marc.tarkovescape.managers.GroupManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CommandManager implements CommandExecutor {

    private final TarkovEscape plugin;
    private final LootTableManager lootTableManager;
    private final GroupManager groupManager;

    public CommandManager(TarkovEscape plugin) {
        this.plugin = plugin;
        this.lootTableManager = new LootTableManager(plugin);
        this.groupManager = plugin.getGroupManager();
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
            case "create":
                handleCreateCommand(player);
                break;
            case "invite":
                if (args.length < 2) {
                    player.sendMessage("Gebruik: /tarkov invite <spelernaam>");
                } else {
                    handleInviteCommand(player, args[1]);
                }
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
        player.sendMessage("/tarkov create - Maak een nieuwe groep");
        player.sendMessage("/tarkov invite <spelernaam> - Nodig een speler uit voor je groep");
        // Voeg hier meer commando's toe als nodig
    }

    private void handleLootCommand(Player player) {
        List<ItemStack> loot = lootTableManager.generateLoot();
        if (loot.isEmpty()) {
            player.sendMessage("Er is geen loot beschikbaar.");
            plugin.getLogger().warning("Geen loot gegenereerd voor speler " + player.getName());
        } else {
            for (ItemStack item : loot) {
                player.getInventory().addItem(item);
            }
            player.sendMessage("Je hebt loot ontvangen!");
            plugin.getLogger().info("Loot gegeven aan speler " + player.getName());
        }
    }

    private void handleCreateCommand(Player player) {
        if (groupManager.createGroup(player)) {
            player.sendMessage("Je hebt een nieuwe groep aangemaakt.");
        } else {
            player.sendMessage("Je zit al in een groep.");
        }
    }

    private void handleInviteCommand(Player player, String targetName) {
        Player target = plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage("Speler niet gevonden.");
            return;
        }
        if (groupManager.invitePlayer(player, target)) {
            player.sendMessage("Je hebt " + targetName + " uitgenodigd voor je groep.");
            target.sendMessage("Je bent uitgenodigd voor een groep door " + player.getName() + ". Gebruik /tarkov accept om te accepteren.");
        } else {
            player.sendMessage("Kon " + targetName + " niet uitnodigen voor je groep.");
        }
    }
}
