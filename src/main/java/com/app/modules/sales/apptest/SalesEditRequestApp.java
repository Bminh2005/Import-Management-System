package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.dashboard.ui.SalesSidebar;
import com.app.modules.sales.request.editrequest.ui.EditRequestController;
import com.app.modules.sales.request.editrequest.ui.EditRequestUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App test cho màn "Chỉnh sửa Yêu cầu Nhập hàng" (EditRequestUI).
 * Mở màn chỉnh sửa trong layout chung (sidebar + nội dung) với một mã
 * yêu cầu mẫu trong RequestRepository.
 *
 * Cách chạy: right-click {@link SalesEditRequestLauncher} → Run.
 */
public class SalesEditRequestApp extends Application {

    private static final String DEFAULT_REQUEST_CODE = "6";

    @Override
    public void start(Stage stage) {
        EditRequestUI ui = new EditRequestUI();
        new EditRequestController(ui);
        ui.loadRequest(DEFAULT_REQUEST_CODE);

        MainLayoutUI root = new MainLayoutUI();
        root.setLeft(new SalesSidebar());
        root.setPage(ui);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("/com/app/common/ui/components/common.css").toExternalForm());
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Chỉnh sửa Yêu cầu");
        stage.setScene(scene);
        stage.show();
    }
}
