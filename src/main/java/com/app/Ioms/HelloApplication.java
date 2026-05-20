package com.app.Ioms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(
                "/com/app/modules/warehouse/dashboard/ui/WarehouseHomePage.fxml"
        ));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 820);
        stage.setMinWidth(1180);
        stage.setMinHeight(760);
        stage.setTitle("He thong Nhap hang - Quan ly Kho");
        stage.setScene(scene);
        stage.show();
    }
}
