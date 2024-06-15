package com.marc.mission;

public class Mission {
    private String name;
    private String description;
    private double reward;

    public Mission(String name, String description, double reward) {
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getReward() {
        return reward;
    }
}
