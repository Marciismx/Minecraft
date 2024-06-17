package com.marc.leaderboard;

import com.marc.economy.EconomyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LeaderboardCommand implements CommandExecutor {

    private final EconomyManager economyManager;

    public LeaderboardCommand(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("balance")) {
            showBalanceLeaderboard(sender);
            return true;
        }

        sender.sendMessage("Usage: /leaderboard balance");
        return false;
    }

    private void showBalanceLeaderboard(CommandSender sender) {
        Map<Player, Double> balances = economyManager.getBalances();
        List<Map.Entry<Player, Double>> sortedBalances = new ArrayList<>(balances.entrySet());
        sortedBalances.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        sender.sendMessage("Balance Leaderboard:");
        int rank = 1;
        for (Map.Entry<Player, Double> entry : sortedBalances) {
            sender.sendMessage(rank + ". " + entry.getKey().getName() + " - " + entry.getValue());
            rank++;
        }
    }
}
