package com.app.auth.login.repository;

import com.app.common.entity.User;
import com.app.common.exception.DatabaseOperationException;
import com.app.database.manager.DatabaseManager;

import javax.swing.text.html.parser.Entity;
import java.sql.*;

public class LoginRepository {
    public User checkAccount(String username, String password, String role) {
        String sql = """
            SELECT id, full_name
            FROM "Users"
            WHERE username = ?
              AND password = ?
              AND role = CAST(? AS user_role)
            """;
        User user = new User(role, username, password, "");

        try (Connection conn = DatabaseManager.getConnection()) {

            if (conn == null || !conn.isValid(5)) {
                throw new RuntimeException("Connection không hợp lệ");
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, role);

                try (ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        user.setId(rs.getLong(1));
                        user.setFullname(rs.getString(2));
                        return user;
                    }
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Database Connection has some issue", e);
        }

        return null; // Không tìm thấy tài khoản
    }
}
