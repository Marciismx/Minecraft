package com.marc.storage;

import com.marc.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseMissionStorage implements MissionStorage {

    private Main plugin;
    private Connection connection;

    public DatabaseMissionStorage(Main plugin) {
        this.plugin = plugin;
        this.connect();
    }

    private void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/missions.db");

            // Create table if not exists
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS mission_progress (" +
                    "player_uuid TEXT, " +
                    "mission_name TEXT, " +
                    "completed BOOLEAN, " +
                    "PRIMARY KEY (player_uuid, mission_name))");
            statement.execute();
            statement.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMissionProgress(UUID playerUUID, Map<String, Boolean> progress) {
        try {
            for (Map.Entry<String, Boolean> entry : progress.entrySet()) {
                PreparedStatement statement = connection.prepareStatement(
                        "REPLACE INTO mission_progress (player_uuid, mission_name, completed) VALUES (?, ?, ?)");
                statement.setString(1, playerUUID.toString());
                statement.setString(2, entry.getKey());
                statement.setBoolean(3, entry.getValue());
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Boolean> loadMissionProgress(UUID playerUUID) {
        Map<String, Boolean> progress = new HashMap<>();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT mission_name, completed FROM mission_progress WHERE player_uuid = ?");
            statement.setString(1, playerUUID.toString());
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                String missionName = results.getString("mission_name");
                boolean completed = results.getBoolean("completed");
                progress.put(missionName, completed);
            }

            results.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return progress;
    }

    @Override
    public void reload() {
        // Optional: Implement reload logic if needed
    }
}
