package com.app.modules.procurement.order.service;

import com.app.common.exception.BusinessException;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiteOrderServiceWhiteBoxC1Test {
    private final SiteOrderService service = new SiteOrderService();

    @Test
    void tcWb01NullAllocationMapIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.validateReallocationInput(null, 100L, 100));

        assertEquals("Dữ liệu phân bổ mặt hàng không được để trống", exception.getMessage());
    }

    @Test
    void tcWb02InvalidOldOrderIdIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.validateReallocationInput(allocationMap(100), 0L, 100));

        assertEquals("Mã đơn hàng gốc không hợp lệ", exception.getMessage());
    }

    @Test
    void tcWb03ZeroAllocationQuantityIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.validateReallocationInput(allocationMap(0), 100L, 100));

        assertEquals("Số lượng vật tư phân bổ tại mỗi kho phải lớn hơn 0", exception.getMessage());
    }

    @Test
    void tcWb04MismatchedTotalQuantityIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.validateReallocationInput(allocationMap(50), 100L, 100));

        assertEquals("Tổng số lượng phân bổ không khớp với tổng số lượng yêu cầu ban đầu",
                exception.getMessage());
    }

    @Test
    void tcWb05ValidAllocationReturnsTrue() {
        assertTrue(service.validateReallocationInput(allocationMap(100), 100L, 100));
    }

    private Map<Long, List<SiteAllocationEntry>> allocationMap(long quantity) {
        return Map.of(1L, List.of(entry(quantity)));
    }

    private SiteAllocationEntry entry(long quantity) {
        return new SiteAllocationEntry(
                1,
                "Kho A",
                10,
                "Dem cao su",
                "cai",
                quantity,
                1000,
                500
        );
    }
}
