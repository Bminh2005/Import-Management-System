package com.app.Ioms;

import com.app.modules.warehouse.inventory.ui.InventoryListUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Điểm chạy thử feature warehouse/inventory: mở thẳng màn
 * "Mặt hàng Tồn kho" (danh sách) với dữ liệu mẫu trong InventoryRepository.
 *
 * Cách chạy:
 * - IDE: right-click {@link WarehouseInventoryLauncher} → Run.
 */
public class WarehouseInventoryApp extends Application {

    @Override
    public void start(Stage stage) {
        InventoryListUI ui = new InventoryListUI();

        Scene scene = new Scene(ui, 1180, 820);
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Mặt hàng Tồn kho");
        stage.setScene(scene);
        stage.show();
    }
}
