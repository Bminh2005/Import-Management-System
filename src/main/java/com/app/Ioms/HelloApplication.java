package com.app.Ioms;

import com.app.Ioms.ui.main.MainShell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainShell root = new MainShell();
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("IOMS - International Order Management");
        stage.setScene(scene);
        stage.show();
    }
}
