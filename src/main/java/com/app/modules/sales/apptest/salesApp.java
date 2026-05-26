package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class salesApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        BorderPane root = new MainLayoutUI();
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
