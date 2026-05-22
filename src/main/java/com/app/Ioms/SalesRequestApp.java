package com.app.Ioms;

import com.app.modules.sales.request.ui.RequestDetailUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Điểm chạy thử feature sales/request: mở thẳng màn
 * "Chi tiết Yêu cầu Nhập hàng" với mã mẫu trong RequestRepository.
 *
 * Từ màn chi tiết bấm "Chỉnh sửa" sẽ sang EditRequestUI;
 * trên đó bấm "Quay lại" sẽ trở về màn chi tiết.
 *
 * Cách chạy:
 * - IDE: right-click {@link SalesRequestLauncher} → Run.
 * - Terminal: mvn javafx:run.
 */
public class SalesRequestApp extends Application {

    private static final String DEFAULT_REQUEST_CODE = "REQ-2024-001";

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
