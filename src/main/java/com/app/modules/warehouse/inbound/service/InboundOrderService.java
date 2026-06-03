package com.app.modules.warehouse.inbound.service;

import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.integration.WarehouseOrderApiClient;
import com.app.modules.warehouse.inbound.integration.WarehouseOrderSyncResult;
import com.app.modules.warehouse.inbound.repository.InboundOrderRepository;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class InboundOrderService {
    private final InboundOrderRepository inboundOrderRepository = new InboundOrderRepository();
    private final WarehouseOrderApiClient warehouseOrderApiClient = new WarehouseOrderApiClient();

    public List<InboundOrderResponse> getRecentInboundOrders() {
        return inboundOrderRepository.findAll().stream()
                .limit(4)
                .toList();
    }

    public List<InboundOrderResponse> getAllInboundOrders() {
        return inboundOrderRepository.findAll();
    }

    public List<InboundOrderResponse> searchInboundOrders(String keyword, String selectedStatus) {
        String normalizedKeyword = normalize(keyword);
        String statusCode = mapStatusCode(selectedStatus);
        return inboundOrderRepository.findAll().stream()
                .filter(order -> normalizedKeyword.isBlank()
                        || normalize(order.getOrderCode()).contains(normalizedKeyword)
                        || normalize(order.getRequestCode()).contains(normalizedKeyword)
                        || normalize(order.getSupplier()).contains(normalizedKeyword))
                .filter(order -> statusCode.isBlank() || statusCode.equals(order.getStatusCode()))
                .toList();
    }

    public List<InboundOrderItemResponse> getOrderItems(long orderId) {
        return inboundOrderRepository.findItemsByOrderId(orderId);
    }

    public InboundOrderResponse getFirstProcessableOrder() {
        return inboundOrderRepository.findAll().stream()
                .filter(order -> !"IMPORTED".equals(order.getStatusCode()))
                .findFirst()
                .orElse(new InboundOrderResponse());
    }

    public InboundOrderResponse getOrderById(long orderId) {
        return inboundOrderRepository.findAll().stream()
                .filter(order -> order.getOrderId() == orderId)
                .findFirst()
                .orElseGet(this::getFirstProcessableOrder);
    }

    public WarehouseOrderSyncResult confirmInboundOrder(long orderId, List<InboundOrderItemResponse> items,
                                                        String mismatchReason, long inspectedBy) {
        validateItems(items, true);
        inboundOrderRepository.confirmInboundOrder(orderId, items,
                resolveMismatchReason(items, mismatchReason), inspectedBy);
        return warehouseOrderApiClient.postOrder(getOrderById(orderId), items);
    }

    public void saveDraft(long orderId, List<InboundOrderItemResponse> items) {
        validateItems(items, false);
        inboundOrderRepository.saveDraft(orderId, items);
    }

    private void validateItems(List<InboundOrderItemResponse> items, boolean requireMismatchReason) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Đơn nhập kho không có mặt hàng để xử lý.");
        }
        boolean hasNegativeQuantity = items.stream()
                .anyMatch(item -> item.getActualQuantity() < 0);
        if (hasNegativeQuantity) {
            throw new IllegalArgumentException("Số lượng thực nhận không được âm.");
        }
        boolean hasMissingReason = requireMismatchReason && items.stream()
                .anyMatch(item -> item.hasMismatch()
                        && (item.getDiscrepancyReason() == null || item.getDiscrepancyReason().isBlank()));
        if (hasMissingReason) {
            throw new IllegalArgumentException("Mỗi mặt hàng sai lệch cần có lý do xử lý.");
        }
    }

    private String resolveMismatchReason(List<InboundOrderItemResponse> items, String mismatchReason) {
        boolean hasMismatch = items.stream().anyMatch(InboundOrderItemResponse::hasMismatch);
        if (!hasMismatch) {
            return "";
        }
        if (mismatchReason != null && !mismatchReason.isBlank()) {
            return mismatchReason;
        }
        return items.stream()
                .filter(InboundOrderItemResponse::hasMismatch)
                .map(item -> item.getProductCode() + ": " + item.getDiscrepancyReason())
                .reduce((left, right) -> left + "; " + right)
                .orElse("");
    }

    private String mapStatusCode(String selectedStatus) {
        String status = normalize(selectedStatus);
        if (status.isBlank() || "tat ca".equals(status)) {
            return "";
        }
        if ("cho xu ly".equals(status)) {
            return "PENDING";
        }
        if ("dang xu ly".equals(status)) {
            return "PROCESSING";
        }
        if ("da nhap kho".equals(status)) {
            return "IMPORTED";
        }
        if ("co sai lech".equals(status)) {
            return "MISMATCH";
        }
        return "";
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.toLowerCase(Locale.ROOT);
    }
}
