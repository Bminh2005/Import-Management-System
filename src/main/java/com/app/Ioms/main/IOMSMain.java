package com.app.Ioms.main;

import com.app.auth.login.controller.LoginController;
import com.app.common.navigator.Navigator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IOMSMain extends Application {
    @Override
    public void start(Stage stage) {
        Navigator navigator = Navigator.getInstance();
        LoginController loginController = new LoginController();
        Scene scene = new Scene(new Pane());

        stage.setTitle("Dang nhap he thong");
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
        stage.setScene(scene);

        navigator.setMainScene(scene);
        navigator.navigateTo(loginController.getLoginUI());

        stage.show();
    }
}
