package com.app.modules.warehouse.inbound.service;

import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;

import java.util.List;

public class InboundOrderRules {
    public static final String STATUS_IMPORTED = "IMPORTED";
    public static final String STATUS_MISMATCH = "MISMATCH";

    public void validateItems(List<InboundOrderItemResponse> items, boolean requireMismatchReason) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Don nhap kho khong co mat hang de xu ly.");
        }
        boolean hasNegativeQuantity = items.stream()
                .anyMatch(item -> item.getActualQuantity() < 0);
        if (hasNegativeQuantity) {
            throw new IllegalArgumentException("So luong thuc nhan khong duoc am.");
        }
        boolean hasMissingReason = requireMismatchReason && items.stream()
                .anyMatch(item -> item.hasMismatch()
                        && (item.getDiscrepancyReason() == null || item.getDiscrepancyReason().isBlank()));
        if (hasMissingReason) {
            throw new IllegalArgumentException("Moi mat hang sai lech can co ly do xu ly.");
        }
    }

    public String resolveStatus(List<InboundOrderItemResponse> items) {
        validateItems(items, false);
        boolean hasMismatch = items.stream().anyMatch(InboundOrderItemResponse::hasMismatch);
        return hasMismatch ? STATUS_MISMATCH : STATUS_IMPORTED;
    }

    public String resolveMismatchReason(List<InboundOrderItemResponse> items, String mismatchReason) {
        validateItems(items, false);
        boolean hasMismatch = items.stream().anyMatch(InboundOrderItemResponse::hasMismatch);
        if (!hasMismatch) {
            return "";
        }
        if (mismatchReason != null && !mismatchReason.isBlank()) {
            return mismatchReason.trim();
        }
        return items.stream()
                .filter(InboundOrderItemResponse::hasMismatch)
                .map(item -> item.getProductCode() + ": " + item.getDiscrepancyReason())
                .reduce((left, right) -> left + "; " + right)
                .orElse("");
    }
}
