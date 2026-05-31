package com.app.modules.procurement.dashboard.ui;

import com.app.common.ui.components.Sidebar;
import com.app.common.ui.components.SidebarItem;

public class ProcurementSidebar extends Sidebar {
    private final SidebarItem dashboard;
    private final SidebarItem orderManager;
    private final SidebarItem siteOrder;
    private final SidebarItem importRequest;
    private final SidebarItem siteList;
    private final SidebarItem inventory;

    public ProcurementSidebar() {
        super();
        dashboard = new SidebarItem("\uD83C\uDFE0", "Trang Chủ", "Ctrl+1");
        orderManager = new SidebarItem("\uD83D\uDCE6", "Đơn hàng nhập khẩu", "Ctrl+2");
        siteOrder = new SidebarItem("\uD83D\uDCCB", "Đơn Đặt Hàng", "Ctrl+3");
        importRequest = new SidebarItem("\uD83D\uDCC4", "Yêu cầu Nhập hàng", "Ctrl+4");
        siteList = new SidebarItem("\uD83D\uDCCD", "Danh sách Site", "Ctrl+5");
        inventory = new SidebarItem("\uD83C\uDFED", "Mặt hàng tồn kho", "Ctrl+6");

        this.addMenuItem(dashboard);
        this.addMenuItem(orderManager);
        this.addMenuItem(siteOrder);
        this.addMenuItem(importRequest);
        this.addMenuItem(siteList);
        this.addMenuItem(inventory);
    }
}
