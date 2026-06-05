package com.app.database.manager;

import com.app.database.connection.IDBProvider;
import com.app.database.connection.PostgreSQLProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static IDBProvider provider = new PostgreSQLProvider();

//    public static IDBProvider getProvider() {
//        return provider;
//    }
    public static Connection getConnection() throws SQLException {
        return provider.getConnection();
    }

    // Có thể viết thêm hàm uỷ quyền để đóng kết nối
    public static void shutdown() {
        provider.shutdown();
    }
    //SAMPLE FOR GET CONNECTION AND QUERY
//    try (Connection conn = DatabaseManager.getConnection()) {
//
//        // Hàm isValid(timeout_in_seconds) để check kết nối có đang sống không
//        if (conn != null && conn.isValid(5)) {
//            System.out.println("✅ BƯỚC 1: Lấy Connection từ Pool THÀNH CÔNG!");
//
//            // Chạy thử một câu truy vấn vô thưởng vô phạt
//            try (Statement stmt = conn.createStatement();
//                 ResultSet rs = stmt.executeQuery("SELECT 1")) {
//
//                if (rs.next()) {
//                    System.out.println("✅ BƯỚC 2: Truy vấn test (SELECT 1) trả về kết quả: " + rs.getInt(1));
//                    System.out.println("🚀 TẤT CẢ HOẠT ĐỘNG HOÀN HẢO! Database đã sẵn sàng.");
//                }
//            }
//        } else {
//            System.out.println("❌ KẾT NỐI THẤT BẠI: Connection bị null hoặc không hợp lệ.");
//        }
//
//    } catch (Exception e) {
//        System.err.println("❌ LỖI NGHIÊM TRỌNG: Không thể kết nối!");
//        System.err.println("Vui lòng kiểm tra lại:");
//        System.err.println("1. Database server (MySQL/PostgreSQL) đã bật chưa?");
//        System.err.println("2. URL, Username, Password đã điền đúng chưa?");
//        System.err.println("3. Đã tải đủ thư viện HikariCP, SLF4J, và JDBC Driver chưa?");
//        System.err.println("--- Chi tiết lỗi ---");
//        e.printStackTrace();
//    }
}
