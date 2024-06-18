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
        if (args.length == 0) {
            sender.sendMessage("Gebruik /tarkov help voor een lijst met commando's.");
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            showHelp(sender);
        } else if (args[0].equalsIgnoreCase("loot")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                // Voeg hier de lootlogica toe
                sender.sendMessage("Loot command uitgevoerd.");
            } else {
                sender.sendMessage("Dit commando kan alleen door een speler worden uitgevoerd.");
            }
        } else {
            sender.sendMessage("Onbekend commando. Gebruik /tarkov help voor een lijst met commando's.");
        }
        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("TarkovEscape Commando's:");
        sender.sendMessage("/tarkov help - Toon deze helpboodschap");
        sender.sendMessage("/tarkov loot - Voer het loot commando uit");
        // Voeg hier meer commando's toe als nodig
    }
}
