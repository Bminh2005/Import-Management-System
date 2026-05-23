package com.app.modules.warehouse.inbound.repository;

import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;

import java.util.List;

public class InboundOrderRepository {
    public List<InboundOrderResponse> findAll() {
        return List.of(
                new InboundOrderResponse("ORD-2024-015", "2024-05-10", "Amazon US", "Cho xu ly", 120, 0, "Cho kiem dem"),
                new InboundOrderResponse("ORD-2024-014", "2024-05-10", "Alibaba CN", "Dang xu ly", 80, 42, "Dang nhap hang"),
                new InboundOrderResponse("ORD-2024-013", "2024-05-09", "eBay UK", "Da nhap kho", 64, 64, "Hoan tat"),
                new InboundOrderResponse("ORD-2024-012", "2024-05-09", "Amazon US", "Co sai lech", 45, 43, "Thieu 2 san pham"),
                new InboundOrderResponse("ORD-2024-011", "2024-05-08", "Rakuten JP", "Cho xu ly", 52, 0, "Hang moi ve")
        );
    }
}
