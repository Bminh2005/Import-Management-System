package com.app.modules.warehouse.dashboard.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.ui.components.Sidebar;
import com.app.common.ui.components.SidebarItem;

public class WarehouseSidebar extends Sidebar {
    private final SidebarItem dashboard;
    private final SidebarItem warehouseManager;

    public WarehouseSidebar() {
        super();
        getDepartmentName().setText("Bộ phận Quản lý Kho");
        getCurrentRole().setText("Bộ phận Quản lý Kho");

        dashboard = new SidebarItem("\uD83C\uDFE0", "Trang Chủ Kho", "Ctrl+1");
        warehouseManager = new SidebarItem("\uD83D\uDCE6", "Đơn Chờ Nhập Kho", "Ctrl+2");
        addMenuItem(dashboard);
        addMenuItem(warehouseManager);

        dashboard.setOnAction(() -> {
            setSelectedItem(dashboard);
            WarehouseNavigation.showWarehouseHome(this);
        });
        warehouseManager.setOnAction(() -> {
            setSelectedItem(warehouseManager);
            WarehouseNavigation.showInboundOrderList(this);
        });
        setActionLogoutButton(() -> System.out.println("Noi dung chuc nang: Dang xuat"));
    }

    public void setActiveMenu(String activeMenu) {
        if ("inbound".equals(activeMenu)) {
            setSelectedItem(warehouseManager);
            return;
        }
        setSelectedItem(dashboard);
    }
}
