package com.app.modules.warehouse.dashboard.service;

import com.app.modules.warehouse.dashboard.dto.WarehouseDashboardSummary;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;

import java.util.List;

public class WarehouseDashboardService {
    private final InboundOrderService inboundOrderService = new InboundOrderService();

    public WarehouseDashboardSummary getDashboardSummary() {
        List<InboundOrderResponse> orders = inboundOrderService.getAllInboundOrders();
        int pending = countByStatus(orders, "PENDING", "ACCEPTED");
        int processing = countByStatus(orders, "PROCESSING");
        int imported = countByStatus(orders, "IMPORTED");
        int mismatch = countByStatus(orders, "MISMATCH");
        return new WarehouseDashboardSummary(pending, processing, imported, mismatch);
    }

    private int countByStatus(List<InboundOrderResponse> orders, String... statuses) {
        int count = 0;
        for (InboundOrderResponse order : orders) {
            for (String status : statuses) {
                if (status.equals(order.getStatusCode())) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }
}
