package com.marc.leaderboard;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LeaderboardManager {
    private HashMap<UUID, Integer> leaderboard;

    public LeaderboardManager() {
        this.leaderboard = new HashMap<>();
    }

    public void updateLeaderboard(UUID player, int score) {
        leaderboard.put(player, score);
    }

    public List<UUID> getTopPlayers(int topN) {
        return leaderboard.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(topN)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }
}
