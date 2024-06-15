package com.marc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class EscapeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("Starting escape sequence...");
            
            // Geef de speler een specifiek item
            ItemStack item = new ItemStack(Material.DIAMOND, 1);
            player.getInventory().addItem(item);
            player.sendMessage("You have received a diamond!");
            
            // Voeg hier meer escape-logica toe
            
            return true;
        } else {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }
    }
}
