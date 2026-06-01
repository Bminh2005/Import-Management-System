package com.app.vinh.test;

import com.app.modules.warehouse.dashboard.ui.WarehouseHomeUI;
import com.app.modules.warehouse.inbound.ui.InboundOrderListUI;
import com.app.modules.warehouse.inbound.ui.InboundOrderProcessUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VinhWarehouseTestApplication extends Application {
    private static final double DEFAULT_WIDTH = 1280;
    private static final double DEFAULT_HEIGHT = 820;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Parent root = createRootFromArgs();
        Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        stage.setTitle("Vinh Test - Warehouse UI");
        stage.setMinWidth(1180);
        stage.setMinHeight(760);
        stage.setScene(scene);
        stage.show();
    }

    private Parent createRootFromArgs() {
        Parameters parameters = getParameters();
        if (parameters.getRaw().isEmpty()) {
            return new WarehouseHomeUI();
        }

        String screenName = parameters.getRaw().get(0);
        return switch (screenName) {
            case "list" -> new InboundOrderListUI();
            case "process" -> new InboundOrderProcessUI();
            case "home" -> new WarehouseHomeUI();
            default -> {
                System.out.println("Nội dung chức năng: Màn hình test không hợp lệ - " + screenName);
                yield new WarehouseHomeUI();
            }
        };
    }
}
