package com.app.modules.procurement.dashboard.ui;

import com.app.auth.security.CurrentUserSession;
import com.app.common.ui.components.Sidebar;
import com.app.common.ui.components.SidebarItem;

public class ProcurementSidebar extends Sidebar {
    private final SidebarItem dashboard;
    private final SidebarItem orderManager;
    private final SidebarItem importRequest;
    private final SidebarItem siteList;
    private final SidebarItem inventory;

    public ProcurementSidebar() {
        super();
        dashboard = new SidebarItem("\uD83C\uDFE0", "Trang Chủ", "Ctrl+1");
        orderManager = new SidebarItem("\uD83D\uDCE6", "Đơn hàng nhập khẩu", "Ctrl+2");
        importRequest = new SidebarItem("\uD83D\uDCC4", "Yêu cầu Nhập hàng", "Ctrl+3");
        siteList = new SidebarItem("\uD83D\uDCCD", "Danh sách Site", "Ctrl+3");
        inventory = new SidebarItem("\uD83C\uDFED", "Mặt hàng tồn kho", "Ctrl+3");

        this.addMenuItem(dashboard);
        this.addMenuItem(orderManager);
        this.addMenuItem(importRequest);
        this.addMenuItem(siteList);
        this.addMenuItem(inventory);
    }
}
