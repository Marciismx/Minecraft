package com.marc.leaderboard;

import com.marc.economy.EconomyManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.Map.Entry;

public class LeaderboardManager {

    private final EconomyManager economyManager;

    public LeaderboardManager(EconomyManager economyManager) {
        this.economyManager = economyManager;
    }

    public List<Entry<Player, Double>> getTopBalances(int topN) {
        Map<Player, Double> balances = economyManager.getBalances();
        List<Entry<Player, Double>> sortedBalances = new ArrayList<>(balances.entrySet());
        sortedBalances.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return sortedBalances.subList(0, Math.min(topN, sortedBalances.size()));
    }

    public void showLeaderboard(CommandSender sender, int topN) {
        List<Entry<Player, Double>> topBalances = getTopBalances(topN);
        sender.sendMessage("Balance Leaderboard:");
        int rank = 1;
        for (Entry<Player, Double> entry : topBalances) {
            sender.sendMessage(rank + ". " + entry.getKey().getName() + " - " + entry.getValue());
            rank++;
        }
    }
}
