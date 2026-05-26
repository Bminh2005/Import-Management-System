package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.dashboard.ui.SalesDashboardUI;
import com.app.modules.sales.dashboard.ui.SalesSidebar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class salesApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainLayoutUI root = new MainLayoutUI();
        root.setLeft(new SalesSidebar());
        root.setPage(new SalesDashboardUI());
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
