package com.app.modules.procurement.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.procurement.importorder.ui.ProcurementDashboardUI;
import com.app.modules.procurement.importorder.ui.ProcurementSidebar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Điểm chạy thử feature procurement/importorder:
 * mở thẳng màn "Trang Chủ - Bộ phận Đặt hàng Quốc tế".
 */
public class ProcurementDashboardApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MainLayoutUI root = new MainLayoutUI();
        root.setLeft(new ProcurementSidebar());
        root.setPage(new ProcurementDashboardUI());

        Scene scene = new Scene(root, 1200, 820);

        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Đặt hàng Quốc tế");
        stage.setScene(scene);
        stage.show();
    }
}