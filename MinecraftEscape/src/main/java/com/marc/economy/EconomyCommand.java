package com.marc.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyCommand implements CommandExecutor {

    private final EconomyManager economyManager;

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

        if (args.length == 0) {
            double balance = economyManager.getBalance(player);
            player.sendMessage("Your current balance is: " + balance);
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
            } else if (action.equalsIgnoreCase("set")) {
                economyManager.setBalance(player, amount);
                player.sendMessage("Set your balance to " + amount + ".");
                return true;
            } else if (action.equalsIgnoreCase("withdraw")) {
                if (economyManager.withdrawBalance(player, amount)) {
                    player.sendMessage("Withdrew " + amount + " from your balance.");
                } else {
                    player.sendMessage("Insufficient balance.");
                }
                return true;
            }
        }

        player.sendMessage("Invalid command usage.");
        return false;
    }
}
