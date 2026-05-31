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
 * Repository CRUD cho Request — đọc/ghi trực tiếp các bảng thật trên
 * Supabase (Postgres) qua {@link DatabaseManager}. Theo quy ước README:
 * CHỈ làm CRUD, không có business logic.
 *
 * Ánh xạ entity ↔ bảng thật:
 *   Request       ← "ImportRequest" (+ "Users" cho người tạo)
 *   RequestItem   ← "RequestDetail" (+ "MerchandiseDetail" + "Merchandise")
 *   RejectedItem  ← "OrderDetail" có status='REFUSED' của các "Order" thuộc yêu cầu
 *   RelatedOrder  ← "Order" (+ "Site", tổng giá trị từ "OrderDetail")
 *
 * Lưu ý: mã item (RequestItem.code) chính là merchandise_detail_id.
 * Tên bảng PascalCase + "Order" (từ khóa) nên phải bọc dấu nháy kép.
 */
public class RequestRepository {

    // ----- READ -----

    /** Lấy toàn bộ yêu cầu (info + items + rejected + orders). */
    public Optional<Request> findById(String code) {
        Long id = tryParseId(code);
        if (id == null) return Optional.empty();
        try (Connection conn = DatabaseManager.getConnection()) {
            Request request = loadRequest(conn, id);
            if (request == null) return Optional.empty();
            loadItems(conn, request, id);
            loadRejectedItems(conn, request, id);
            loadRelatedOrders(conn, request, id);
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
                + "md.unit AS unit, COALESCE(r.desired_date::text, '') AS delivery_date "
                + "FROM \"RequestDetail\" d "
                + "JOIN \"MerchandiseDetail\" md ON md.id = d.merchandise_detail_id "
                + "JOIN \"Merchandise\" m ON m.id = md.merchandise_id "
                + "JOIN \"ImportRequest\" r ON r.id = d.request_id "
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

    private void loadRejectedItems(Connection conn, Request request, long id) throws SQLException {
        String sql = "SELECT od.merchandise_detail_id AS item_code, "
                + "m.merchandise_name AS name, od.quantity AS quantity, md.unit AS unit, "
                + "COALESCE(od.refused_reason, '') AS reason, "
                + "o.created_at::date AS rejected_date "
                + "FROM \"OrderDetail\" od "
                + "JOIN \"Order\" o ON o.id = od.order_id "
                + "JOIN \"MerchandiseDetail\" md ON md.id = od.merchandise_detail_id "
                + "JOIN \"Merchandise\" m ON m.id = md.merchandise_id "
                + "WHERE o.request_id = ? AND od.status = 'REFUSED'::order_status "
                + "ORDER BY o.created_at DESC, od.id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    request.getRejectedItems().add(new RejectedItem(
                            String.valueOf(rs.getLong("item_code")),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            "overseas",
                            rs.getString("reason"),
                            rs.getString("rejected_date")));
                }
            }
        }
    }

    private void loadRelatedOrders(Connection conn, Request request, long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(relatedOrderSelect("WHERE o.request_id = ? ORDER BY o.created_at, o.id"))) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    request.getOrders().add(mapRelatedOrder(rs));
                }
            }
        }
    }

    /** SELECT dùng chung cho RelatedOrder (info + đếm item + tổng giá trị). */
    private static String relatedOrderSelect(String tail) {
        return "SELECT o.id AS code, o.created_at::date AS order_date, "
                + "COALESCE(s.site_name, '') AS site, o.status AS status, o.request_id AS request_id, "
                + "(SELECT COUNT(*) FROM \"OrderDetail\" od WHERE od.order_id = o.id) AS item_count, "
                + "COALESCE((SELECT SUM(od.quantity * md.reference_price) "
                + "          FROM \"OrderDetail\" od "
                + "          JOIN \"MerchandiseDetail\" md ON md.id = od.merchandise_detail_id "
                + "          WHERE od.order_id = o.id), 0)::bigint AS total_value "
                + "FROM \"Order\" o "
                + "LEFT JOIN \"Site\" s ON s.id = o.site_id " + tail;
    }

    private RelatedOrder mapRelatedOrder(ResultSet rs) throws SQLException {
        return new RelatedOrder(
                String.valueOf(rs.getLong("code")),
                rs.getString("order_date"),
                rs.getString("site"),
                rs.getInt("item_count"),
                fromOrderEnum(rs.getString("status")),
                rs.getLong("total_value"));
    }

    // ----- WRITE -----

    /**
     * Cập nhật phần thông tin chung của 1 yêu cầu (không đụng items).
     * Trên bảng thật chỉ có status là đổi được (created_at/user là FK/auto),
     * và chỉ đổi nếu token map được sang enum request_status.
     */
    public void updateRequestInfo(Request request) {
        updateStatus(request.getCode(), request.getStatus());
    }

    /**
     * Chỉ cập nhật trạng thái yêu cầu. Token "cancelled"/"draft" KHÔNG có
     * trong enum request_status (PENDING/PROCESSING/PROCESSED) nên sẽ no-op
     * (không được phép sửa schema để thêm enum).
     */
    public void updateStatus(String code, String status) {
        Long id = tryParseId(code);
        String dbStatus = toRequestEnum(status);
        if (id == null || dbStatus == null) return;
        String sql = "UPDATE \"ImportRequest\" SET status = ?::request_status WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dbStatus);
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật trạng thái " + code, e);
        }
    }

    public void updateItemQuantity(String requestCode, String itemCode, int quantity) {
        Long reqId = tryParseId(requestCode);
        Long mdId = tryParseId(itemCode);
        if (reqId == null || mdId == null) return;
        String sql = "UPDATE \"RequestDetail\" SET quantity = ? "
                + "WHERE request_id = ? AND merchandise_detail_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, reqId);
            ps.setLong(3, mdId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật số lượng " + itemCode, e);
        }
    }

    /**
     * Ngày nhận hàng dùng chung cho cả yêu cầu (cột desired_date trên
     * "ImportRequest"), nên cập nhật theo yêu cầu — itemCode không dùng.
     */
    public void updateItemDeliveryDate(String requestCode, String itemCode, String date) {
        Long reqId = tryParseId(requestCode);
        if (reqId == null) return;
        String sql = "UPDATE \"ImportRequest\" SET desired_date = ?::date WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (date == null || date.isBlank()) ps.setNull(1, java.sql.Types.VARCHAR);
            else ps.setString(1, date.trim());
            ps.setLong(2, reqId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật ngày nhận " + itemCode, e);
        }
    }

    /**
     * Thêm/cập nhật 1 mặt hàng của yêu cầu (upsert theo
     * request_id + merchandise_detail_id). Không có unique constraint nên
     * thử UPDATE trước, nếu chưa có dòng nào thì INSERT. Cột id là IDENTITY
     * nên bỏ qua khi insert; bảng không có cột position.
     */
    public void insertItem(String requestCode, RequestItem item, int position) {
        Long reqId = tryParseId(requestCode);
        Long mdId = tryParseId(item.getCode());
        if (reqId == null || mdId == null) return;
        try (Connection conn = DatabaseManager.getConnection()) {
            String update = "UPDATE \"RequestDetail\" SET quantity = ? "
                    + "WHERE request_id = ? AND merchandise_detail_id = ?";
            int affected;
            try (PreparedStatement ps = conn.prepareStatement(update)) {
                ps.setInt(1, item.getQuantity());
                ps.setLong(2, reqId);
                ps.setLong(3, mdId);
                affected = ps.executeUpdate();
            }
            if (affected == 0) {
                String insert = "INSERT INTO \"RequestDetail\" "
                        + "(quantity, request_id, merchandise_detail_id) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insert)) {
                    ps.setInt(1, item.getQuantity());
                    ps.setLong(2, reqId);
                    ps.setLong(3, mdId);
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
        String sql = "DELETE FROM \"RequestDetail\" "
                + "WHERE request_id = ? AND merchandise_detail_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reqId);
            ps.setLong(2, mdId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa mặt hàng " + itemCode, e);
        }
    }

    /**
     * Danh sách sản phẩm khả dụng (master list) = toàn bộ "MerchandiseDetail"
     * kèm tên hàng. code = merchandise_detail_id.
     */
    public List<RequestItem> findAllProducts() {
        List<RequestItem> result = new ArrayList<>();
        String sql = "SELECT md.id AS item_code, m.merchandise_name AS name, md.unit AS unit "
                + "FROM \"MerchandiseDetail\" md "
                + "JOIN \"Merchandise\" m ON m.id = md.merchandise_id "
                + "ORDER BY md.id";
        try (Connection conn = DatabaseManager.getConnection();
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

    /** Tìm 1 đơn hàng liên quan theo mã đơn (kèm mã yêu cầu gốc). */
    public Optional<RelatedOrderWithRequest> findRelatedOrder(String orderCode) {
        Long orderId = tryParseId(orderCode);
        if (orderId == null) return Optional.empty();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(relatedOrderSelect("WHERE o.id = ?"))) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                RelatedOrder order = mapRelatedOrder(rs);
                long requestId = rs.getLong("request_id");
                String requestCode = rs.wasNull() ? "" : String.valueOf(requestId);
                return Optional.of(new RelatedOrderWithRequest(requestCode, order));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đọc đơn hàng " + orderCode, e);
        }
    }

    /** Đọc items của 1 yêu cầu (dùng để dựng item của đơn hàng). */
    public List<RequestItem> findRequestItems(String requestCode) {
        List<RequestItem> result = new ArrayList<>();
        Long reqId = tryParseId(requestCode);
        if (reqId == null) return result;
        String sql = "SELECT d.merchandise_detail_id AS item_code, "
                + "m.merchandise_name AS name, d.quantity AS quantity, md.unit AS unit "
                + "FROM \"RequestDetail\" d "
                + "JOIN \"MerchandiseDetail\" md ON md.id = d.merchandise_detail_id "
                + "JOIN \"Merchandise\" m ON m.id = md.merchandise_id "
                + "WHERE d.request_id = ? ORDER BY d.id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, reqId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new RequestItem(
                            String.valueOf(rs.getLong("item_code")),
                            rs.getString("name"),
                            rs.getInt("quantity"),
                            rs.getString("unit"),
                            "",
                            "pending"));
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

    /** Xóa 1 yêu cầu (xóa chi tiết trước để tránh vi phạm khóa ngoại). */
    public void deleteById(String code) {
        Long id = tryParseId(code);
        if (id == null) return;
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM \"RequestDetail\" WHERE request_id = ?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM \"ImportRequest\" WHERE id = ?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa yêu cầu " + code, e);
        }
    }

    // ----- Helpers: parse id + map enum trạng thái -----

    private static Long tryParseId(String code) {
        if (code == null) return null;
        try {
            return Long.parseLong(code.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** request_status (DB) → token UI cho StatusStyle. */
    private static String fromRequestEnum(String dbStatus) {
        if (dbStatus == null) return "pending";
        switch (dbStatus) {
            case "PENDING": return "pending";
            case "PROCESSING": return "processing";
            case "PROCESSED": return "completed";
            default: return dbStatus.toLowerCase();
        }
    }

    /** token UI → request_status (DB); null nếu không có enum tương ứng. */
    private static String toRequestEnum(String token) {
        if (token == null) return null;
        switch (token.toLowerCase()) {
            case "pending": return "PENDING";
            case "processing": return "PROCESSING";
            case "completed": return "PROCESSED";
            default: return null;
        }
    }

    /** order_status (DB) → token UI cho StatusStyle. */
    private static String fromOrderEnum(String dbStatus) {
        if (dbStatus == null) return "pending";
        switch (dbStatus) {
            case "PENDING": return "pending";
            case "PROCESSING": return "processing";
            case "ACCEPTED": return "completed";
            case "REFUSED": return "cancelled";
            default: return dbStatus.toLowerCase();
        }
    }
}
