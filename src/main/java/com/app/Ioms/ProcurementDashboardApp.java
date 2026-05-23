package com.app.Ioms;

import com.app.modules.procurement.importorder.ui.ProcurementDashboardUI;
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
        ProcurementDashboardUI dashboard = new ProcurementDashboardUI();

        Scene scene = new Scene(dashboard, 1200, 820);

        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Đặt hàng Quốc tế");
        stage.setScene(scene);
        stage.show();
    }
}