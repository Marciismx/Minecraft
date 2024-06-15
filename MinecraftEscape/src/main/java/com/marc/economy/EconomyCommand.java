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
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                double balance = economyManager.getBalance(player);
                player.sendMessage("Your balance is: " + balance);
                return true;
            }
            if (args.length == 2) {
                String action = args[0];
                double amount;
                try {
                    amount = Double.parseDouble(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid amount.");
                    return false;
                }
                if (action.equalsIgnoreCase("add")) {
                    economyManager.addBalance(player, amount);
                    player.sendMessage("Added " + amount + " to your balance.");
                    return true;
                }
                if (action.equalsIgnoreCase("subtract")) {
                    economyManager.subtractBalance(player, amount);
                    player.sendMessage("Subtracted " + amount + " from your balance.");
                    return true;
                }
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return false;
    }
}
