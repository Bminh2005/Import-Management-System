package com.app.Ioms.ui.orders;
package com.app.Ioms.ui.orders;

import com.app.Ioms.domain.Order;
import java.util.Map;

public class AllocationResult {
    private final Order order;
    private final Map<String, String> allocation; // Map<SKU, SiteCode>

    public AllocationResult(Order order, Map<String, String> allocation) {
        this.order = order;
        this.allocation = allocation;
    }

    public Order getOrder() { return order; }
    public Map<String, String> getAllocation() { return allocation; }
}
import com.app.Ioms.domain.Order;

import java.util.Map;

public class AllocationResult {
    private final Order order;
    // Map<SKU, SiteCode>
    private final Map<String, String> allocation;

    public AllocationResult(Order order, Map<String, String> allocation) {
        this.order = order;
        this.allocation = allocation;
    }

    public Order getOrder() {
        return order;
    }

    public Map<String, String> getAllocation() {
        return allocation;
    }
}
