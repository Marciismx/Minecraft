package com.marc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;

public class DatabaseManager {
    private Connection connection;

    public void connect(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    public void saveUserData(UserData userData) throws SQLException {
        String sql = "REPLACE INTO user_data (uuid, experience, balance, completed_missions) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userData.getUuid().toString());
            stmt.setInt(2, userData.getExperience());
            stmt.setDouble(3, userData.getBalance());
            stmt.setString(4, String.join(",", userData.getCompletedMissions()));
            stmt.executeUpdate();
        }
    }

    public UserData loadUserData(UUID uuid) throws SQLException {
        String sql = "SELECT * FROM user_data WHERE uuid = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int experience = rs.getInt("experience");
                double balance = rs.getDouble("balance");
                List<String> completedMissions = Arrays.asList(rs.getString("completed_missions").split(","));
                return new UserData(uuid, experience, balance, completedMissions);
            }
        }
        return null;
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
