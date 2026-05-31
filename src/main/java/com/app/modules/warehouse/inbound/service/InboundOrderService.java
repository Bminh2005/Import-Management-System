package com.app.modules.warehouse.inbound.service;

import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.repository.InboundOrderRepository;

import java.util.List;

public class InboundOrderService {
    private final InboundOrderRepository inboundOrderRepository = new InboundOrderRepository();

    public List<InboundOrderResponse> getRecentInboundOrders() {
        return inboundOrderRepository.findAll().stream()
                .limit(4)
                .toList();
    }

    public List<InboundOrderResponse> getAllInboundOrders() {
        return inboundOrderRepository.findAll();
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

    public void confirmInboundOrder(long orderId, List<InboundOrderItemResponse> items,
                                    String mismatchReason, long inspectedBy) {
        inboundOrderRepository.confirmInboundOrder(orderId, items, mismatchReason, inspectedBy);
    }
}
