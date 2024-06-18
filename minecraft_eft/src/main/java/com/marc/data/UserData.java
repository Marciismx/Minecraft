package com.marc.data;

import java.util.List;
import java.util.UUID;

public class UserData {
    private UUID uuid;
    private int experience;
    private double balance;
    private List<String> completedMissions;

    public UserData(UUID uuid, int experience, double balance, List<String> completedMissions) {
        this.uuid = uuid;
        this.experience = experience;
        this.balance = balance;
        this.completedMissions = completedMissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getCompletedMissions() {
        return completedMissions;
    }

    public void setCompletedMissions(List<String> completedMissions) {
        this.completedMissions = completedMissions;
    }
}
