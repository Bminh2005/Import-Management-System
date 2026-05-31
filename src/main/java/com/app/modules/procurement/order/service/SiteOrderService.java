package com.app.modules.procurement.order.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.app.modules.procurement.order.dao.ImportRequestDAO;
import com.app.modules.procurement.order.dao.SiteInventoryDAO;
import com.app.modules.procurement.order.dao.SiteOrderDAO;
import com.app.modules.procurement.order.model.DeliveryType;
import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.OrderStatus;
import com.app.modules.procurement.order.model.RequestDetailItem;
import com.app.modules.procurement.order.model.SiteAllocationEntry;
import com.app.modules.procurement.order.model.SiteInventoryInfo;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.model.SiteOrderItem;

public class SiteOrderService {
    private final SiteOrderDAO orderDAO;
    private final SiteInventoryDAO inventoryDAO;
    private final ImportRequestDAO requestDAO;

    public SiteOrderService() {
        this.orderDAO = new SiteOrderDAO();
        this.inventoryDAO = new SiteInventoryDAO();
        this.requestDAO = new ImportRequestDAO();
    }

    public List<SiteOrder> getAllOrders() {
        return orderDAO.getAll();
    }

    public List<SiteOrder> getOrdersByStatus(OrderStatus status) {
        return orderDAO.getByStatus(status);
    }

    public SiteOrder getOrderById(long orderId) {
        return orderDAO.getById(orderId);
    }

    public int countByStatus(OrderStatus status) {
        return orderDAO.countByStatus(status);
    }

    public List<SiteInventoryInfo> getAvailableSitesForItem(long merchandiseDetailId) {
        return inventoryDAO.getAvailableSitesForItem(merchandiseDetailId);
    }

    public List<RequestDetailItem> getRequestDetails(long requestId) {
        return requestDAO.getRequestDetails(requestId);
    }

    public ImportRequestInfo getRequestInfo(long requestId) {
        return requestDAO.findRequestById(requestId).orElse(null);
    }

    public long saveOrder(SiteOrder order, List<SiteOrderItem> items) {
        return orderDAO.save(order, items);
    }

    public int createOrdersFromAllocation(long requestId, long userId,
                                          LocalDate expectedDate,
                                          Map<Long, List<SiteAllocationEntry>> allocationMap) {
        int created = 0;
        for (Map.Entry<Long, List<SiteAllocationEntry>> entry : allocationMap.entrySet()) {
            long siteId = entry.getKey();
            List<SiteAllocationEntry> allocations = entry.getValue();
            if (allocations.isEmpty()) {
                continue;
            }
            SiteOrder order = new SiteOrder(siteId, requestId, userId,
                    expectedDate, OrderStatus.PENDING, DeliveryType.SHIP);
            List<SiteOrderItem> items = new ArrayList<>();
            for (SiteAllocationEntry allocation : allocations) {
                if (allocation.getQuantity() <= 0) {
                    continue;
                }
                SiteOrderItem item = new SiteOrderItem();
                item.setMerchandiseDetailId(allocation.getMerchandiseDetailId());
                item.setMerchandiseName(allocation.getMerchandiseName());
                item.setUnit(allocation.getUnit());
                item.setQuantity(allocation.getQuantity());
                item.setPrice(allocation.getPrice());
                item.setStatus(OrderStatus.PENDING);
                item.setRefusedReason(null);
                items.add(item);
            }
            if (!items.isEmpty()) {
                orderDAO.save(order, items);
                created++;
            }
        }
        return created;
    }
}
