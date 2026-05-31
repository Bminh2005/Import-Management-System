package com.app.modules.procurement.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.database.manager.DatabaseManager;
import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.RequestDetailItem;

public class ImportRequestDAO {
    private static final String FIND_REQUEST_SQL = "SELECT r.id, r.user_id, u.username, r.desired_date "
            + "FROM \"ImportRequest\" r "
            + "JOIN \"Users\" u ON r.user_id = u.id "
            + "WHERE r.id = ?";

    private static final String FIND_DETAILS_SQL = "SELECT rd.id, rd.quantity, md.id AS md_id, md.unit, "
            + "md.reference_price, m.merchandise_name "
            + "FROM \"RequestDetail\" rd "
            + "JOIN \"MerchandiseDetail\" md ON rd.merchandise_detail_id = md.id "
            + "JOIN \"Merchandise\" m ON md.merchandise_id = m.id "
            + "WHERE rd.request_id = ?";

    public Optional<ImportRequestInfo> findRequestById(long requestId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_REQUEST_SQL)) {
            stmt.setLong(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new ImportRequestInfo(
                            rs.getLong("id"),
                            rs.getLong("user_id"),
                            rs.getString("username"),
                            rs.getString("desired_date")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn ImportRequest", e);
        }
        return Optional.empty();
    }

    public List<RequestDetailItem> getRequestDetails(long requestId) {
        List<RequestDetailItem> items = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_DETAILS_SQL)) {
            stmt.setLong(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(new RequestDetailItem(
                            rs.getLong("id"),
                            rs.getLong("md_id"),
                            rs.getString("merchandise_name"),
                            rs.getString("unit"),
                            rs.getLong("quantity"),
                            rs.getDouble("reference_price")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn RequestDetail", e);
        }
        return items;
    }
}
