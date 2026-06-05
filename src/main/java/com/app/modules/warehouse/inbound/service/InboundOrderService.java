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
    private final InboundOrderRules inboundOrderRules = new InboundOrderRules();

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
        inboundOrderRules.validateItems(items, true);
        String targetStatus = inboundOrderRules.resolveStatus(items);
        String resolvedMismatchReason = inboundOrderRules.resolveMismatchReason(items, mismatchReason);
        inboundOrderRepository.confirmInboundOrder(orderId, items,
                targetStatus, resolvedMismatchReason, inspectedBy);
        return warehouseOrderApiClient.postOrder(getOrderById(orderId), items);
    }

    public void saveDraft(long orderId, List<InboundOrderItemResponse> items) {
        inboundOrderRules.validateItems(items, false);
        inboundOrderRepository.saveDraft(orderId, items);
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
