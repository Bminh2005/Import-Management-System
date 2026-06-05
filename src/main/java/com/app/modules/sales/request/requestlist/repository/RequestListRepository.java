package com.app.modules.sales.request.requestlist.repository;

import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.request.requestlist.dto.RequestListRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository đọc dữ liệu cho màn danh sách yêu cầu nhập hàng.
 */
public class RequestListRepository {

    public List<RequestListRow> findAllSummaries() {
        List<RequestListRow> result = new ArrayList<>();
        String sql = "SELECT r.id, r.created_at::date AS created_date, r.status, "
                + "(SELECT COUNT(*)::int FROM \"RequestDetail\" d WHERE d.request_id = r.id) AS item_count "
                + "FROM \"ImportRequest\" r ORDER BY r.created_at DESC, r.id DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new RequestListRow(
                        String.valueOf(rs.getLong("id")),
                        rs.getString("created_date"),
                        rs.getInt("item_count"),
                        fromRequestEnum(rs.getString("status"))));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc danh sách yêu cầu", e);
        }
        return result;
    }

    private static String fromRequestEnum(String dbStatus) {
        if (dbStatus == null) return "pending";
        switch (dbStatus) {
            case "PENDING": return "pending";
            case "PROCESSING": return "processing";
            case "PROCESSED": return "completed";
            default: return dbStatus.toLowerCase();
        }
    }
}
