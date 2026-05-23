package com.app.Ioms;

import com.app.modules.procurement.inventory.ui.InventoryListUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Điểm chạy thử feature procurement/inventory: mở thẳng màn
 * "Mặt hàng Tồn kho" của Bộ phận Đặt hàng Quốc tế với dữ liệu mẫu
 * trong InventoryRepository.
 *
 * Cách chạy:
 * - IDE: right-click {@link ProcurementInventoryLauncher} → Run.
 */
public class ProcurementInventoryApp extends Application {

    @Override
    public void start(Stage stage) {
        InventoryListUI ui = new InventoryListUI();

        Scene scene = new Scene(ui, 1180, 820);
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Mặt hàng Tồn kho");
        stage.setScene(scene);
        stage.show();
    }
}
