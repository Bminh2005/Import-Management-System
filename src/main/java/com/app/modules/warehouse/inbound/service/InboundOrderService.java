package com.app.modules.warehouse.inbound.service;

import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
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

    public InboundOrderResponse getFirstProcessableOrder() {
        return inboundOrderRepository.findAll().stream()
                .filter(order -> !"Da nhap kho".equals(order.getStatus()))
                .findFirst()
                .orElse(new InboundOrderResponse());
    }
}
