package com.marc.Mission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays; // Add this import statement

public class MissionCommand implements CommandExecutor {
    private MissionManager missionManager;

    public MissionCommand(MissionManager missionManager) {
        this.missionManager = missionManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1 && args[0].equalsIgnoreCase("add")) {
            String name = args[1];
            String description = String.join(" ", Arrays.asList(args).subList(2, args.length));
            Mission mission = new Mission(name, description);
            missionManager.addMission(mission);
            sender.sendMessage("Mission added: " + name);
            return true;
        }
        return false;
    }
}