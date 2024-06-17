package com.marc.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommand implements CommandExecutor {
    private EconomyManager economyManager;

    public EconomyCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("Usage: /balance or /balance add <amount> or /balance subtract <amount>");
            return true;
        }

        if (args[0].equalsIgnoreCase("add") && args.length == 2) {
            double amount = Double.parseDouble(args[1]);
            economyManager.addBalance(player.getUniqueId(), amount);
            player.sendMessage("Added " + amount + " to your balance.");
        } else if (args[0].equalsIgnoreCase("subtract") && args.length == 2) {
            double amount = Double.parseDouble(args[1]);
            economyManager.subtractBalance(player.getUniqueId(), amount);
            player.sendMessage("Subtracted " + amount + " from your balance.");
        } else {
            player.sendMessage("Your balance: " + economyManager.getBalance(player.getUniqueId()));
        }
        return true;
    }
}
