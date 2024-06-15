package com.marc.leaderboard;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderboardManager {
    private Map<UUID, Integer> playerScores = new HashMap<>();

    public void addScore(Player player, int score) {
        playerScores.put(player.getUniqueId(), playerScores.getOrDefault(player.getUniqueId(), 0) + score);
    }

    public int getScore(Player player) {
        return playerScores.getOrDefault(player.getUniqueId(), 0);
    }

    public Map<UUID, Integer> getTopScores(int top) {
        return playerScores.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .limit(top)
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }
}
