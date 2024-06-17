package com.marc.areas;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AreaCommand implements CommandExecutor {
    private AreaManager areaManager;

    public AreaCommand(AreaManager areaManager) {
        this.areaManager = areaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length < 1) {
            player.sendMessage("Usage: /area add <name>");
            return true;
        }

        if (args[0].equalsIgnoreCase("add") && args.length == 2) {
            String name = args[1];
            Location location = player.getLocation();
            areaManager.addArea(name, location);
            player.sendMessage("Area added: " + name);
            return true;
        }
        return false;
    }
}
