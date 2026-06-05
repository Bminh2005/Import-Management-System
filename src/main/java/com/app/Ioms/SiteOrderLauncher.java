package com.app.Ioms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SiteOrderLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/app/modules/procurement/order/ui/SiteOrderListView.fxml"
                )
        );

        Scene scene = new Scene(loader.load(), 1280, 800);

        stage.setTitle("Đơn Đặt Hàng - Danh sách");
        stage.setMinWidth(1024);
        stage.setMinHeight(640);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}