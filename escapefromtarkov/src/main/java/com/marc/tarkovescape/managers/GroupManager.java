package com.marc.tarkovescape.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GroupManager {
    private final Map<Player, Set<Player>> groups = new HashMap<>();
    private final Map<Player, Player> invites = new HashMap<>();

    public boolean createGroup(Player leader) {
        if (groups.containsKey(leader)) {
            return false; // Leader is already in a group
        }
        groups.put(leader, new HashSet<>());
        return true;
    }

    public boolean invitePlayer(Player leader, Player invitee) {
        if (!groups.containsKey(leader) || groups.containsKey(invitee)) {
            return false; // Leader doesn't have a group or invitee is already in a group
        }
        invites.put(invitee, leader);
        return true;
    }

    public boolean acceptInvite(Player invitee) {
        Player leader = invites.get(invitee);
        if (leader == null || !groups.containsKey(leader)) {
            return false; // No invite found or leader no longer has a group
        }
        groups.get(leader).add(invitee);
        groups.put(invitee, groups.get(leader));
        invites.remove(invitee);
        return true;
    }
}
