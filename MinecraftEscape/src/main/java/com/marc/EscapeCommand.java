package com.marc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EscapeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("Starting escape sequence...");
            // Voeg hier je escape-logica toe
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
    }
}
