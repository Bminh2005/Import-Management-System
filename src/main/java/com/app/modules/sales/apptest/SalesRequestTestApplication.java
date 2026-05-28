package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.request.ui.EditRequestUI;
import com.app.modules.sales.request.ui.RequestDetailUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test app cho 2 màn "Xem chi tiết Yêu cầu" và "Chỉnh sửa Yêu cầu",
 * nhúng vào trong MainLayoutUI để có sidebar + header.
 *
 * Tham số dòng lệnh:
 *   (mặc định)  -> mở màn Chỉnh sửa REQ-2024-001
 *   "view"      -> mở màn Xem chi tiết
 *   "view <ma>" / "edit <ma>" -> chỉ định mã yêu cầu khác
 */
public class SalesRequestTestApplication extends Application {

    private static final String DEFAULT_CODE = "REQ-2024-001";

    private MainLayoutUI layout;

    @Override
    public void start(Stage stage) {
        layout = new MainLayoutUI();

        String screen = "edit";
        String code = DEFAULT_CODE;
        if (!getParameters().getRaw().isEmpty()) {
            screen = getParameters().getRaw().get(0);
        }
        if (getParameters().getRaw().size() > 1) {
            code = getParameters().getRaw().get(1);
        }

        if ("view".equalsIgnoreCase(screen)) {
            showDetail(code);
        } else {
            showEdit(code);
        }

        Scene scene = new Scene(layout, 1440, 900);
        stage.setTitle("Hệ thống Quản lý Nhập khẩu");
        stage.setMinWidth(1180);
        stage.setMinHeight(760);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> DatabaseManager.shutdown());
        stage.show();
    }

    private void showDetail(String code) {
        RequestDetailUI page = new RequestDetailUI();
        page.setOnBack(() -> System.out.println("Nội dung chức năng: Đóng chi tiết yêu cầu"));
        page.setOnEdit(this::showEdit);
        page.loadRequest(code);
        layout.setPage(page);
    }

    private void showEdit(String code) {
        EditRequestUI page = new EditRequestUI();
        page.setOnBack(() -> showDetail(code));
        page.setOnSaved(c -> System.out.println("Nội dung chức năng: Đã lưu " + c));
        page.loadRequest(code);
        layout.setPage(page);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
