package com.app.Ioms.service;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.Site;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Allocation logic mock:
 * - Sắp xếp site theo shippingPriority
 * - Chỉ hiển thị site có tồn > 0 cho SKU tương ứng
 * - Trả về danh sách site đề xuất cho từng item
 */
public class AllocationService {
package com.app.Ioms.service;

import com.app.Ioms.domain.Order;
import com.app.Ioms.domain.OrderItem;
import com.app.Ioms.domain.Site;

import java.util.*;
import java.util.stream.Collectors;

public class AllocationService {

    private static final List<Site> SITES = List.of(
            new Site("SGN", "HCM Warehouse", 1),
            new Site("DAD", "Da Nang DC", 2),
            new Site("HAN", "Ha Noi DC", 3),
            new Site("NYC", "New York Hub", 4),
            new Site("LON", "London Hub", 5)
    );

    private static final Map<String, Map<String, Integer>> INVENTORY = new HashMap<>();

    static {
        INVENTORY.put("SKU-1001", new HashMap<>(Map.of("SGN", 15, "HAN", 5, "NYC", 0, "LON", 2)));
        INVENTORY.put("SKU-1002", new HashMap<>(Map.of("DAD", 7, "SGN", 3, "NYC", 4)));
        INVENTORY.put("SKU-2001", new HashMap<>(Map.of("NYC", 10, "LON", 1)));
        INVENTORY.put("SKU-3003", new HashMap<>(Map.of("HAN", 8)));
        INVENTORY.put("SKU-4004", new HashMap<>(Map.of("SGN", 20)));
    }

    public List<Site> listSitesByPriority() {
        return SITES.stream().sorted(Comparator.comparingInt(Site::getShippingPriority)).collect(Collectors.toList());
    }

    public Map<String, Integer> inventoryForSku(String sku) {
        return INVENTORY.getOrDefault(sku, Collections.emptyMap());
    }

    public List<Site> suggestedSitesForItem(OrderItem item) {
        Map<String, Integer> map = inventoryForSku(item.getSku());
        return listSitesByPriority().stream()
                .filter(s -> map.getOrDefault(s.getCode(), 0) > 0)
                .collect(Collectors.toList());
    }

    public boolean canAllocate(Order order, Map<String, String> itemToSiteCode) {
        for (var it : order.getItems()) {
            String siteCode = itemToSiteCode.get(it.getSku());
            if (siteCode == null) return false;
            int qty = INVENTORY.getOrDefault(it.getSku(), Map.of()).getOrDefault(siteCode, 0);
            if (qty < it.getQuantity()) return false;
        }
        return true;
    }

    public void reserve(Order order, Map<String, String> itemToSiteCode) {
        for (var it : order.getItems()) {
            String site = itemToSiteCode.get(it.getSku());
            INVENTORY.computeIfPresent(it.getSku(), (sku, map) -> {
                map.put(site, map.getOrDefault(site, 0) - it.getQuantity());
                return map;
            });
        }
    }
}
    private static final List<Site> SITES = List.of(
            new Site("SGN", "HCM Warehouse", 1),
            new Site("DAD", "Da Nang DC", 2),
            new Site("HAN", "Ha Noi DC", 3),
            new Site("NYC", "New York Hub", 4),
            new Site("LON", "London Hub", 5)
    );

    // tồn kho giả lập theo SKU + Site
    private static final Map<String, Map<String, Integer>> INVENTORY = new HashMap<>();

    static {
        // SKU-1001
        INVENTORY.put("SKU-1001", new HashMap<>(Map.of("SGN", 15, "HAN", 5, "NYC", 0, "LON", 2)));
        // SKU-1002
        INVENTORY.put("SKU-1002", new HashMap<>(Map.of("DAD", 7, "SGN", 3, "NYC", 4)));
        // SKU-2001
        INVENTORY.put("SKU-2001", new HashMap<>(Map.of("NYC", 10, "LON", 1)));
        // SKU-3003
        INVENTORY.put("SKU-3003", new HashMap<>(Map.of("HAN", 8)));
        // SKU-4004
        INVENTORY.put("SKU-4004", new HashMap<>(Map.of("SGN", 20)));
    }

    public List<Site> listSitesByPriority() {
        return SITES.stream().sorted(Comparator.comparingInt(Site::getShippingPriority)).collect(Collectors.toList());
    }

    public Map<String, Integer> inventoryForSku(String sku) {
        return INVENTORY.getOrDefault(sku, Collections.emptyMap());
    }

    public List<Site> suggestedSitesForItem(OrderItem item) {
        Map<String, Integer> map = inventoryForSku(item.getSku());
        return listSitesByPriority().stream()
                .filter(s -> map.getOrDefault(s.getCode(), 0) > 0)
                .collect(Collectors.toList());
    }

    public boolean canAllocate(Order order, Map<String, String> itemToSiteCode) {
        // kiểm tra tổng tồn đủ theo lựa chọn
        for (var it : order.getItems()) {
            String siteCode = itemToSiteCode.get(it.getSku());
            if (siteCode == null) return false;
            int qty = INVENTORY.getOrDefault(it.getSku(), Map.of()).getOrDefault(siteCode, 0);
            if (qty < it.getQuantity()) return false;
        }
        return true;
    }

    public void reserve(Order order, Map<String, String> itemToSiteCode) {
        // trừ tạm tồn kho
        for (var it : order.getItems()) {
            String site = itemToSiteCode.get(it.getSku());
            INVENTORY.computeIfPresent(it.getSku(), (sku, map) -> {
                map.put(site, map.getOrDefault(site, 0) - it.getQuantity());
                return map;
            });
        }
    }
}
