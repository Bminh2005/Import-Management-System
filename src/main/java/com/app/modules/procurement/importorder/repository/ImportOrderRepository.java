package com.app.modules.procurement.importorder.repository;

import com.app.database.manager.DatabaseManager;
import com.app.common.entity.ImportRequest;
import com.app.common.entity.RequestDetail;
import com.app.common.entity.Order;
import com.app.common.entity.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImportOrderRepository {

    public record RequestSummary(
            long id,
            String code,
            String createdDate,
            String desiredDate,
            String createdBy,
            int itemCount,
            String priority,
            String status
    ) {}

    public record RequestDetailItem(
            long id,
            long merchandiseDetailId,
            String merchandiseName,
            int quantity,
            String unit,
            double referencePrice
    ) {}

    public record SiteInventoryInfo(
            long siteId,
            String siteName,
            double siteDistance,
            long deliveryByShip,
            long deliveryByAir,
            int availableQuantity,
            double price
    ) {}

    /**
     * Fetch all pending and processing import requests.
     */
    public List<RequestSummary> findPendingRequests() {
        List<RequestSummary> list = new ArrayList<>();
        String sql = "SELECT r.id, r.created_at, r.desired_date, r.status, u.username, " +
                     "  (SELECT COALESCE(SUM(quantity), 0) FROM \"RequestDetail\" rd WHERE rd.request_id = r.id) as item_count " +
                     "FROM \"ImportRequest\" r " +
                     "JOIN \"Users\" u ON r.user_id = u.id " +
                     "WHERE r.status IN ('PENDING'::request_status, 'PROCESSING'::request_status) " +
                     "ORDER BY r.created_at DESC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String code = "REQ-" + String.format("%03d", id);
                Timestamp createdAt = rs.getTimestamp("created_at");
                Date desiredDate = rs.getDate("desired_date");
                String status = rs.getString("status").toLowerCase();
                String createdBy = rs.getString("username");
                int itemCount = rs.getInt("item_count");

                // Determine a priority based on days remaining to desired_date
                String priority = "medium";
                if (desiredDate != null && createdAt != null) {
                    long diffMs = desiredDate.getTime() - createdAt.getTime();
                    long diffDays = diffMs / (1000 * 60 * 60 * 24);
                    if (diffDays <= 7) {
                        priority = "high";
                    } else if (diffDays >= 30) {
                        priority = "low";
                    }
                }

                list.add(new RequestSummary(
                        id,
                        code,
                        createdAt != null ? createdAt.toLocalDateTime().toLocalDate().toString() : "",
                        desiredDate != null ? desiredDate.toString() : "",
                        createdBy,
                        itemCount,
                        priority,
                        status
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Fetch a specific import request by ID.
     */
    public RequestSummary findRequestById(long id) {
        String sql = "SELECT r.id, r.created_at, r.desired_date, r.status, u.username, " +
                     "  (SELECT COALESCE(SUM(quantity), 0) FROM \"RequestDetail\" rd WHERE rd.request_id = r.id) as item_count " +
                     "FROM \"ImportRequest\" r " +
                     "JOIN \"Users\" u ON r.user_id = u.id " +
                     "WHERE r.id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String code = "REQ-" + String.format("%03d", id);
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    Date desiredDate = rs.getDate("desired_date");
                    String status = rs.getString("status").toLowerCase();
                    String createdBy = rs.getString("username");
                    int itemCount = rs.getInt("item_count");

                    String priority = "medium";
                    if (desiredDate != null && createdAt != null) {
                        long diffMs = desiredDate.getTime() - createdAt.getTime();
                        long diffDays = diffMs / (1000 * 60 * 60 * 24);
                        if (diffDays <= 7) {
                            priority = "high";
                        } else if (diffDays >= 30) {
                            priority = "low";
                        }
                    }

                    return new RequestSummary(
                            id,
                            code,
                            createdAt != null ? createdAt.toLocalDateTime().toLocalDate().toString() : "",
                            desiredDate != null ? desiredDate.toString() : "",
                            createdBy,
                            itemCount,
                            priority,
                            status
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch all items (details) of a specific import request.
     */
    public List<RequestDetailItem> getRequestDetails(long requestId) {
        List<RequestDetailItem> list = new ArrayList<>();
        String sql = "SELECT rd.id, rd.merchandise_detail_id, rd.quantity, md.unit, md.reference_price, m.merchandise_name " +
                     "FROM \"RequestDetail\" rd " +
                     "JOIN \"MerchandiseDetail\" md ON rd.merchandise_detail_id = md.id " +
                     "JOIN \"Merchandise\" m ON md.merchandise_id = m.id " +
                     "WHERE rd.request_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, requestId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new RequestDetailItem(
                            rs.getLong("id"),
                            rs.getLong("merchandise_detail_id"),
                            rs.getString("merchandise_name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            rs.getDouble("reference_price")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Get inventory information of a merchandise detail across all sites.
     */
    public List<SiteInventoryInfo> getSitesInventory(long merchandiseDetailId) {
        List<SiteInventoryInfo> list = new ArrayList<>();
        String sql = "SELECT si.site_id, si.quantity, si.price, s.site_name, s.site_distance, s.delivery_by_ship, s.delivery_by_air " +
                     "FROM \"SiteInventory\" si " +
                     "JOIN \"Site\" s ON si.site_id = s.id " +
                     "WHERE si.merchandise_detail_id = ? AND si.quantity > 0 " +
                     "ORDER BY s.site_distance ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, merchandiseDetailId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new SiteInventoryInfo(
                            rs.getLong("site_id"),
                            rs.getString("site_name"),
                            rs.getDouble("site_distance"),
                            rs.getLong("delivery_by_ship"),
                            rs.getLong("delivery_by_air"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Create a purchase order and return its generated ID.
     */
    public long createOrder(Connection conn, long siteId, String expectedDeliveryDate, long userId, long requestId, String deliveryType) throws SQLException {
        String sql = "INSERT INTO \"Order\" (site_id, expected_delivery_date, user_id, request_id, status, delivery) " +
                     "VALUES (?, ?::date, ?, ?, 'PENDING'::order_status, ?::delivery_type) " +
                     "RETURNING id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, siteId);
            stmt.setString(2, expectedDeliveryDate);
            stmt.setLong(3, userId);
            stmt.setLong(4, requestId);
            stmt.setString(5, deliveryType);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("Creating order failed, no ID obtained.");
    }

    /**
     * Create an order detail item.
     */
    public void createOrderDetail(Connection conn, long orderId, long merchandiseDetailId, int quantity) throws SQLException {
        String sql = "INSERT INTO \"OrderDetail\" (order_id, merchandise_detail_id, quantity, status) " +
                     "VALUES (?, ?, ?, 'PENDING'::order_status)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            stmt.setLong(2, merchandiseDetailId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    /**
     * Update the status of an ImportRequest.
     */
    public void updateRequestStatus(Connection conn, long requestId, String status) throws SQLException {
        String sql = "UPDATE \"ImportRequest\" SET status = ?::request_status WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.toUpperCase());
            stmt.setLong(2, requestId);
            stmt.executeUpdate();
        }
    }

    /**
     * Deduct inventory quantity at a specific site.
     */
    public void deductSiteInventory(Connection conn, long siteId, long merchandiseDetailId, int quantity) throws SQLException {
        String sql = "UPDATE \"SiteInventory\" SET quantity = quantity - ? " +
                     "WHERE site_id = ? AND merchandise_detail_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setLong(2, siteId);
            stmt.setLong(3, merchandiseDetailId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("SiteInventory update failed, no rows affected.");
            }
        }
    }
}
