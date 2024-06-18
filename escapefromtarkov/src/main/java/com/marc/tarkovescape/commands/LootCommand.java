package com.marc.tarkovescape.commands;

import com.marc.tarkovescape.TarkovEscape;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LootCommand implements CommandExecutor {
    private final TarkovEscape plugin;

    public LootCommand(TarkovEscape plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Alleen spelers kunnen dit commando gebruiken.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            player.sendMessage("/loot generate - Genereer loot op je locatie.");
            player.sendMessage("/loot help - Toon dit helpbericht.");
            return true;
        }

        if (args[0].equalsIgnoreCase("generate")) {
            // Logica om loot te genereren
            plugin.getLootSysteem().spawnLoot(player);
            player.sendMessage("Loot gegenereerd!");
            return true;
        }

        player.sendMessage("Onbekend commando. Gebruik /loot help voor hulp.");
        return true;
    }
}
