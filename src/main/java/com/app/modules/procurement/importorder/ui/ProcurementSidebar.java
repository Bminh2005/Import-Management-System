package com.app.modules.procurement.importorder.ui;

import com.app.common.ui.MainLayoutUI;
import com.app.common.ui.components.Sidebar;
import com.app.common.ui.components.SidebarItem;
import com.app.modules.procurement.inventory.ui.InventoryListUI;
import com.app.modules.procurement.order.model.SiteOrder;
import com.app.modules.procurement.order.ui.SiteOrderListController;
import com.app.modules.procurement.order.ui.SiteOrderNavigator;
import com.app.modules.procurement.product.ui.MerchandiseListUI;
import com.app.modules.site.catalog.ui.SiteDetailUI;
import com.app.modules.site.catalog.ui.SitesListUI;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        // Đổi tên bộ phận và vai trò hiển thị trên sidebar
        this.getDepartmentName().setText("Bộ phận Đặt hàng quốc tế");
        this.getCurrentRole().setText("Bộ phận Đặt hàng quốc tế");

        setupActions();
    }


    private void setupActions() {
        dashboard.setOnAction(() -> navigateTo(new ProcurementDashboardUI(), "Hệ thống Quản lý Nhập khẩu - Đặt hàng Quốc tế"));
        importRequest.setOnAction(() -> navigateTo(new ProcessImportRequestsUI(), "Hệ thống Quản lý Nhập khẩu - Xử lý Yêu cầu nhập hàng"));
        orderManager.setOnAction(() -> {
            navigateTo(
                    SiteOrderNavigator.getListView(),
                    "Hệ thống Quản lý Nhập khẩu - Đơn đặt hàng"
            );
        });
//        orderManager.setOnAction(() -> {
//            // Placeholder: chuyển đến quản lý mặt hàng (MerchandiseListUI) làm mẫu
//            navigateTo(new MerchandiseListUI(), "Hệ thống Quản lý Nhập khẩu - Quản lý Mặt hàng");
//        });

        siteList.setOnAction(() -> {
            SitesListUI sitesList = new SitesListUI();
            sitesList.setOnViewDetail(siteId -> {
                SiteDetailUI siteDetail = new SiteDetailUI();
                siteDetail.loadSite(siteId);
                siteDetail.setOnBack(() -> navigateTo(sitesList, "Hệ thống Quản lý Nhập khẩu - Danh sách Site"));
                navigateTo(siteDetail, "Hệ thống Quản lý Nhập khẩu - Chi tiết Site " + siteId);
            });
            navigateTo(sitesList, "Hệ thống Quản lý Nhập khẩu - Danh sách Site");
        });

        inventory.setOnAction(() -> navigateTo(new InventoryListUI(), "Hệ thống Quản lý Nhập khẩu - Mặt hàng Tồn kho"));
    }

    private void navigateTo(javafx.scene.Parent newPage, String title) {
        Scene scene = this.getScene();
        if (scene != null) {
            Parent root = scene.getRoot();
            if (root instanceof MainLayoutUI) {
                ((MainLayoutUI) root).setPage(newPage);
            } else {
                scene.setRoot(newPage);
            }
            if (scene.getWindow() instanceof Stage) {
                ((Stage) scene.getWindow()).setTitle(title);
            }
        }
    }
}

