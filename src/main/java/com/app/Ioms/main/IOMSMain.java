package com.app.Ioms.main;

import com.app.auth.login.controller.LoginController;
import com.app.common.navigator.Navigator;
import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.dashboard.ui.SalesShell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class IOMSMain extends Application {
    @Override
    public void start(Stage stage) {
        Navigator navigator = Navigator.getInstance();
        LoginController loginController = new LoginController();
        navigator.setMainScene(new Scene(new Pane()));
        navigator.navigateTo(loginController.getLoginUI());
        Scene root = new Scene(loginController.getLoginUI().getRoot());
        stage.setTitle("Đăng nhập hệ thống");
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
        stage.setScene(root);
        stage.show();
    }
}
