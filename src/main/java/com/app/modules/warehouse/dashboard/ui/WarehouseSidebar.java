package com.app.modules.warehouse.dashboard.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class WarehouseSidebar extends VBox {
    @FXML
    private Button homeButton;

    @FXML
    private Button inboundButton;

    public WarehouseSidebar() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WarehouseSidebar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Khong the tai WarehouseSidebar.fxml", exception);
        }
    }

    @FXML
    private void initialize() {
        setActiveMenu("home");
    }

    @FXML
    private void onHomeClick() {
        System.out.println("Chuyen trang: Trang chu quan ly kho");
        setActiveMenu("home");
        WarehouseNavigation.showWarehouseHome(this);
    }

    @FXML
    private void onInboundClick() {
        System.out.println("Chuyen trang: Danh sach don nhap kho");
        setActiveMenu("inbound");
        WarehouseNavigation.showInboundOrderList(this);
    }

    @FXML
    private void onCollapseClick() {
        System.out.println("Noi dung chuc nang: Thu gon/mo rong sidebar kho");
    }

    public void setActiveMenu(String activeMenu) {
        setButtonActive(homeButton, !"inbound".equals(activeMenu));
        setButtonActive(inboundButton, "inbound".equals(activeMenu));
    }

    private void setButtonActive(Button button, boolean active) {
        if (button == null) {
            return;
        }
        button.getStyleClass().remove("active");
        if (active) {
            button.getStyleClass().add("active");
        }
    }
}
