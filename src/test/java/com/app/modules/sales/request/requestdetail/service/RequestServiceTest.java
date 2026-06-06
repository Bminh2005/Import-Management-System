package com.app.modules.sales.request.requestdetail.service;

import com.app.modules.sales.request.entity.OrderItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.Request;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.requestdetail.dto.OrderDetailResponse;
import com.app.modules.sales.request.requestdetail.repository.IRequestDetailRepository;
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
 * Kiểm thử tự động cho {@link RequestService#getOrderDetail(String)} — JUnit 5.
 * Full name: com.app.modules.sales.request.requestdetail.service.RequestServiceTest
 */
public class RequestServiceTest {

    private FakeRequestDetailRepository repository;
    private RequestService service;

    @BeforeEach
    void setUp() {
        repository = new FakeRequestDetailRepository();
        service = new RequestService(repository);
    }

    // ==========================================
    // KIỂM THỬ HỘP TRẮNG (WHITE-BOX — C1)
    // ==========================================

    /** TC_WB_01: Nhánh orElseThrow — đơn không tồn tại. */
    @Test
    void testGetOrderDetail_WhiteBox_OrderNotFound_TC_WB_01() {
        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> service.getOrderDetail("99999"));
        assertEquals("Không tìm thấy đơn hàng 99999", ex.getMessage());
    }

    /** TC_WB_02: Nhánh items.isEmpty() = false — dùng OrderDetail, không fallback. */
    @Test
    void testGetOrderDetail_WhiteBox_HasOrderItems_TC_WB_02() {
        repository.putOrder("1", relatedOrder("1", "1", 2),
                List.of(
                        new OrderItem("1", "Laptop Dell XPS 13", 10, "Cái"),
                        new OrderItem("3", "Màn hình ASUS ProArt 27 inch", 5, "Cái")
                ));

        OrderDetailResponse response = service.getOrderDetail("1");

        assertNotNull(response);
        assertEquals(2, response.getItems().size());
        assertTrue(repository.getFindRequestItemsCallCount() == 0);
    }

    /** TC_WB_03: Nhánh items.isEmpty() = true — fallback từ YCNH gốc. */
    @Test
    void testGetOrderDetail_WhiteBox_FallbackFromRequest_TC_WB_03() {
        repository.putOrder("2", relatedOrder("2", "2", 1), List.of());
        repository.putRequestItems("2", List.of(
                new RequestItem("4", "Bàn phím cơ Logitech G Pro", 15, "Chiếc", "", "pending")
        ));

        OrderDetailResponse response = service.getOrderDetail("2");

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals("4", response.getItems().get(0).getCode());
        assertEquals("Bàn phím cơ Logitech G Pro", response.getItems().get(0).getName());
        assertEquals(15, response.getItems().get(0).getQuantity());
        assertTrue(repository.getFindRequestItemsCallCount() > 0);
    }

    /** TC_WB_04: Fallback nhưng YCNH gốc không có mặt hàng. */
    @Test
    void testGetOrderDetail_WhiteBox_FallbackEmpty_TC_WB_04() {
        repository.putOrder("3", relatedOrder("3", "3", 0), List.of());
        repository.putRequestItems("3", List.of());

        OrderDetailResponse response = service.getOrderDetail("3");

        assertNotNull(response);
        assertTrue(response.getItems().isEmpty());
    }

    // ==========================================
    // KIỂM THỬ HỘP ĐEN (BLACK-BOX — BVA)
    // ==========================================

    /** TC_BB_01: Mã đơn không hợp lệ (không phải số). */
    @Test
    void testGetOrderDetail_BlackBox_InvalidCodeNotNumeric_TC_BB_01() {
        assertThrows(NoSuchElementException.class, () -> service.getOrderDetail("abc"));
    }

    /** TC_BB_02: Mã đơn rỗng. */
    @Test
    void testGetOrderDetail_BlackBox_EmptyCode_TC_BB_02() {
        assertThrows(NoSuchElementException.class, () -> service.getOrderDetail(""));
    }

    /** TC_BB_03: Mã đơn không tồn tại trong DB. */
    @Test
    void testGetOrderDetail_BlackBox_NotFound_TC_BB_03() {
        assertThrows(NoSuchElementException.class, () -> service.getOrderDetail("99999"));
    }

    /** TC_BB_04: Mã đơn hợp lệ — 2 mặt hàng (theo mockData.sql, đơn #1). */
    @Test
    void testGetOrderDetail_BlackBox_ValidOrder1_TC_BB_04() {
        repository.putOrder("1", relatedOrder("1", "1", 2),
                List.of(
                        new OrderItem("1", "Laptop Dell XPS 13", 10, "Cái"),
                        new OrderItem("3", "Màn hình ASUS ProArt 27 inch", 5, "Cái")
                ));

        OrderDetailResponse response = service.getOrderDetail("1");

        assertEquals("1", response.getCode());
        assertEquals("1", response.getRequestCode());
        assertEquals(2, response.getItems().size());
        assertTrue(response.getItems().stream().anyMatch(i -> "1".equals(i.getCode())));
        assertTrue(response.getItems().stream().anyMatch(i -> "3".equals(i.getCode())));
    }

    /** TC_BB_05: Mã đơn hợp lệ — 1 mặt hàng (theo mockData.sql, đơn #2). */
    @Test
    void testGetOrderDetail_BlackBox_ValidOrder2_TC_BB_05() {
        repository.putOrder("2", relatedOrder("2", "2", 1),
                List.of(new OrderItem("4", "Bàn phím cơ Logitech G Pro", 15, "Chiếc")));

        OrderDetailResponse response = service.getOrderDetail("2");

        assertEquals("2", response.getCode());
        assertEquals(1, response.getItems().size());
        assertEquals("4", response.getItems().get(0).getCode());
    }

    // ==========================================
    // HELPERS
    // ==========================================

    private static IRequestDetailRepository.RelatedOrderWithRequest relatedOrder(
            String orderCode, String requestCode, int itemCount) {
        RelatedOrder order = new RelatedOrder(
                orderCode, "2026-06-12", "Kho Tổng Miền Bắc",
                itemCount, "completed", 0L);
        return new IRequestDetailRepository.RelatedOrderWithRequest(requestCode, order);
    }

    // ==========================================
    // FAKE REPOSITORY
    // ==========================================

    private static class FakeRequestDetailRepository implements IRequestDetailRepository {

        private final Map<String, RelatedOrderWithRequest> orders = new HashMap<>();
        private final Map<String, List<OrderItem>> orderItems = new HashMap<>();
        private final Map<String, List<RequestItem>> requestItems = new HashMap<>();
        private int findRequestItemsCallCount;

        void putOrder(String orderCode, RelatedOrderWithRequest wrap, List<OrderItem> items) {
            orders.put(orderCode, wrap);
            orderItems.put(orderCode, new ArrayList<>(items));
        }

        void putRequestItems(String requestCode, List<RequestItem> items) {
            requestItems.put(requestCode, new ArrayList<>(items));
        }

        int getFindRequestItemsCallCount() {
            return findRequestItemsCallCount;
        }

        @Override
        public Optional<Request> findById(String code) {
            return Optional.empty();
        }

        @Override
        public Optional<RelatedOrderWithRequest> findRelatedOrder(String orderCode) {
            return Optional.ofNullable(orders.get(orderCode));
        }

        @Override
        public List<OrderItem> findOrderItems(String orderCode) {
            return new ArrayList<>(orderItems.getOrDefault(orderCode, List.of()));
        }

        @Override
        public List<RequestItem> findRequestItems(String requestCode) {
            findRequestItemsCallCount++;
            return new ArrayList<>(requestItems.getOrDefault(requestCode, List.of()));
        }
    }
}
