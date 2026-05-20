package com.app.Ioms;

import com.app.modules.sales.request.ui.RequestDetailController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Điểm chạy thử feature sales/request: mở thẳng màn
 * "Chi tiết Yêu cầu Nhập hàng" với mã mẫu trong RequestRepository.
 *
 * Từ màn chi tiết bấm "Chỉnh sửa" sẽ sang EditRequestPage;
 * trên đó bấm "Quay lại" sẽ trở về màn chi tiết.
 *
 * Cách chạy:
 * - IDE: right-click {@link SalesRequestLauncher} → Run.
 * - Terminal: mvn javafx:run.
 */
public class SalesRequestApp extends Application {

    private static final String DEFAULT_REQUEST_CODE = "REQ-2024-001";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/com/app/modules/sales/request/ui/RequestDetailPage.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 820);

        RequestDetailController controller = loader.getController();
        controller.loadRequest(DEFAULT_REQUEST_CODE);

        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Chi tiết Yêu cầu");
        stage.setScene(scene);
        stage.show();
    }
}
