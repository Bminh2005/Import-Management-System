package com.app.modules.procurement.order.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.app.database.manager.DatabaseManager;
import com.app.modules.procurement.order.model.DeliveryType;
import com.app.modules.procurement.order.model.OrderStatus;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.model.SiteOrderItem;

public class SiteOrderDAO {
    private static final String BASE_ORDER_SQL = "SELECT o.id, o.created_at, o.site_id, s.site_name, "
            + "s.site_address, s.delivery_by_ship, s.delivery_by_air, "
            + "o.expected_delivery_date, o.user_id, u.username, o.request_id, o.status, o.delivery "
            + "FROM \"Order\" o "
            + "JOIN \"Site\" s ON o.site_id = s.id "
            + "JOIN \"Users\" u ON o.user_id = u.id ";

    private static final String FIND_ALL_SQL = BASE_ORDER_SQL + "ORDER BY o.created_at DESC";
    private static final String FIND_BY_STATUS_SQL = BASE_ORDER_SQL + "WHERE o.status = ? ORDER BY o.created_at DESC";
    private static final String FIND_BY_ID_SQL = BASE_ORDER_SQL + "WHERE o.id = ?";
    private static final String COUNT_BY_STATUS_SQL = "SELECT status, COUNT(*) AS total FROM \"Order\" GROUP BY status";
    private static final String INSERT_ORDER_SQL = "INSERT INTO \"Order\" "
            + "(site_id, expected_delivery_date, user_id, request_id, status, delivery) "
            + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private static final String INSERT_ORDER_DETAIL_SQL = "INSERT INTO \"OrderDetail\" "
            + "(order_id, merchandise_detail_id, quantity, status, refused_reason) "
            + "VALUES (?, ?, ?, ?, ?)";

    public List<SiteOrder> getAll() {
        return queryOrders(FIND_ALL_SQL, null);
    }

    public List<SiteOrder> getByStatus(OrderStatus status) {
        if (status == null) {
            return getAll();
        }
        return queryOrders(FIND_BY_STATUS_SQL, status.name());
    }

    public SiteOrder getById(long id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_ID_SQL)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SiteOrder order = mapOrder(rs);
                    populateOrderItems(conn, order);
                    return order;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn Order", e);
        }
        return null;
    }

    public long save(SiteOrder order, List<SiteOrderItem> items) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement orderStmt = conn.prepareStatement(INSERT_ORDER_SQL)) {
                orderStmt.setLong(1, order.getSiteId());
                orderStmt.setDate(2, Date.valueOf(order.getExpectedDeliveryDate()));
                orderStmt.setLong(3, order.getUserId());
                if (order.getRequestId() > 0) {
                    orderStmt.setLong(4, order.getRequestId());
                } else {
                    orderStmt.setNull(4, Types.BIGINT);
                }
                orderStmt.setString(5, order.getStatus().name());
                orderStmt.setString(6, order.getDelivery().name());
                try (ResultSet rs = orderStmt.executeQuery()) {
                    if (rs.next()) {
                        long orderId = rs.getLong("id");
                        try (PreparedStatement itemStmt = conn.prepareStatement(INSERT_ORDER_DETAIL_SQL)) {
                            for (SiteOrderItem item : items) {
                                itemStmt.setLong(1, orderId);
                                itemStmt.setLong(2, item.getMerchandiseDetailId());
                                itemStmt.setLong(3, item.getQuantity());
                                itemStmt.setString(4, item.getStatus() == null ? OrderStatus.PENDING.name() : item.getStatus().name());
                                itemStmt.setString(5, item.getRefusedReason());
                                itemStmt.addBatch();
                            }
                            itemStmt.executeBatch();
                        }
                        conn.commit();
                        return orderId;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi lưu Order", e);
        }
        return -1;
    }

    public List<Long> replaceRefusedOrder(long sourceOrderId, Map<SiteOrder, List<SiteOrderItem>> replacementOrders) {
        if (sourceOrderId <= 0 || replacementOrders == null || replacementOrders.isEmpty()) {
            return List.of();
        }
        List<Long> createdIds = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try {
                for (Map.Entry<SiteOrder, List<SiteOrderItem>> entry : replacementOrders.entrySet()) {
                    List<SiteOrderItem> items = entry.getValue();
                    if (items == null || items.isEmpty()) {
                        continue;
                    }
                    createdIds.add(insertOrder(conn, entry.getKey(), items));
                }
                if (!createdIds.isEmpty()) {
                    deleteOrder(conn, sourceOrderId);
                }
                conn.commit();
                return createdIds;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi thay thế Order bị hủy", e);
        }
    }

    private long insertOrder(Connection conn, SiteOrder order, List<SiteOrderItem> items) throws SQLException {
        try (PreparedStatement orderStmt = conn.prepareStatement(INSERT_ORDER_SQL)) {
            orderStmt.setLong(1, order.getSiteId());
            orderStmt.setDate(2, Date.valueOf(order.getExpectedDeliveryDate()));
            orderStmt.setLong(3, order.getUserId());
            if (order.getRequestId() > 0) {
                orderStmt.setLong(4, order.getRequestId());
            } else {
                orderStmt.setNull(4, Types.BIGINT);
            }
            orderStmt.setString(5, order.getStatus().name());
            orderStmt.setString(6, order.getDelivery().name());
            try (ResultSet rs = orderStmt.executeQuery()) {
                if (rs.next()) {
                    long orderId = rs.getLong("id");
                    insertOrderItems(conn, orderId, items);
                    return orderId;
                }
            }
        }
        throw new SQLException("Không lấy được id Order vừa tạo");
    }

    private void insertOrderItems(Connection conn, long orderId, List<SiteOrderItem> items) throws SQLException {
        try (PreparedStatement itemStmt = conn.prepareStatement(INSERT_ORDER_DETAIL_SQL)) {
            for (SiteOrderItem item : items) {
                itemStmt.setLong(1, orderId);
                itemStmt.setLong(2, item.getMerchandiseDetailId());
                itemStmt.setLong(3, item.getQuantity());
                itemStmt.setString(4, item.getStatus() == null ? OrderStatus.PENDING.name() : item.getStatus().name());
                itemStmt.setString(5, item.getRefusedReason());
                itemStmt.addBatch();
            }
            itemStmt.executeBatch();
        }
    }

    private void deleteOrder(Connection conn, long orderId) throws SQLException {
        try (PreparedStatement detailStmt = conn.prepareStatement(
                "DELETE FROM \"OrderDetail\" WHERE order_id = ?")) {
            detailStmt.setLong(1, orderId);
            detailStmt.executeUpdate();
        }
        try (PreparedStatement orderStmt = conn.prepareStatement(
                "DELETE FROM \"Order\" WHERE id = ?")) {
            orderStmt.setLong(1, orderId);
            orderStmt.executeUpdate();
        }
    }

    public void deleteById(long orderId) {
        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement detailStmt = conn.prepareStatement(
                    "DELETE FROM \"OrderDetail\" WHERE order_id = ?")) {
                detailStmt.setLong(1, orderId);
                detailStmt.executeUpdate();
            }
            try (PreparedStatement orderStmt = conn.prepareStatement(
                    "DELETE FROM \"Order\" WHERE id = ?")) {
                orderStmt.setLong(1, orderId);
                orderStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi xóa Order", e);
        }
    }

    public void updateStatus(long orderId, OrderStatus status) {
        String sql = "UPDATE \"Order\" SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setLong(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi cập nhật trạng thái Order", e);
        }
    }

    public int countByStatus(OrderStatus status) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_BY_STATUS_SQL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("status");
                if (status != null && status.name().equals(name)) {
                    return rs.getInt("total");
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi đếm Order theo trạng thái", e);
        }
    }

    private List<SiteOrder> queryOrders(String sql, String status) {
        Map<Long, SiteOrder> orders = new LinkedHashMap<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (status != null) {
                stmt.setString(1, status);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SiteOrder order = mapOrder(rs);
                    orders.put(order.getId(), order);
                }
            }
            if (!orders.isEmpty()) {
                populateOrderItems(conn, orders.values());
            }
            return new ArrayList<>(orders.values());
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi khi truy vấn Orders", e);
        }
    }

    private void populateOrderItems(Connection conn, Collection<SiteOrder> orders) throws SQLException {
        if (orders.isEmpty()) {
            return;
        }
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        for (SiteOrder order : orders) {
            joiner.add(String.valueOf(order.getId()));
        }
        String sql = "SELECT od.id, od.order_id, od.merchandise_detail_id, md.unit, md.reference_price, "
                + "m.merchandise_name, od.quantity, od.refused_reason, od.status "
                + "FROM \"OrderDetail\" od "
                + "JOIN \"MerchandiseDetail\" md ON od.merchandise_detail_id = md.id "
                + "JOIN \"Merchandise\" m ON md.merchandise_id = m.id "
                + "WHERE od.order_id IN " + joiner;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            Map<Long, SiteOrder> orderMap = new LinkedHashMap<>();
            for (SiteOrder order : orders) {
                orderMap.put(order.getId(), order);
            }
            while (rs.next()) {
                long orderId = rs.getLong("order_id");
                SiteOrder order = orderMap.get(orderId);
                if (order == null) {
                    continue;
                }
                SiteOrderItem item = new SiteOrderItem();
                item.setId(rs.getLong("id"));
                item.setOrderId(orderId);
                item.setMerchandiseDetailId(rs.getLong("merchandise_detail_id"));
                item.setMerchandiseName(rs.getString("merchandise_name"));
                item.setUnit(rs.getString("unit"));
                item.setQuantity(rs.getLong("quantity"));
                item.setPrice(rs.getDouble("reference_price"));
                item.setRefusedReason(rs.getString("refused_reason"));
                item.setStatus(parseOrderStatus(rs.getString("status")));
                order.getItems().add(item);
            }
        }
    }

    private void populateOrderItems(Connection conn, SiteOrder order) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT od.id, od.order_id, od.merchandise_detail_id, md.unit, md.reference_price, "
                        + "m.merchandise_name, od.quantity, od.refused_reason, od.status "
                        + "FROM \"OrderDetail\" od "
                        + "JOIN \"MerchandiseDetail\" md ON od.merchandise_detail_id = md.id "
                        + "JOIN \"Merchandise\" m ON md.merchandise_id = m.id "
                        + "WHERE od.order_id = ?")) {
            stmt.setLong(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SiteOrderItem item = new SiteOrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(order.getId());
                    item.setMerchandiseDetailId(rs.getLong("merchandise_detail_id"));
                    item.setMerchandiseName(rs.getString("merchandise_name"));
                    item.setUnit(rs.getString("unit"));
                    item.setQuantity(rs.getLong("quantity"));
                    item.setPrice(rs.getDouble("reference_price"));
                    item.setRefusedReason(rs.getString("refused_reason"));
                    item.setStatus(parseOrderStatus(rs.getString("status")));
                    order.getItems().add(item);
                }
            }
        }
    }

    private SiteOrder mapOrder(ResultSet rs) throws SQLException {
        SiteOrder order = new SiteOrder();
        order.setId(rs.getLong("id"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        order.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : LocalDateTime.now());
        order.setSiteId(rs.getLong("site_id"));
        order.setSiteName(rs.getString("site_name"));
        order.setSiteAddress(rs.getString("site_address"));
        order.setDeliveryByShip(rs.getLong("delivery_by_ship"));
        order.setDeliveryByAir(rs.getLong("delivery_by_air"));
        Date expected = rs.getDate("expected_delivery_date");
        order.setExpectedDeliveryDate(expected != null ? expected.toLocalDate() : LocalDate.now());
        order.setUserId(rs.getLong("user_id"));
        order.setOrdererName(rs.getString("username"));
        long requestId = rs.getLong("request_id");
        order.setRequestId(rs.wasNull() ? 0 : requestId);
        order.setStatus(parseOrderStatus(rs.getString("status")));
        order.setDelivery(parseDelivery(rs.getString("delivery")));
        return order;
    }

    private OrderStatus parseOrderStatus(String value) {
        if (value == null) {
            return OrderStatus.PENDING;
        }
        try {
            return OrderStatus.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return switch (value.toUpperCase()) {
                case "ACCEPTED" -> OrderStatus.ACCEPTED;
                case "REFUSED" -> OrderStatus.REFUSED;
                case "PROCESSING" -> OrderStatus.PROCESSING;
                default -> OrderStatus.PENDING;
            };
        }
    }

    private DeliveryType parseDelivery(String value) {
        if (value == null) {
            return DeliveryType.SHIP;
        }
        try {
            return DeliveryType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            return DeliveryType.SHIP;
        }
    }
}
