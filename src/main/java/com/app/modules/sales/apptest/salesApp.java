package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.dashboard.ui.SalesDashboardUI;
import com.app.modules.sales.dashboard.ui.SalesSidebar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

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
//    public static void main(String[] args) {
//        String url = "jdbc:postgresql://aws-1-ap-northeast-2.pooler.supabase.com:6543/postgres?user=postgres.lsrmwnisivtnpeqmpxcu&password=binhminh3000";
//
//        try (Connection conn = DriverManager.getConnection(url)) {
//            System.out.println("Connected!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
