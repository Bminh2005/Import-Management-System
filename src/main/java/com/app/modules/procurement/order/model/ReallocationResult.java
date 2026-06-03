package com.app.modules.procurement.order.model;

import java.util.ArrayList;
import java.util.List;

public class ReallocationResult {
    private final List<Long> createdOrderIds = new ArrayList<>();
    private final List<String> createdOrderSiteNames = new ArrayList<>();

    public ReallocationResult(List<Long> createdOrderIds, List<String> siteNames) {
        if (createdOrderIds != null) {
            this.createdOrderIds.addAll(createdOrderIds);
        }
        if (siteNames != null) {
            this.createdOrderSiteNames.addAll(siteNames);
        }
    }

    public ReallocationResult(List<Long> createdOrderIds) {
        this(createdOrderIds, List.of());
    }

    public List<Long> getCreatedOrderIds() {
        return List.copyOf(createdOrderIds);
    }

    public List<String> getCreatedOrderSiteNames() {
        return List.copyOf(createdOrderSiteNames);
    }

    public int getCount() {
        return createdOrderIds.size();
    }
}
