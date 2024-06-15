package com.marc.economy;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class EconomyManager {
    private HashMap<UUID, Double> balances = new HashMap<>();

    public double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }

    public void addBalance(Player player, double amount) {
        double currentBalance = getBalance(player);
        balances.put(player.getUniqueId(), currentBalance + amount);
    }

    public void subtractBalance(Player player, double amount) {
        double currentBalance = getBalance(player);
        balances.put(player.getUniqueId(), Math.max(0, currentBalance - amount));
    }
}
