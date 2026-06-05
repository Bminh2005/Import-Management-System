package com.app.modules.sales.request.editrequest.service;

import com.app.modules.sales.request.editrequest.dto.RequestResponse;
import com.app.modules.sales.request.editrequest.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.editrequest.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class kiểm thử tự động cho RequestService sử dụng JUnit 5.
 * Tên đầy đủ (Full Name): com.app.modules.sales.request.editrequest.service.RequestServiceTest
 */
public class RequestServiceTest {

    private FakeRequestRepository repository;
    private RequestService service;

    @BeforeEach
    public void setUp() {
        // Khởi tạo repository giả lập và service dưới kiểm thử
        repository = new FakeRequestRepository();
        service = new RequestService(repository);
    }

    // ==========================================
    // KIỂM THỬ HỘP ĐEN (BLACK-BOX TESTING)
    // ==========================================

    /**
     * TC_BB_01: Cập nhật thành công yêu cầu nhập hàng ở trạng thái pending với danh sách mặt hàng hợp lệ.
     * Kiểm tra khả năng thêm mới, giữ nguyên/cập nhật và xóa bớt mặt hàng.
     */
    @Test
    public void testUpdateRequest_Success_TC_BB_01() {
        // GIVEN: Một yêu cầu đang ở trạng thái pending với 2 mặt hàng cũ ("I01", "I02")
        Request existingRequest = new Request("RQ001", "2026-06-01", "pending", "sales_member", "");
        existingRequest.getItems().add(new RequestItem("I01", "Sữa tươi Vinamilk", 100, "Thùng", "2026-06-10", "pending"));
        existingRequest.getItems().add(new RequestItem("I02", "Bột giặt Omo 5kg", 50, "Bao", "2026-06-12", "pending"));
        repository.addRequest(existingRequest);

        // WHEN: Nhận dữ liệu cập nhật DTO:
        // - "I01": được giữ lại nhưng cập nhật số lượng thành 120 và ngày thành "2026-06-15"
        // - "I03": là mặt hàng mới được thêm vào (số lượng 30, ngày "2026-06-18")
        // - "I02": bị xóa đi (không có trong danh sách cập nhật DTO)
        List<RequestItem> updatedItems = new ArrayList<>();
        updatedItems.add(new RequestItem("I01", "Sữa tươi Vinamilk", 120, "Thùng", "2026-06-15", "pending"));
        updatedItems.add(new RequestItem("I03", "Nước lau sàn Gift", 30, "Chai", "2026-06-18", "pending"));
        UpdateRequestDTO dto = new UpdateRequestDTO("RQ001", updatedItems);

        RequestResponse response = service.updateRequest(dto);

        // THEN:
        assertNotNull(response);
        assertEquals("RQ001", response.getCode());
        assertEquals("pending", response.getStatus());
        assertEquals(2, response.getItemCount());

        // Kiểm tra chi tiết các mặt hàng sau khi lưu
        RequestItem item01 = response.getItems().stream().filter(i -> i.getCode().equals("I01")).findFirst().orElse(null);
        RequestItem item03 = response.getItems().stream().filter(i -> i.getCode().equals("I03")).findFirst().orElse(null);
        RequestItem item02 = response.getItems().stream().filter(i -> i.getCode().equals("I02")).findFirst().orElse(null);

        assertNotNull(item01);
        assertEquals(120, item01.getQuantity());
        assertEquals("2026-06-15", item01.getDeliveryDate());

        assertNotNull(item03);
        assertEquals(30, item03.getQuantity());
        assertEquals("2026-06-18", item03.getDeliveryDate());

        // Hàng I02 phải bị xóa khỏi danh sách
        assertTrue(item02 == null);

        // Kiểm tra xem repository đã ghi nhận các hành vi gọi hàm chính xác
        assertTrue(repository.deletedItems.contains("RQ001:I02"));
        assertTrue(repository.insertedItems.stream().anyMatch(s -> s.startsWith("RQ001:I01")));
        assertTrue(repository.insertedItems.stream().anyMatch(s -> s.startsWith("RQ001:I03")));
    }

    /**
     * TC_BB_02: Cập nhật thất bại khi mã yêu cầu nhập hàng không tồn tại.
     * Kết quả kỳ vọng: Ném ra NoSuchElementException.
     */
    @Test
    public void testUpdateRequest_RequestNotFound_TC_BB_02() {
        // GIVEN: Repository không chứa mã yêu cầu "RQ999"
        UpdateRequestDTO dto = new UpdateRequestDTO("RQ999", new ArrayList<>());

        // WHEN & THEN:
        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> {
            service.updateRequest(dto);
        });
        assertEquals("Không tìm thấy yêu cầu RQ999", ex.getMessage());
    }

    /**
     * TC_BB_03: Cập nhật thất bại khi trạng thái yêu cầu là "processing".
     * Kết quả kỳ vọng: Ném ra IllegalStateException.
     */
    @Test
    public void testUpdateRequest_InvalidStatus_Processing_TC_BB_03() {
        // GIVEN: Yêu cầu "RQ002" ở trạng thái "processing"
        Request existingRequest = new Request("RQ002", "2026-06-01", "processing", "sales_member", "");
        repository.addRequest(existingRequest);

        UpdateRequestDTO dto = new UpdateRequestDTO("RQ002", new ArrayList<>());

        // WHEN & THEN:
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            service.updateRequest(dto);
        });
        assertEquals("Yêu cầu RQ002 đang ở trạng thái processing, không thể chỉnh sửa.", ex.getMessage());
    }

    /**
     * TC_BB_04: Cập nhật thất bại khi trạng thái yêu cầu là "completed".
     * Kết quả kỳ vọng: Ném ra IllegalStateException.
     */
    @Test
    public void testUpdateRequest_InvalidStatus_Completed_TC_BB_04() {
        // GIVEN: Yêu cầu "RQ003" ở trạng thái "completed"
        Request existingRequest = new Request("RQ003", "2026-06-01", "completed", "sales_member", "");
        repository.addRequest(existingRequest);

        UpdateRequestDTO dto = new UpdateRequestDTO("RQ003", new ArrayList<>());

        // WHEN & THEN:
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            service.updateRequest(dto);
        });
        assertEquals("Yêu cầu RQ003 đang ở trạng thái completed, không thể chỉnh sửa.", ex.getMessage());
    }

    /**
     * TC_BB_05: Cập nhật thành công khi người dùng xóa tất cả mặt hàng khỏi yêu cầu (danh sách DTO rỗng).
     * Kết quả kỳ vọng: Các mặt hàng cũ bị xóa hết, trả về yêu cầu trống.
     */
    @Test
    public void testUpdateRequest_EmptyItems_TC_BB_05() {
        // GIVEN: Yêu cầu "RQ004" đang ở trạng thái pending và có 1 mặt hàng cũ "I01"
        Request existingRequest = new Request("RQ004", "2026-06-01", "pending", "sales_member", "");
        existingRequest.getItems().add(new RequestItem("I01", "Sữa tươi Vinamilk", 100, "Thùng", "2026-06-10", "pending"));
        repository.addRequest(existingRequest);

        UpdateRequestDTO dto = new UpdateRequestDTO("RQ004", new ArrayList<>()); // danh sách trống

        // WHEN:
        RequestResponse response = service.updateRequest(dto);

        // THEN:
        assertNotNull(response);
        assertEquals(0, response.getItemCount());
        assertTrue(repository.deletedItems.contains("RQ004:I01"));
    }

    // ==========================================
    // KIỂM THỬ HỘP TRẮNG (WHITE-BOX TESTING - ĐỘ ĐO C1)
    // ==========================================

    /**
     * TC_WB_01: Phủ Nhánh 1.A (Trạng thái khác "pending" -> Ném IllegalStateException)
     */
    @Test
    public void testUpdateRequest_WhiteBox_Branch_1A() {
        // Thiết lập yêu cầu có trạng thái "processing" (khác "pending")
        Request req = new Request("RQ_WB01", "2026-06-01", "processing", "member", "");
        repository.addRequest(req);

        UpdateRequestDTO dto = new UpdateRequestDTO("RQ_WB01", new ArrayList<>());

        assertThrows(IllegalStateException.class, () -> service.updateRequest(dto));
    }

    /**
     * TC_WB_02: Phủ các Nhánh:
     * - 1.B (Trạng thái bằng "pending")
     * - 2.B (Vòng lặp duyệt các mặt hàng mới rỗng)
     * - 3.B (Vòng lặp duyệt các mặt hàng cũ rỗng)
     */
    @Test
    public void testUpdateRequest_WhiteBox_Branch_1B_2B_3B() {
        // Yêu cầu ở trạng thái pending, không có mặt hàng nào trong DB
        Request req = new Request("RQ_WB02", "2026-06-01", "pending", "member", "");
        repository.addRequest(req);

        // DTO rỗng
        UpdateRequestDTO dto = new UpdateRequestDTO("RQ_WB02", new ArrayList<>());

        RequestResponse response = service.updateRequest(dto);
        assertNotNull(response);
        assertEquals(0, response.getItemCount());
    }

    /**
     * TC_WB_03: Phủ các Nhánh:
     * - 1.B (Trạng thái bằng "pending")
     * - 2.A (Vòng lặp mới có phần tử)
     * - 3.A (Vòng lặp cũ có phần tử)
     * - 4.A (Mặt hàng cũ không được giữ lại -> Bị xóa)
     * - 4.B (Mặt hàng cũ được giữ lại -> Không bị xóa)
     */
    @Test
    public void testUpdateRequest_WhiteBox_Branch_AllLoopsAndConditions() {
        // Yêu cầu có 2 mặt hàng cũ: "I01" và "I02"
        Request req = new Request("RQ_WB03", "2026-06-01", "pending", "member", "");
        req.getItems().add(new RequestItem("I01", "Product 1", 10, "Box", "2026-06-10", "pending"));
        req.getItems().add(new RequestItem("I02", "Product 2", 20, "Box", "2026-06-10", "pending"));
        repository.addRequest(req);

        // DTO có 2 mặt hàng: "I01" (giữ lại) và "I03" (thêm mới)
        List<RequestItem> newItems = new ArrayList<>();
        newItems.add(new RequestItem("I01", "Product 1", 15, "Box", "2026-06-11", "pending"));
        newItems.add(new RequestItem("I03", "Product 3", 30, "Box", "2026-06-15", "pending"));
        UpdateRequestDTO dto = new UpdateRequestDTO("RQ_WB03", newItems);

        RequestResponse response = service.updateRequest(dto);

        // Xác nhận cấu trúc trả về
        assertNotNull(response);
        assertEquals(2, response.getItemCount());

        // Kiểm tra xem I02 có bị xóa (phủ 4.A) và I01 được giữ (phủ 4.B)
        assertTrue(response.getItems().stream().anyMatch(i -> i.getCode().equals("I01")));
        assertTrue(response.getItems().stream().anyMatch(i -> i.getCode().equals("I03")));
        assertTrue(response.getItems().stream().noneMatch(i -> i.getCode().equals("I02")));

        assertTrue(repository.deletedItems.contains("RQ_WB03:I02"));
    }

    /**
     * TC_WB_04: Nhánh đặc biệt - Không tìm thấy yêu cầu nhập hàng trong DB
     */
    @Test
    public void testUpdateRequest_WhiteBox_RequestNotFound() {
        UpdateRequestDTO dto = new UpdateRequestDTO("RQ_NOT_FOUND", new ArrayList<>());
        assertThrows(NoSuchElementException.class, () -> service.updateRequest(dto));
    }


    // ==========================================
    // FAKE REPOSITORY SUBCLASS FOR TESTING
    // ==========================================
    private static class FakeRequestRepository extends RequestRepository {
        
        private static class RequestMetadata {
            final String code;
            final String createdDate;
            final String status;
            final String createdBy;

            RequestMetadata(String code, String createdDate, String status, String createdBy) {
                this.code = code;
                this.createdDate = createdDate;
                this.status = status;
                this.createdBy = createdBy;
            }
        }

        private final Map<String, RequestMetadata> metadataMap = new HashMap<>();
        private final Map<String, List<RequestItem>> itemsMap = new HashMap<>();
        
        public final List<String> insertedItems = new ArrayList<>();
        public final List<String> updatedDeliveryDates = new ArrayList<>();
        public final List<String> deletedItems = new ArrayList<>();

        public FakeRequestRepository() {
            super(null); // Bỏ qua provider của database PostgreSQL
        }

        public void addRequest(Request request) {
            metadataMap.put(request.getCode(), new RequestMetadata(
                request.getCode(), request.getCreatedDate(), request.getStatus(), request.getCreatedBy()
            ));
            List<RequestItem> copyList = new ArrayList<>();
            for (RequestItem i : request.getItems()) {
                copyList.add(new RequestItem(i.getCode(), i.getName(), i.getQuantity(), i.getUnit(), i.getDeliveryDate(), i.getStatus()));
            }
            itemsMap.put(request.getCode(), copyList);
        }

        @Override
        public Optional<Request> findById(String code) {
            RequestMetadata meta = metadataMap.get(code);
            if (meta == null) {
                return Optional.empty();
            }
            
            // Xây dựng lại thực thể Request độc lập hoàn toàn để tránh ConcurrentModificationException
            Request req = new Request(meta.code, meta.createdDate, meta.status, meta.createdBy, "");
            List<RequestItem> storedItems = itemsMap.get(code);
            if (storedItems != null) {
                for (RequestItem i : storedItems) {
                    req.getItems().add(new RequestItem(i.getCode(), i.getName(), i.getQuantity(), i.getUnit(), i.getDeliveryDate(), i.getStatus()));
                }
            }
            return Optional.of(req);
        }

        @Override
        public void insertItem(String requestCode, RequestItem item, int position) {
            insertedItems.add(requestCode + ":" + item.getCode() + ":" + item.getQuantity() + ":" + position);
            List<RequestItem> storedItems = itemsMap.computeIfAbsent(requestCode, k -> new ArrayList<>());
            // Xóa item cũ nếu trùng mã để giả lập upsert
            storedItems.removeIf(i -> i.getCode().equals(item.getCode()));
            storedItems.add(new RequestItem(item.getCode(), item.getName(), item.getQuantity(), item.getUnit(), item.getDeliveryDate(), item.getStatus()));
        }

        @Override
        public void updateItemDeliveryDate(String requestCode, String itemCode, String date) {
            updatedDeliveryDates.add(requestCode + ":" + itemCode + ":" + date);
            List<RequestItem> storedItems = itemsMap.get(requestCode);
            if (storedItems != null) {
                for (RequestItem i : storedItems) {
                    if (i.getCode().equals(itemCode)) {
                        i.setDeliveryDate(date);
                    }
                }
            }
        }

        @Override
        public void deleteItem(String requestCode, String itemCode) {
            deletedItems.add(requestCode + ":" + itemCode);
            List<RequestItem> storedItems = itemsMap.get(requestCode);
            if (storedItems != null) {
                storedItems.removeIf(i -> i.getCode().equals(itemCode));
            }
        }
    }
}
