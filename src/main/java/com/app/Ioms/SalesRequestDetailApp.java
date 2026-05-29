package com.app.Ioms;

import com.app.modules.sales.request.ui.RequestDetailUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App test cho màn "Chi tiết Yêu cầu Nhập hàng" (RequestDetailUI).
 * Mở thẳng màn chi tiết với một mã yêu cầu mẫu trong RequestRepository.
 * Màn này độc lập, thoát bằng nút "Đóng".
 *
 * Cách chạy: right-click {@link SalesRequestDetailLauncher} → Run.
 */
public class SalesRequestDetailApp extends Application {

    private static final String DEFAULT_REQUEST_CODE = "4";

    @Override
    public void start(Stage stage) {
        RequestDetailUI ui = new RequestDetailUI();
        ui.loadRequest(DEFAULT_REQUEST_CODE);

        Scene scene = new Scene(ui, 1100, 820);
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Chi tiết Yêu cầu");
        stage.setScene(scene);
        stage.show();
    }
}
