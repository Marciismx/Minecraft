package com.marc.tarkovescape.commands;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    
    private final TarkovEscape plugin;

    public CommandManager(TarkovEscape plugin) {
        this.plugin = plugin;
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
            player.sendMessage("Gebruik /tarkov help voor een lijst met commando's.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                showHelp(player);
                break;
            case "loot":
                // Voeg hier de lootlogica toe
                player.sendMessage("Loot commando uitgevoerd.");
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
}
