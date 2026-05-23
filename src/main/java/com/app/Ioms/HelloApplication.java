package com.app.Ioms;

import com.app.modules.warehouse.dashboard.ui.WarehouseHomeUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new WarehouseHomeUI(), 1280, 820);
        stage.setMinWidth(1180);
        stage.setMinHeight(760);
        stage.setTitle("He thong Nhap hang - Quan ly Kho");
        stage.setScene(scene);
        stage.show();
    }
}
