package com.marc.economy;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    private Map<Player, Double> balances;

    public EconomyManager() {
        balances = new HashMap<>();
    }

    public Map<Player, Double> getBalances() {
        return balances;
    }

    public void addBalance(Player player, double amount) {
        balances.put(player, getBalance(player) + amount);
    }

    public double getBalance(Player player) {
        return balances.getOrDefault(player, 0.0);
    }

    public void setBalance(Player player, double amount) {
        balances.put(player, amount);
    }

    public boolean withdrawBalance(Player player, double amount) {
        double currentBalance = getBalance(player);
        if (currentBalance >= amount) {
            balances.put(player, currentBalance - amount);
            return true;
        }
        return false;
    }
}
