package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.dashboard.ui.SalesDashboardUI;
import com.app.modules.sales.dashboard.ui.SalesShell;
import com.app.modules.sales.dashboard.ui.SalesSidebar;
import com.app.modules.sales.request.createrequest.controller.CreateRequestController;
import com.app.modules.sales.request.createrequest.ui.CreateImportRequestUI;
import com.app.modules.sales.request.requestlist.ui.RequestListUI;
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
        SalesShell shell = new SalesShell(root);
        shell.showDashboard();
//        root.setLeft(new SalesSidebar());
//        CreateRequestController controller = new CreateRequestController();
//        root.setPage(controller.getView());
        Scene scene = new Scene(root, 1280, 720);

//        scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/theme.css").toExternalForm());
//        scene.getStylesheets().add(getClass().getResource("/com/app/common/ui/components/common.css").toExternalForm());
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Sales");
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
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
