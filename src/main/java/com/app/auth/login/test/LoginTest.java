package com.app.auth.login.test;

import com.app.auth.login.controller.LoginController;
import com.app.auth.login.controller.SignupController;
import com.app.auth.login.ui.LoginUI;
import com.app.auth.login.ui.SignupUI;
import com.app.common.navigator.Navigator;
import com.app.common.ui.components.Sidebar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginUI loginUI;
        LoginController controller = new LoginController();
        loginUI = controller.getLoginUI();
        Scene scene = new Scene(new Pane());
        Navigator.getInstance().setMainScene(scene);
        Navigator.getInstance().navigateTo(loginUI);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
