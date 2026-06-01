package com.app.modules.sales.request.editrequest.repository;

import com.app.database.connection.IDBProvider;
import com.app.database.connection.PostgreSQLProvider;
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
 * Repository CRUD cho Request — đọc/ghi trực tiếp bảng ImportRequest và RequestDetail.
 * Áp dụng SRP (chỉ lo dữ liệu yêu cầu + mặt hàng); IDBProvider được tiêm để dễ thay đổi nguồn DB.
 */
public class RequestRepository {

    private final IDBProvider dbProvider;

    public RequestRepository() {
        this(new PostgreSQLProvider());
    }

    public RequestRepository(IDBProvider dbProvider) {
        this.dbProvider = dbProvider;
    }

    // ----- READ -----

    /** Lấy 1 yêu cầu kèm danh sách mặt hàng. */
    public Optional<Request> findById(String code) {
        Long id = tryParseId(code);
        if (id == null) return Optional.empty();
        try (Connection conn = dbProvider.getConnection()) {
            Request request = loadRequest(conn, id);
            if (request == null) return Optional.empty();
            loadItems(conn, request, id);
            return Optional.of(request);
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc yêu cầu " + code, e);
        }
    }

    private Request loadRequest(Connection conn, long id) throws SQLException {
        String sql = "SELECT r.id, r.created_at::date AS created_date, r.status, "
                + "COALESCE(u.username, '') AS created_by "
                + "FROM \"ImportRequest\" r "
                + "LEFT JOIN \"Users\" u ON u.id = r.user_id "
                + "WHERE r.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Request(
                        String.valueOf(rs.getLong("id")),
                        rs.getString("created_date"),
                        fromRequestEnum(rs.getString("status")),
                        rs.getString("created_by"),
                        "");
            }
        }
    }

    private void loadItems(Connection conn, Request request, long id) throws SQLException {
        String itemStatus = "completed".equals(request.getStatus()) ? "approved" : "pending";
        String sql = "SELECT d.merchandise_detail_id AS item_code, "
                + "m.merchandise_name AS name, d.quantity AS quantity, "
                + "md.unit AS unit, COALESCE(d.desired_date::text, '') AS delivery_date "
                + "FROM \"RequestDetail\" d "
                + "JOIN \"MerchandiseDetail\" md ON md.id = d.merchandise_detail_id "
                + "JOIN \"Merchandise\" m ON m.id = md.merchandise_id "
                + "WHERE d.request_id = ? ORDER BY d.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    request.getItems().add(new RequestItem(
                            String.valueOf(rs.getLong("item_code")),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            rs.getString("delivery_date"),
                            itemStatus));
                }
            }
        }
    }

    // ----- WRITE -----

    /** Ngày nhận hàng của 1 mặt hàng trong yêu cầu. */
    public void updateItemDeliveryDate(String requestCode, String itemCode, String date) {
        Long reqId = tryParseId(requestCode);
        Long mdId = tryParseId(itemCode);
        if (reqId == null || mdId == null) return;
        String sql = "UPDATE \"RequestDetail\" SET desired_date = ?::date WHERE request_id = ? AND merchandise_detail_id = ?";
        try (Connection conn = dbProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setDateOrNull(ps, 1, date);
            ps.setLong(2, reqId);
            ps.setLong(3, mdId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật ngày nhận " + itemCode, e);
        }
    }

    /** Thêm/cập nhật 1 mặt hàng (upsert theo request_id + merchandise_detail_id). */
    public void insertItem(String requestCode, RequestItem item, int position) {
        Long reqId = tryParseId(requestCode);
        Long mdId = tryParseId(item.getCode());
        if (reqId == null || mdId == null) return;
        try (Connection conn = dbProvider.getConnection()) {
            String update = "UPDATE \"RequestDetail\" SET quantity = ?, desired_date = ?::date "
                    + "WHERE request_id = ? AND merchandise_detail_id = ?";
            int affected;
            try (PreparedStatement ps = conn.prepareStatement(update)) {
                ps.setInt(1, item.getQuantity());
                setDateOrNull(ps, 2, item.getDeliveryDate());
                ps.setLong(3, reqId);
                ps.setLong(4, mdId);
                affected = ps.executeUpdate();
            }
            if (affected == 0) {
                String insert = "INSERT INTO \"RequestDetail\" "
                        + "(quantity, desired_date, request_id, merchandise_detail_id) VALUES (?, ?::date, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    ps.setInt(1, item.getQuantity());
                    setDateOrNull(ps, 2, item.getDeliveryDate());
                    ps.setLong(3, reqId);
                    ps.setLong(4, mdId);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm mặt hàng " + item.getCode(), e);
        }
    }

    /** Xóa 1 mặt hàng khỏi yêu cầu. */
    public void deleteItem(String requestCode, String itemCode) {
        Long reqId = tryParseId(requestCode);
        Long mdId = tryParseId(itemCode);
        if (reqId == null || mdId == null) return;
        String sql = "DELETE FROM \"RequestDetail\" WHERE request_id = ? AND merchandise_detail_id = ?";
        try (Connection conn = dbProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reqId);
            ps.setLong(2, mdId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa mặt hàng " + itemCode, e);
        }
    }

    /** Danh sách toàn bộ sản phẩm khả dụng. */
    public List<RequestItem> findAllProducts() {
        List<RequestItem> result = new ArrayList<>();
        String sql = "SELECT md.id AS item_code, m.merchandise_name AS name, md.unit AS unit "
                + "FROM \"MerchandiseDetail\" md "
                + "JOIN \"Merchandise\" m ON m.id = md.merchandise_id "
                + "ORDER BY md.id";
        try (Connection conn = dbProvider.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new RequestItem(
                        String.valueOf(rs.getLong("item_code")),
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

    /** Gán ngày (yyyy-MM-dd) hoặc NULL cho tham số desired_date. */
    private static void setDateOrNull(PreparedStatement ps, int index, String date) throws SQLException {
        if (date == null || date.isBlank()) ps.setNull(index, java.sql.Types.VARCHAR);
        else ps.setString(index, date.trim());
    }

    /** Phân tích mã định danh chuỗi sang Long, trả null nếu không hợp lệ. */
    private static Long tryParseId(String code) {
        if (code == null) return null;
        try {
            return Long.parseLong(code.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Chuyển trạng thái yêu cầu từ DB sang định dạng hiển thị. */
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
