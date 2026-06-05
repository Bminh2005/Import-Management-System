package com.app.modules.warehouse.inbound.service;

import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InboundOrderRulesBlackBoxTest {
    private final InboundOrderRules rules = new InboundOrderRules();

    @Test
    void matchingQuantitiesAreImported() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 100, 100, ""),
                item("SP002", 50, 50, "")
        );

        assertDoesNotThrow(() -> rules.validateItems(items, true));
        assertEquals(InboundOrderRules.STATUS_IMPORTED, rules.resolveStatus(items));
        assertEquals("", rules.resolveMismatchReason(items, ""));
    }

    @Test
    void mismatchWithItemReasonIsAcceptedAndMarkedMismatch() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 100, 95, "Thieu 5 san pham"),
                item("SP002", 50, 50, "")
        );

        assertDoesNotThrow(() -> rules.validateItems(items, true));
        assertEquals(InboundOrderRules.STATUS_MISMATCH, rules.resolveStatus(items));
        assertEquals("SP001: Thieu 5 san pham", rules.resolveMismatchReason(items, ""));
    }

    @Test
    void globalMismatchReasonOverridesItemReasons() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 100, 95, "Thieu 5 san pham")
        );

        assertEquals("Hang bi rach thung",
                rules.resolveMismatchReason(items, " Hang bi rach thung "));
    }

    @Test
    void mismatchWithoutReasonIsRejectedWhenConfirming() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 100, 95, "")
        );

        assertThrows(IllegalArgumentException.class, () -> rules.validateItems(items, true));
    }

    @Test
    void negativeActualQuantityIsRejected() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 100, -1, "Nhap sai so luong")
        );

        assertThrows(IllegalArgumentException.class, () -> rules.validateItems(items, true));
    }

    @Test
    void emptyItemListIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> rules.validateItems(List.of(), true));
    }

    private InboundOrderItemResponse item(String code, int ordered, int actual, String reason) {
        return new InboundOrderItemResponse(1, 1, 1, code, "Mat hang " + code,
                ordered, actual, reason);
    }
}
