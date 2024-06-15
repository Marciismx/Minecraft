package com.marc.leaderboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaderboardCommand implements CommandExecutor {
    private LeaderboardManager leaderboardManager;

    public LeaderboardCommand(LeaderboardManager leaderboardManager) {
        this.leaderboardManager = leaderboardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("Your score: " + leaderboardManager.getScore(player));
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("top")) {
                player.sendMessage("Top Scores:");
                leaderboardManager.getTopScores(5).forEach((uuid, score) -> {
                    player.sendMessage(uuid.toString() + ": " + score);
                });
                return true;
            }
        } else {
            sender.sendMessage("This command can only be run by a player.");
        }
        return false;
    }
}
