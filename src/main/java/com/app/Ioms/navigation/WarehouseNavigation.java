package com.app.Ioms.navigation;

import com.app.modules.warehouse.dashboard.ui.WarehouseHomeUI;
import com.app.modules.warehouse.inbound.ui.InboundOrderListUI;
import com.app.modules.warehouse.inbound.ui.InboundOrderProcessUI;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.function.Supplier;

public final class WarehouseNavigation {
    private WarehouseNavigation() {
    }

    public static void showWarehouseHome(Node source) {
        System.out.println("Chuyển trang: Trang chủ quản lý kho");
        switchScene(source, WarehouseHomeUI::new);
    }

    public static void showInboundOrderList(Node source) {
        System.out.println("Chuyển trang: Danh sách đơn nhập kho");
        switchScene(source, InboundOrderListUI::new);
    }

    public static void showInboundOrderList(Node source, String statusCode) {
        System.out.println("Chuyển trang: Danh sách đơn nhập kho - " + statusCode);
        switchScene(source, () -> {
            InboundOrderListUI page = new InboundOrderListUI();
            page.setSelectedStatusCode(statusCode);
            return page;
        });
    }

    public static void showInboundOrderProcess(Node source) {
        System.out.println("Chuyển trang: Xử lý đơn nhập kho");
        switchScene(source, InboundOrderProcessUI::new);
    }

    public static void showInboundOrderProcess(Node source, long orderId) {
        System.out.println("Chuyển trang: Xử lý đơn nhập kho " + orderId);
        switchScene(source, () -> new InboundOrderProcessUI(orderId));
    }

    private static void switchScene(Node source, Supplier<Parent> pageFactory) {
        Scene currentScene = source.getScene();
        if (currentScene == null) {
            return;
        }
        currentScene.setRoot(pageFactory.get());
        Stage stage = (Stage) currentScene.getWindow();
        stage.setTitle("Hệ thống Nhập hàng - Quản lý Kho");
    }
}
