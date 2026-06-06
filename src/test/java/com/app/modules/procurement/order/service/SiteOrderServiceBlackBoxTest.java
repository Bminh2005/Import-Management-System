package com.app.modules.procurement.order.service;

import com.app.common.exception.BusinessException;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SiteOrderServiceBlackBoxTest {
    private final SiteOrderService service = new SiteOrderService();

    @Test
    void tcBb01LowerBoundaryZeroQuantityIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.validateReallocationInput(allocationMap(0), 100L, 100));

        assertEquals("Số lượng vật tư phân bổ tại mỗi kho phải lớn hơn 0", exception.getMessage());
    }

    @Test
    void tcBb02LowerBoundaryOneQuantityIsAccepted() {
        assertTrue(service.validateReallocationInput(allocationMap(1), 100L, 1));
    }

    @Test
    void tcBb03OutsideBoundaryNegativeQuantityIsRejected() {
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.validateReallocationInput(allocationMap(-5), 100L, 100));

        assertEquals("Số lượng vật tư phân bổ tại mỗi kho phải lớn hơn 0", exception.getMessage());
    }

    @Test
    void tcBb04ValidInnerBoundaryQuantityIsAccepted() {
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
