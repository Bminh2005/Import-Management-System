package com.app.modules.warehouse.dashboard.service;

import com.app.modules.warehouse.dashboard.dto.WarehouseDashboardSummary;

public class WarehouseDashboardService {
    public WarehouseDashboardSummary getDashboardSummary() {
        return new WarehouseDashboardSummary(12, 5, 87, 3);
    }
}
