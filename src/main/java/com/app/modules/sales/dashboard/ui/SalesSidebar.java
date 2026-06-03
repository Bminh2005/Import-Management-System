package com.app.modules.sales.dashboard.ui;

import com.app.common.ui.components.Sidebar;
import com.app.common.ui.components.SidebarItem;

public class SalesSidebar extends Sidebar {
    private final SidebarItem dashboard;
    private final SidebarItem productManager;
    private final SidebarItem importRequest;

    public SalesSidebar() {
        super();
        dashboard = new SidebarItem("\uD83C\uDFE0", "Trang Chủ", "Ctrl+1");
        productManager = new SidebarItem("\uD83D\uDCE6", "Quản lý Mặt hàng", "Ctrl+2");
        importRequest = new SidebarItem("\uD83D\uDCC4", "Yêu cầu Nhập hàng", "Ctrl+3");
        this.addMenuItem(dashboard);
        this.addMenuItem(productManager);
        this.addMenuItem(importRequest);
    }

    /** Gắn điều hướng thật (gọi từ {@link SalesShell}). */
    public void bindNavigation(Runnable onDashboard, Runnable onProducts, Runnable onImportRequests) {
        bindItemAction(dashboard, onDashboard);
        bindItemAction(productManager, onProducts);
        bindItemAction(importRequest, onImportRequests);
    }

    public SidebarItem getImportRequestItem() {
        return importRequest;
    }

    public SidebarItem getProductManagerItem() {
        return productManager;
    }
}
