package com.app.Ioms.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class WarehouseNavigation {
    private static final String WAREHOUSE_HOME =
            "/com/app/modules/warehouse/dashboard/ui/WarehouseHomePage.fxml";
    private static final String INBOUND_ORDER_LIST =
            "/com/app/modules/warehouse/inbound/ui/InboundOrderListPage.fxml";
    private static final String INBOUND_ORDER_PROCESS =
            "/com/app/modules/warehouse/inbound/ui/InboundOrderProcessPage.fxml";

    private WarehouseNavigation() {
    }

    public static void showWarehouseHome(Node source) {
        System.out.println("Chuyen trang: Trang chu quan ly kho");
        switchScene(source, WAREHOUSE_HOME);
    }

    public static void showInboundOrderList(Node source) {
        System.out.println("Chuyen trang: Danh sach don nhap kho");
        switchScene(source, INBOUND_ORDER_LIST);
    }

    public static void showInboundOrderProcess(Node source) {
        System.out.println("Chuyen trang: Xu ly don nhap kho");
        switchScene(source, INBOUND_ORDER_PROCESS);
    }

    private static void switchScene(Node source, String fxmlPath) {
        try {
            URL resource = WarehouseNavigation.class.getResource(fxmlPath);
            if (resource == null) {
                throw new IllegalStateException("Khong tim thay FXML: " + fxmlPath);
            }
            Parent page = FXMLLoader.load(resource);
            Scene currentScene = source.getScene();
            if (currentScene == null) {
                return;
            }
            currentScene.setRoot(page);
            Stage stage = (Stage) currentScene.getWindow();
            stage.setTitle("He thong Nhap hang - Quan ly Kho");
        } catch (IOException exception) {
            throw new IllegalStateException("Khong the chuyen trang: " + fxmlPath, exception);
        }
    }
}
