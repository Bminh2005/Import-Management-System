package com.app.modules.warehouse.inbound.service;

import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InboundOrderRulesWhiteBoxC1Test {
    private final InboundOrderRules rules = new InboundOrderRules();

    @Test
    void validateItemsCoversNullInputBranch() {
        assertThrows(IllegalArgumentException.class, () -> rules.validateItems(null, true));
    }

    @Test
    void validateItemsCoversNegativeQuantityBranch() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 10, -1, "So luong am")
        );

        assertThrows(IllegalArgumentException.class, () -> rules.validateItems(items, false));
    }

    @Test
    void validateItemsAllowsMissingReasonWhenSavingDraft() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 10, 8, "")
        );

        assertDoesNotThrow(() -> rules.validateItems(items, false));
    }

    @Test
    void validateItemsRejectsMissingReasonWhenConfirming() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 10, 8, "   ")
        );

        assertThrows(IllegalArgumentException.class, () -> rules.validateItems(items, true));
    }

    @Test
    void resolveStatusCoversImportedAndMismatchBranches() {
        assertEquals(InboundOrderRules.STATUS_IMPORTED,
                rules.resolveStatus(List.of(item("SP001", 10, 10, ""))));
        assertEquals(InboundOrderRules.STATUS_MISMATCH,
                rules.resolveStatus(List.of(item("SP001", 10, 12, "Thua 2 san pham"))));
    }

    @Test
    void resolveMismatchReasonCoversNoMismatchBranch() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 10, 10, "")
        );

        assertEquals("", rules.resolveMismatchReason(items, "Ghi chu khong duoc dung"));
    }

    @Test
    void resolveMismatchReasonCoversAggregateItemReasonsBranch() {
        List<InboundOrderItemResponse> items = List.of(
                item("SP001", 10, 8, "Thieu 2"),
                item("SP002", 5, 6, "Thua 1")
        );

        assertEquals("SP001: Thieu 2; SP002: Thua 1",
                rules.resolveMismatchReason(items, " "));
    }

    private InboundOrderItemResponse item(String code, int ordered, int actual, String reason) {
        return new InboundOrderItemResponse(1, 1, 1, code, "Mat hang " + code,
                ordered, actual, reason);
    }
}
