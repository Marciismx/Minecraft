package com.marc.storage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseMissionStorage implements MissionStorage {
    private String url;
    private String username;
    private String password;

    public DatabaseMissionStorage(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        // Initialize database
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS mission_progress (" +
                    "player_id VARCHAR(36) NOT NULL, " +
                    "mission_name VARCHAR(255) NOT NULL, " +
                    "completed BOOLEAN NOT NULL, " +
                    "PRIMARY KEY (player_id, mission_name))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveMissionProgress(UUID playerId, Map<String, Boolean> progress) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "REPLACE INTO mission_progress (player_id, mission_name, completed) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (Map.Entry<String, Boolean> entry : progress.entrySet()) {
                    pstmt.setString(1, playerId.toString());
                    pstmt.setString(2, entry.getKey());
                    pstmt.setBoolean(3, entry.getValue());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Boolean> loadMissionProgress(UUID playerId) {
        Map<String, Boolean> progress = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT mission_name, completed FROM mission_progress WHERE player_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, playerId.toString());
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        progress.put(rs.getString("mission_name"), rs.getBoolean("completed"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return progress;
    }
}
