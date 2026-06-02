package com.app.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDBProvider {
    // Trả về một kết nối lấy từ Pool
    Connection getConnection() throws SQLException;
    // Đóng toàn bộ Connection Pool khi tắt ứng dụng
    void shutdown();
}
