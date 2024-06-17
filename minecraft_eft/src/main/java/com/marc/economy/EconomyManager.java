package com.marc.economy;

import java.util.HashMap;
import java.util.UUID;

public class EconomyManager {
    private HashMap<UUID, Double> balances;

    public EconomyManager() {
        this.balances = new HashMap<>();
    }

    public double getBalance(UUID player) {
        return balances.getOrDefault(player, 0.0);
    }

    public void addBalance(UUID player, double amount) {
        balances.put(player, getBalance(player) + amount);
    }

    public void subtractBalance(UUID player, double amount) {
        balances.put(player, getBalance(player) - amount);
    }
}
