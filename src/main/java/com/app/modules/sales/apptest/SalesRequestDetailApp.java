package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.modules.sales.dashboard.ui.SalesSidebar;
import com.app.modules.sales.request.requestdetail.ui.RequestDetailUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App test cho màn "Chi tiết Yêu cầu Nhập hàng" (RequestDetailUI).
 * Mở màn chi tiết trong layout chung (sidebar + nội dung) với một mã
 * yêu cầu mẫu trong RequestRepository.
 *
 * Cách chạy: right-click {@link SalesRequestDetailLauncher} → Run.
 */
public class SalesRequestDetailApp extends Application {

    private static final String DEFAULT_REQUEST_CODE = "4";

    @Override
    public void start(Stage stage) {
        RequestDetailUI ui = new RequestDetailUI();
        ui.loadRequest(DEFAULT_REQUEST_CODE);

        MainLayoutUI root = new MainLayoutUI();
        root.setLeft(new SalesSidebar());
        root.setPage(ui);

        Scene scene = new Scene(root);
        // Theme (biến màu -primary-color, -border-color...) định nghĩa trong common.css
        // dưới selector .root -> phải gắn ở cấp Scene để page nhúng qua setPage kế thừa được.
        scene.getStylesheets().add(
                getClass().getResource("/com/app/common/ui/components/common.css").toExternalForm());
        stage.setTitle("Hệ thống Quản lý Nhập khẩu - Chi tiết Yêu cầu");
        stage.setScene(scene);
        stage.show();
    }
}
