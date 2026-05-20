package com.app.modules.sales.request.repository;

import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Repository cho Request. Tạm thời lưu trong bộ nhớ; khi nối DB
 * thật chỉ cần thay phần findById / save / delete bằng SQL/JDBC.
 *
 * Quy ước theo README: repository CHỈ làm CRUD, không có business logic.
 */
public class RequestRepository {

    private final Map<String, Request> store = new HashMap<>();

    public RequestRepository() {
        seed();
    }

    /** SELECT theo mã yêu cầu. */
    public Optional<Request> findById(String code) {
        return Optional.ofNullable(store.get(code));
    }

    /** INSERT / UPDATE: ghi đè theo mã yêu cầu. */
    public void save(Request request) {
        if (request == null || request.getCode() == null) {
            return;
        }
        store.put(request.getCode(), request);
    }

    /** DELETE theo mã yêu cầu. */
    public void deleteById(String code) {
        store.remove(code);
    }

    // -------- Dữ liệu mẫu (sẽ bỏ khi nối DB) --------
    private void seed() {
        Request req = new Request("REQ-2024-001", "2024-05-08", "processing",
                "Nguyễn Văn A", "Trần Thị B - Bộ phận Đặt hàng Quốc tế");

        req.getItems().add(new RequestItem("MH001", "Laptop Dell XPS 13",
                2, "cái", "2024-05-15", "approved"));
        req.getItems().add(new RequestItem("MH002", "Bàn phím cơ Keychron K2",
                3, "cái", "2024-05-16", "pending"));
        req.getItems().add(new RequestItem("MH003", "Chuột Logitech MX Master 3",
                1, "cái", "2024-05-15", "pending"));

        req.getRejectedItems().add(new RejectedItem("MH004",
                "Màn hình LG UltraWide 34\"", 2, "cái", "overseas",
                "Không tìm thấy Site có đủ tồn kho", "2024-05-09"));
        req.getRejectedItems().add(new RejectedItem("MH005",
                "Webcam Logitech C920", 5, "cái", "user",
                "Đã đặt hàng từ nguồn khác", "2024-05-08"));

        req.getOrders().add(new RelatedOrder("DH-2024-101", "2024-05-09",
                "SITE01 - Kho Hong Kong", 2, "processing", 75000000L));
        req.getOrders().add(new RelatedOrder("DH-2024-102", "2024-05-10",
                "SITE02 - Kho Singapore", 1, "pending", 25000000L));
        req.getOrders().add(new RelatedOrder("DH-2024-103", "2024-05-11",
                "SITE03 - Kho Bangkok", 1, "completed", 30000000L));

        store.put(req.getCode(), req);
    }
}
