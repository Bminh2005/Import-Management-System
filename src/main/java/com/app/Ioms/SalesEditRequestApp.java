package com.app.Ioms;

import com.app.modules.sales.request.ui.EditRequestUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App test cho màn "Chỉnh sửa Yêu cầu Nhập hàng" (EditRequestUI).
 * Mở thẳng màn chỉnh sửa với một mã yêu cầu mẫu trong RequestRepository.
 * Màn này độc lập — thao tác vào đến từ danh sách yêu cầu nhập hàng.
 *
 * Cách chạy: right-click {@link SalesEditRequestLauncher} → Run.
 */
public class SalesEditRequestApp extends Application {

    private static final String DEFAULT_REQUEST_CODE = "4";

    @Override
    public void start(Stage stage) {
        EditRequestUI ui = new EditRequestUI();
        ui.loadRequest(DEFAULT_REQUEST_CODE);

        Scene scene = new Scene(ui, 1100, 820);
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Chỉnh sửa Yêu cầu");
        stage.setScene(scene);
        stage.show();
    }
}
