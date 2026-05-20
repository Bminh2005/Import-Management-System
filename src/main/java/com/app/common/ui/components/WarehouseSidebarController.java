package com.app.common.ui.components;

import com.app.Ioms.navigation.WarehouseNavigation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WarehouseSidebarController {
    @FXML
    private Button warehouseHomeButton;

    @FXML
    private Button inboundOrdersButton;

    private String activeMenu = "home";

    @FXML
    private void initialize() {
        applyActiveMenu();
    }

    @FXML
    private void onWarehouseHomeClick() {
        System.out.println("Noi dung chuc nang: Mo trang chu quan ly kho");
        WarehouseNavigation.showWarehouseHome(warehouseHomeButton);
    }

    @FXML
    private void onInboundOrdersClick() {
        System.out.println("Noi dung chuc nang: Mo danh sach don nhap kho");
        WarehouseNavigation.showInboundOrderList(inboundOrdersButton);
    }

    @FXML
    private void onSwitchDepartmentClick() {
        System.out.println("Noi dung chuc nang: Doi bo phan");
    }

    @FXML
    private void onCollapseClick() {
        System.out.println("Noi dung chuc nang: Thu gon thanh dieu huong");
    }

    public String getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
        applyActiveMenu();
    }

    private void applyActiveMenu() {
        if (warehouseHomeButton == null || inboundOrdersButton == null) {
            return;
        }
        warehouseHomeButton.getStyleClass().remove("active");
        inboundOrdersButton.getStyleClass().remove("active");
        if ("inbound".equals(activeMenu)) {
            inboundOrdersButton.getStyleClass().add("active");
        } else {
            warehouseHomeButton.getStyleClass().add("active");
        }
    }
}
