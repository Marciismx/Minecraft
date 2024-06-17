package com.marc.leaderboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.List;
import java.util.UUID;

public class LeaderboardCommand implements CommandExecutor {
    private LeaderboardManager leaderboardManager;

    public LeaderboardCommand(LeaderboardManager leaderboardManager) {
        this.leaderboardManager = leaderboardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        List<UUID> topPlayers = leaderboardManager.getTopPlayers(10);
        player.sendMessage("Top 10 Players:");
        for (int i = 0; i < topPlayers.size(); i++) {
            player.sendMessage((i + 1) + ". " + topPlayers.get(i));
        }
        return true;
    }
}
