package com.app.modules.warehouse.dashboard.ui;

import com.app.common.ui.components.Sidebar;
import com.app.common.ui.components.SidebarItem;

public class WarehouseSidebar extends Sidebar {
    private final SidebarItem dashboard;
    private final SidebarItem warehouseManager;

    public WarehouseSidebar() {
        super();
        dashboard = new SidebarItem("\uD83C\uDFE0", "Trang Chủ", "Ctrl+1");
        warehouseManager = new SidebarItem("\uD83D\uDCE6", "Đơn chờ nhập kho", "Ctrl+2");
        this.addMenuItem(dashboard);
        this.addMenuItem(warehouseManager);
    }
}
