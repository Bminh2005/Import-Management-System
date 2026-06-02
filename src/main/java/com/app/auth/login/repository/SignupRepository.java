package com.app.auth.login.repository;

import com.app.common.entity.User;
import com.app.common.exception.DatabaseOperationException;
import com.app.database.manager.DatabaseManager;

import java.sql.*;

public class SignupRepository {

    public User save(User newUser){
        String sql = """
                INSERT INTO users (username, password, role, full_name) VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        // Hàm isValid(timeout_in_seconds) để check kết nối có đang sống không
            if (conn == null || !conn.isValid(5)) {
                throw new RuntimeException("Kết nối database không hợp lệ");
            }

            ps.setString(1, newUser.getUsername());
            ps.setString(2, newUser.getPassword());
            ps.setString(3, newUser.getRole());
            ps.setString(4, newUser.getFullname());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return newUser;
            } else {
                throw new DatabaseOperationException("Lỗi khi lưu user");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Lỗi khi lưu user", e);
        }
    }
}
