package com.app.Ioms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Điểm chạy thử feature warehouse/inventory: mở thẳng màn
 * "Mặt hàng Tồn kho" (danh sách) với dữ liệu mẫu trong InventoryRepository.
 *
 * Từ màn danh sách bấm "Xem chi tiết" → sang InventoryDetailPage,
 * "Quay lại danh sách" → về lại màn danh sách.
 *
 * Cách chạy:
 * - IDE: right-click {@link WarehouseInventoryLauncher} → Run.
 */
public class WarehouseInventoryApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/app/modules/warehouse/inventory/ui/InventoryListPage.fxml"));
        Scene scene = new Scene(loader.load(), 1180, 820);

        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Mặt hàng Tồn kho");
        stage.setScene(scene);
        stage.show();
    }
}
