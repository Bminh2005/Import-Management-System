package com.app.modules.sales.request.repository;

import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository CRUD cho Request — đọc/ghi trực tiếp Supabase (Postgres)
 * qua {@link DatabaseManager}. Theo quy ước README: CHỈ làm CRUD,
 * không có business logic.
 */
public class RequestRepository {

    // ----- READ -----

    /** Lấy toàn bộ yêu cầu (info + items + rejected + orders). */
    public Optional<Request> findById(String code) {
        try (Connection conn = DatabaseManager.getConnection()) {
            Request request = loadRequest(conn, code);
            if (request == null) return Optional.empty();
            loadItems(conn, request);
            loadRejectedItems(conn, request);
            loadRelatedOrders(conn, request);
            return Optional.of(request);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc yêu cầu " + code, e);
        }
    }

    private Request loadRequest(Connection conn, String code) throws SQLException {
        String sql = "SELECT code, created_date, status, created_by, assigned_to "
                + "FROM sales_request WHERE code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Request(
                        rs.getString("code"),
                        rs.getString("created_date"),
                        rs.getString("status"),
                        rs.getString("created_by"),
                        rs.getString("assigned_to"));
            }
        }
    }

    private void loadItems(Connection conn, Request request) throws SQLException {
        String sql = "SELECT item_code, name, quantity, unit, delivery_date, status "
                + "FROM sales_request_item WHERE request_code = ? "
                + "ORDER BY position, item_code";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getCode());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    request.getItems().add(new RequestItem(
                            rs.getString("item_code"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            rs.getString("delivery_date"),
                            rs.getString("status")));
                }
            }
        }
    }

    private void loadRejectedItems(Connection conn, Request request) throws SQLException {
        String sql = "SELECT item_code, name, quantity, unit, rejected_by, reason, rejected_date "
                + "FROM sales_request_rejected_item WHERE request_code = ? "
                + "ORDER BY rejected_date DESC, item_code";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getCode());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    request.getRejectedItems().add(new RejectedItem(
                            rs.getString("item_code"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            rs.getString("rejected_by"),
                            rs.getString("reason"),
                            rs.getString("rejected_date")));
                }
            }
        }
    }

    private void loadRelatedOrders(Connection conn, Request request) throws SQLException {
        String sql = "SELECT code, order_date, site, item_count, status, total_value "
                + "FROM sales_related_order WHERE request_code = ? "
                + "ORDER BY order_date, code";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getCode());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    request.getOrders().add(new RelatedOrder(
                            rs.getString("code"),
                            rs.getString("order_date"),
                            rs.getString("site"),
                            rs.getInt("item_count"),
                            rs.getString("status"),
                            rs.getLong("total_value")));
                }
            }
        }
    }

    // ----- WRITE -----

    /** Cập nhật phần thông tin chung của 1 yêu cầu (không đụng items). */
    public void updateRequestInfo(Request request) {
        String sql = "UPDATE sales_request SET created_date = ?, status = ?, "
                + "created_by = ?, assigned_to = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, request.getCreatedDate());
            ps.setString(2, request.getStatus());
            ps.setString(3, request.getCreatedBy());
            ps.setString(4, request.getAssignedTo());
            ps.setString(5, request.getCode());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật yêu cầu " + request.getCode(), e);
        }
    }

    /** Chỉ cập nhật trạng thái (dùng cho hủy yêu cầu). */
    public void updateStatus(String code, String status) {
        String sql = "UPDATE sales_request SET status = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật trạng thái " + code, e);
        }
    }

    public void updateItemQuantity(String requestCode, String itemCode, int quantity) {
        String sql = "UPDATE sales_request_item SET quantity = ? "
                + "WHERE request_code = ? AND item_code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setString(2, requestCode);
            ps.setString(3, itemCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật số lượng " + itemCode, e);
        }
    }

    public void updateItemDeliveryDate(String requestCode, String itemCode, String date) {
        String sql = "UPDATE sales_request_item SET delivery_date = ? "
                + "WHERE request_code = ? AND item_code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, date);
            ps.setString(2, requestCode);
            ps.setString(3, itemCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật ngày nhận " + itemCode, e);
        }
    }

    /** Thêm 1 mặt hàng mới vào yêu cầu. */
    public void insertItem(String requestCode, RequestItem item, int position) {
        String sql = "INSERT INTO sales_request_item "
                + "(request_code, item_code, name, quantity, unit, delivery_date, status, position) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT (request_code, item_code) DO UPDATE SET "
                + "name = EXCLUDED.name, quantity = EXCLUDED.quantity, "
                + "unit = EXCLUDED.unit, delivery_date = EXCLUDED.delivery_date, "
                + "status = EXCLUDED.status, position = EXCLUDED.position";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestCode);
            ps.setString(2, item.getCode());
            ps.setString(3, item.getName());
            ps.setInt(4, item.getQuantity());
            ps.setString(5, item.getUnit());
            ps.setString(6, item.getDeliveryDate());
            ps.setString(7, item.getStatus());
            ps.setInt(8, position);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm mặt hàng " + item.getCode(), e);
        }
    }

    /** Xóa 1 mặt hàng khỏi yêu cầu. */
    public void deleteItem(String requestCode, String itemCode) {
        String sql = "DELETE FROM sales_request_item "
                + "WHERE request_code = ? AND item_code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestCode);
            ps.setString(2, itemCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa mặt hàng " + itemCode, e);
        }
    }

    /**
     * Danh sách sản phẩm có sẵn (master list) — tổng hợp từ tất cả items
     * đã từng xuất hiện trong các yêu cầu / mặt hàng bị từ chối.
     * Mỗi sản phẩm chỉ trả 1 lần (DISTINCT theo item_code).
     */
    public List<RequestItem> findAllProducts() {
        List<RequestItem> result = new ArrayList<>();
        String sql = "SELECT item_code, MAX(name) AS name, MAX(unit) AS unit "
                + "FROM ("
                + "  SELECT item_code, name, unit FROM sales_request_item "
                + "  UNION ALL "
                + "  SELECT item_code, name, unit FROM sales_request_rejected_item "
                + ") AS t "
                + "GROUP BY item_code ORDER BY item_code";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new RequestItem(
                        rs.getString("item_code"),
                        rs.getString("name"),
                        0,
                        rs.getString("unit"),
                        "",
                        "pending"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc danh sách sản phẩm", e);
        }
        return result;
    }

    /** Tìm 1 đơn hàng liên quan theo mã đơn (kèm mã yêu cầu gốc). */
    public Optional<RelatedOrderWithRequest> findRelatedOrder(String orderCode) {
        String sql = "SELECT request_code, code, order_date, site, item_count, status, total_value "
                + "FROM sales_related_order WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                RelatedOrder order = new RelatedOrder(
                        rs.getString("code"),
                        rs.getString("order_date"),
                        rs.getString("site"),
                        rs.getInt("item_count"),
                        rs.getString("status"),
                        rs.getLong("total_value"));
                return Optional.of(new RelatedOrderWithRequest(
                        rs.getString("request_code"), order));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc đơn hàng " + orderCode, e);
        }
    }

    /** Đọc items của 1 yêu cầu (dùng để dựng item của đơn hàng demo). */
    public List<RequestItem> findRequestItems(String requestCode) {
        List<RequestItem> result = new ArrayList<>();
        String sql = "SELECT item_code, name, quantity, unit, delivery_date, status "
                + "FROM sales_request_item WHERE request_code = ? "
                + "ORDER BY position, item_code";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new RequestItem(
                            rs.getString("item_code"),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            rs.getString("delivery_date"),
                            rs.getString("status")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc items của " + requestCode, e);
        }
        return result;
    }

    /** Gói RelatedOrder kèm mã yêu cầu gốc. */
    public static class RelatedOrderWithRequest {
        private final String requestCode;
        private final RelatedOrder order;
        public RelatedOrderWithRequest(String requestCode, RelatedOrder order) {
            this.requestCode = requestCode;
            this.order = order;
        }
        public String getRequestCode() { return requestCode; }
        public RelatedOrder getOrder() { return order; }
    }

    /** Đổi trạng thái 1 đơn hàng liên quan (dùng cho hủy đơn). */
    public void updateRelatedOrderStatus(String orderCode, String status) {
        String sql = "UPDATE sales_related_order SET status = ? WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, orderCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật trạng thái đơn " + orderCode, e);
        }
    }

    public void deleteById(String code) {
        String sql = "DELETE FROM sales_request WHERE code = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa yêu cầu " + code, e);
        }
    }
}
