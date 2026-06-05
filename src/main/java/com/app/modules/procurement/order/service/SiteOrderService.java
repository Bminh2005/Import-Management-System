package com.app.modules.procurement.order.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.app.modules.procurement.order.dao.ImportRequestDAO;
import com.app.modules.procurement.order.dao.SiteInventoryDAO;
import com.app.modules.procurement.order.dao.SiteOrderDAO;
import com.app.modules.procurement.order.model.DeliveryType;
import com.app.modules.procurement.order.model.ImportRequestInfo;
import com.app.modules.procurement.order.model.OrderStatus;
import com.app.modules.procurement.order.model.ReallocationResult;
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

    public SiteOrder getOrderById(long orderId) {
        return orderDAO.getById(orderId);
    }

    public List<SiteInventoryInfo> getEligibleSitesForReallocation(long merchandiseDetailId,
                                                                   long requiredQuantity,
                                                                   LocalDate desiredDate) {
        List<SiteInventoryInfo> candidates = inventoryDAO.getAvailableSitesForItem(merchandiseDetailId);
        LocalDate today = LocalDate.now();
        List<SiteInventoryInfo> eligible = new ArrayList<>();
        for (SiteInventoryInfo site : candidates) {
            if (site.getAvailableQuantity() < requiredQuantity) {
                continue;
            }
            long shipDays = Math.max(0, site.getDeliveryByShip());
            LocalDate estimated = today.plusDays(shipDays);
            site.setEstimatedDelivery(estimated);
            if (desiredDate != null && !estimated.isBefore(desiredDate)) {
                continue;
            }
            eligible.add(site);
        }
        eligible.sort(Comparator
                .comparingLong(SiteInventoryInfo::getDeliveryByShip)
                .thenComparing(Comparator.comparingLong(SiteInventoryInfo::getAvailableQuantity).reversed()));
        return eligible;
    }

    public List<RequestDetailItem> getReallocationLineItems(SiteOrder order) {
        if (order == null || order.getStatus() != OrderStatus.REFUSED) {
            return List.of();
        }
        List<RequestDetailItem> lines = new ArrayList<>();
        for (SiteOrderItem item : order.getItems()) {
            lines.add(new RequestDetailItem(
                    item.getId(),
                    item.getMerchandiseDetailId(),
                    item.getMerchandiseName(),
                    item.getUnit(),
                    item.getQuantity(),
                    item.getPrice()));
        }
        return lines;
    }

    public ImportRequestInfo resolveRequestContext(SiteOrder order) {
        if (order == null) {
            return null;
        }
        if (order.hasLinkedRequest()) {
            ImportRequestInfo info = getRequestInfo(order.getRequestId());
            if (info != null) {
                return info;
            }
        }
        String desired = order.getExpectedDeliveryDate() != null
                ? order.getExpectedDeliveryDate().toString()
                : null;
        return new ImportRequestInfo(0, order.getUserId(), order.getOrdererName(), desired);
    }

    public ImportRequestInfo getRequestInfo(long requestId) {
        if (requestId <= 0) {
            return null;
        }
        return requestDAO.findRequestById(requestId).orElse(null);
    }

    public LocalDate resolveDesiredDate(ImportRequestInfo requestContext, SiteOrder sourceOrder) {
        if (requestContext != null && requestContext.getDesiredDate() != null) {
            try {
                return LocalDate.parse(requestContext.getDesiredDate());
            } catch (DateTimeParseException ignored) {
                // fall through
            }
        }
        if (sourceOrder != null && sourceOrder.getExpectedDeliveryDate() != null) {
            return sourceOrder.getExpectedDeliveryDate();
        }
        return LocalDate.now().plusDays(7);
    }

    public ReallocationResult finalizeReallocation(SiteOrder sourceOrder,
                                                   ImportRequestInfo requestContext,
                                                   Map<Long, List<SiteAllocationEntry>> allocationMap) {
        if (sourceOrder == null || allocationMap == null || allocationMap.isEmpty()) {
            return new ReallocationResult(List.of());
        }
        long requestId = sourceOrder.hasLinkedRequest()
                ? sourceOrder.getRequestId()
                : requestContext != null ? requestContext.getRequestId() : 0;
        long userId = sourceOrder.getUserId();
        LocalDate expectedDate = resolveDesiredDate(requestContext, sourceOrder);
        List<String> siteNames = new ArrayList<>();
        Map<SiteOrder, List<SiteOrderItem>> replacementOrders = new LinkedHashMap<>();

        for (Map.Entry<Long, List<SiteAllocationEntry>> entry : allocationMap.entrySet()) {
            long siteId = entry.getKey(); // FIX: đặt tên rõ ràng là siteId
            List<SiteAllocationEntry> allocations = entry.getValue();
            if (allocations == null || allocations.isEmpty()) {
                continue;
            }

            // FIX: Tạo SiteOrder mới với id = 0, chỉ set siteId
            // Trước đây: new SiteOrder(entry.getKey(), ...) → truyền siteId vào vị trí id
            // Gây ra conflict vì order mới mang id của site thay vì để DB tự sinh
            SiteOrder order = new SiteOrder();
            order.setId(0); // Đảm bảo DB tự sinh id mới qua sequence
            order.setSiteId(siteId);
            order.setRequestId(requestId);
            order.setUserId(userId);
            order.setExpectedDeliveryDate(expectedDate);
            order.setStatus(OrderStatus.PENDING);
            order.setDelivery(DeliveryType.SHIP);

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
                items.add(item);
            }
            if (!items.isEmpty()) {
                replacementOrders.put(order, items);
                siteNames.add(allocations.get(0).getSiteName());
            }
        }

        List<Long> createdIds = orderDAO.replaceRefusedOrder(sourceOrder.getId(), replacementOrders);
        return new ReallocationResult(createdIds, siteNames);
    }

    public static String formatOrderCode(long orderId) {
        return String.valueOf(orderId);
    }

    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "—";
        }
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}