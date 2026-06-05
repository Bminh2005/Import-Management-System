package com.app.modules.warehouse.inbound.repository;

import com.app.database.manager.DatabaseManager;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InboundOrderRepository {
    private static final String STATUS_IMPORTED = "IMPORTED";
    private static final String STATUS_MISMATCH = "MISMATCH";
    private static final String STATUS_PROCESSING = "PROCESSING";

    public List<InboundOrderResponse> findAll() {
        ensureWarehouseSchema();
        String sql = """
                SELECT o.id,
                       o.site_id,
                       o.request_id,
                       o.expected_delivery_date,
                       COALESCE(o.actual_delivery_date::date::text, o.expected_delivery_date::text, '') AS received_date,
                       COALESCE(s.site_name, 'N/A') AS supplier,
                       o.status::text AS status,
                       COUNT(od.id) AS item_count,
                       COALESCE(SUM(od.quantity), 0) AS expected_quantity,
                       COALESCE(SUM(COALESCE(od.actual_quantity, 0)), 0) AS actual_quantity,
                       COALESCE(o.mismatch_reason, '') AS mismatch_reason
                FROM "Order" o
                LEFT JOIN "Site" s ON s.id = o.site_id
                LEFT JOIN "OrderDetail" od ON od.order_id = o.id
                GROUP BY o.id, o.site_id, o.request_id, o.expected_delivery_date, o.actual_delivery_date,
                         s.site_name, o.status, o.mismatch_reason
                ORDER BY o.expected_delivery_date DESC NULLS LAST, o.id DESC
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<InboundOrderResponse> orders = new ArrayList<>();
            while (resultSet.next()) {
                String statusCode = resultSet.getString("status");
                long orderId = resultSet.getLong("id");
                orders.add(new InboundOrderResponse(
                        orderId,
                        resultSet.getLong("site_id"),
                        buildOrderCode(orderId, resultSet.getString("expected_delivery_date")),
                        buildRequestCode(resultSet.getLong("request_id"), resultSet.getString("expected_delivery_date")),
                        resultSet.getString("received_date"),
                        resultSet.getString("expected_delivery_date"),
                        resultSet.getString("supplier"),
                        displayStatus(statusCode),
                        statusCode,
                        resultSet.getInt("item_count"),
                        resultSet.getInt("expected_quantity"),
                        resultSet.getInt("actual_quantity"),
                        resultSet.getString("mismatch_reason")
                ));
            }
            return orders;
        } catch (SQLException exception) {
            System.out.println("Nội dung chức năng: Không đọc được đơn nhập kho từ database - "
                    + exception.getMessage());
            return sampleOrders();
        }
    }

    public List<InboundOrderItemResponse> findItemsByOrderId(long orderId) {
        ensureWarehouseSchema();
        String sql = """
                SELECT od.id AS order_detail_id,
                       od.order_id,
                       od.merchandise_detail_id,
                       ('MD-' || od.merchandise_detail_id) AS product_code,
                       COALESCE(m.merchandise_name, 'N/A') AS product_name,
                       COALESCE(md.unit, '') AS unit,
                       COALESCE(od.quantity, 0) AS ordered_quantity,
                       COALESCE(od.actual_quantity, od.quantity, 0) AS actual_quantity,
                       COALESCE(od.refused_reason, '') AS discrepancy_reason
                FROM "OrderDetail" od
                LEFT JOIN "MerchandiseDetail" md ON md.id = od.merchandise_detail_id
                LEFT JOIN "Merchandise" m ON m.id = md.merchandise_id
                WHERE od.order_id = ?
                ORDER BY od.id
                """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<InboundOrderItemResponse> items = new ArrayList<>();
                while (resultSet.next()) {
                    items.add(new InboundOrderItemResponse(
                            resultSet.getLong("order_detail_id"),
                            resultSet.getLong("order_id"),
                            resultSet.getLong("merchandise_detail_id"),
                            resultSet.getString("product_code"),
                            resultSet.getString("product_name"),
                            resultSet.getString("unit"),
                            resultSet.getInt("ordered_quantity"),
                            resultSet.getInt("actual_quantity"),
                            resultSet.getString("discrepancy_reason")
                    ));
                }
                return items;
            }
        } catch (SQLException exception) {
            System.out.println("Nội dung chức năng: Không đọc được chi tiết đơn nhập kho - "
                    + exception.getMessage());
            return List.of();
        }
    }

    public void confirmInboundOrder(long orderId, List<InboundOrderItemResponse> items,
                                    String targetStatus, String mismatchReason, long inspectedBy) {
        ensureWarehouseSchema();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Đơn nhập kho không có mặt hàng để xác nhận.");
        }

        boolean hasMismatch = STATUS_MISMATCH.equals(targetStatus);
        if (!STATUS_IMPORTED.equals(targetStatus) && !STATUS_MISMATCH.equals(targetStatus)) {
            throw new IllegalArgumentException("Trang thai xac nhan nhap kho khong hop le.");
        }
        if (hasMismatch && (mismatchReason == null || mismatchReason.isBlank())) {
            throw new IllegalArgumentException("Bắt buộc nhập lý do sai lệch khi số lượng thực nhận không khớp.");
        }

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                OrderLock orderLock = lockOrder(connection, orderId);
                if (STATUS_IMPORTED.equals(orderLock.status()) || STATUS_MISMATCH.equals(orderLock.status())) {
                    throw new IllegalStateException("Đơn nhập kho đã được xác nhận, không thể cộng kho lần nữa.");
                }
                updateActualQuantities(connection, orderId, items);
                updateInventory(connection, orderLock.siteId(), items);
                updateOrderStatus(connection, orderId,
                        targetStatus,
                        hasMismatch ? mismatchReason : "",
                        inspectedBy);
                connection.commit();
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Không thể xác nhận đơn nhập kho.", exception);
        }
    }

    public void saveDraft(long orderId, List<InboundOrderItemResponse> items) {
        ensureWarehouseSchema();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Đơn nhập kho không có mặt hàng để lưu tạm.");
        }

        try (Connection connection = DatabaseManager.getConnection()) {
            connection.setAutoCommit(false);
            try {
                OrderLock orderLock = lockOrder(connection, orderId);
                if (STATUS_IMPORTED.equals(orderLock.status()) || STATUS_MISMATCH.equals(orderLock.status())) {
                    throw new IllegalStateException("Đơn nhập kho đã được xác nhận, không thể lưu tạm.");
                }
                updateActualQuantities(connection, orderId, items);
                updateOrderStatus(connection, orderId, STATUS_PROCESSING, null, null);
                connection.commit();
            } catch (SQLException | RuntimeException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Không thể lưu tạm đơn nhập kho.", exception);
        }
    }

    private void updateActualQuantities(Connection connection, long orderId,
                                        List<InboundOrderItemResponse> items) throws SQLException {
        String sql = """
                UPDATE "OrderDetail"
                SET actual_quantity = ?,
                    refused_reason = ?
                WHERE id = ? AND order_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (InboundOrderItemResponse item : items) {
                statement.setInt(1, item.getActualQuantity());
                statement.setString(2, item.hasMismatch() ? item.getDiscrepancyReason() : null);
                statement.setLong(3, item.getOrderDetailId());
                statement.setLong(4, orderId);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void updateOrderStatus(Connection connection, long orderId, String status,
                                   String mismatchReason, Long inspectedBy) throws SQLException {
        String sql = """
                UPDATE "Order"
                SET status = ?::order_status,
                    actual_delivery_date = CASE
                        WHEN ?::order_status IN ('IMPORTED'::order_status, 'MISMATCH'::order_status)
                        THEN CURRENT_TIMESTAMP
                        ELSE actual_delivery_date
                    END,
                    inspected_by = COALESCE(?, inspected_by),
                    mismatch_reason = COALESCE(?, mismatch_reason)
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setString(2, status);
            if (inspectedBy == null) {
                statement.setNull(3, java.sql.Types.BIGINT);
            } else {
                statement.setLong(3, inspectedBy);
            }
            statement.setString(4, mismatchReason);
            statement.setLong(5, orderId);
            statement.executeUpdate();
        }
    }

    private void updateInventory(Connection connection, long siteId,
                                 List<InboundOrderItemResponse> items) throws SQLException {
        for (InboundOrderItemResponse item : items) {
            Long inventoryId = findInventoryId(connection, siteId, item.getMerchandiseDetailId());
            if (inventoryId == null) {
                insertInventory(connection, siteId, item);
            } else {
                increaseInventory(connection, inventoryId, item.getActualQuantity());
            }
        }
    }

    private Long findInventoryId(Connection connection, long siteId, long merchandiseDetailId) throws SQLException {
        String sql = """
                SELECT id
                FROM "SiteInventory"
                WHERE site_id = ? AND merchandise_detail_id = ?
                ORDER BY id
                LIMIT 1
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, siteId);
            statement.setLong(2, merchandiseDetailId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getLong("id") : null;
            }
        }
    }

    private void insertInventory(Connection connection, long siteId,
                                 InboundOrderItemResponse item) throws SQLException {
        String sql = """
                INSERT INTO "SiteInventory" (site_id, merchandise_detail_id, quantity, price)
                VALUES (?, ?, ?, 0)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, siteId);
            statement.setLong(2, item.getMerchandiseDetailId());
            statement.setInt(3, item.getActualQuantity());
            statement.executeUpdate();
        }
    }

    private void increaseInventory(Connection connection, long inventoryId, int actualQuantity) throws SQLException {
        String sql = """
                UPDATE "SiteInventory"
                SET quantity = COALESCE(quantity, 0) + ?
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, actualQuantity);
            statement.setLong(2, inventoryId);
            statement.executeUpdate();
        }
    }

    private OrderLock lockOrder(Connection connection, long orderId) throws SQLException {
        String sql = "SELECT site_id, status::text AS status FROM \"Order\" WHERE id = ? FOR UPDATE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new OrderLock(resultSet.getLong("site_id"), resultSet.getString("status"));
                }
            }
        }
        throw new IllegalArgumentException("Không tìm thấy đơn nhập kho: " + orderId);
    }

    private record OrderLock(long siteId, String status) {
    }

    private void ensureWarehouseSchema() {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("ALTER TYPE order_status ADD VALUE IF NOT EXISTS 'IMPORTED'");
            statement.execute("ALTER TYPE order_status ADD VALUE IF NOT EXISTS 'MISMATCH'");
            statement.execute("""
                    ALTER TABLE "Order"
                    ADD COLUMN IF NOT EXISTS actual_delivery_date TIMESTAMPTZ
                    """);
            statement.execute("""
                    ALTER TABLE "Order"
                    ADD COLUMN IF NOT EXISTS inspected_by BIGINT REFERENCES "Users"(id)
                    """);
            statement.execute("""
                    ALTER TABLE "Order"
                    ADD COLUMN IF NOT EXISTS mismatch_reason TEXT
                    """);
            statement.execute("""
                    ALTER TABLE "OrderDetail"
                    ADD COLUMN IF NOT EXISTS actual_quantity BIGINT
                    """);
        } catch (SQLException exception) {
            System.out.println("Nội dung chức năng: Chưa đảm bảo được schema warehouse - "
                    + exception.getMessage());
        }
    }

    private String buildOrderCode(long orderId, String expectedDate) {
        String year = "2026";
        if (expectedDate != null && expectedDate.length() >= 4) {
            year = expectedDate.substring(0, 4);
        }
        return "ORD-" + year + "-" + String.format("%03d", orderId);
    }

    private String buildRequestCode(long requestId, String expectedDate) {
        if (requestId <= 0) {
            return "REQ-N/A";
        }
        String year = "2026";
        if (expectedDate != null && expectedDate.length() >= 4) {
            year = expectedDate.substring(0, 4);
        }
        return "REQ-" + year + "-" + String.format("%03d", requestId);
    }

    private String displayStatus(String statusCode) {
        if (STATUS_IMPORTED.equals(statusCode)) {
            return "Đã nhập kho";
        }
        if (STATUS_MISMATCH.equals(statusCode)) {
            return "Có sai lệch";
        }
        if ("PROCESSING".equals(statusCode)) {
            return "Đang xử lý";
        }
        return "Chờ xử lý";
    }

    private List<InboundOrderResponse> sampleOrders() {
        return List.of(
                new InboundOrderResponse(1, 1, "ORD-2026-001", "REQ-2026-001",
                        "2026-06-12", "2026-06-12", "Kho Tổng Miền Bắc",
                        "Chờ xử lý", "PENDING", 3, 15, 0, ""),
                new InboundOrderResponse(2, 2, "ORD-2026-002", "REQ-2026-002",
                        "2026-06-20", "2026-06-20", "Kho Vệ Tinh Miền Nam",
                        "Đang xử lý", "PROCESSING", 2, 15, 0, ""),
                new InboundOrderResponse(3, 3, "ORD-2026-003", "REQ-2026-003",
                        "2026-07-01", "2026-07-01", "Chi nhánh Đà Nẵng",
                        "Chờ xử lý", "PENDING", 0, 0, 0, "")
        );
    }
}
