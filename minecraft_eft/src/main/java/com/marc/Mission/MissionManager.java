package com.marc.Mission;

import java.util.ArrayList;
import java.util.List;

public class MissionManager {
    private List<Mission> missions;

    public MissionManager() {
        this.missions = new ArrayList<>();
    }

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public List<Mission> getMissions() {
        return missions;
    }
}