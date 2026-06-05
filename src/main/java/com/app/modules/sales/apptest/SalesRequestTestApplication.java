package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.dashboard.ui.SalesShell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test app cho 2 màn "Xem chi tiết Yêu cầu" và "Chỉnh sửa Yêu cầu",
 * nhúng vào trong MainLayoutUI để có sidebar + header.
 *
 * Tham số dòng lệnh:
 *   (mặc định)  -> mở màn Chỉnh sửa yêu cầu mã 1
 *   "view"      -> mở danh sách (bấm mắt để xem chi tiết)
 *   "edit <ma>" -> mở thẳng màn chỉnh sửa
 */
public class SalesRequestTestApplication extends Application {

    private static final String DEFAULT_CODE = "4";

    private MainLayoutUI layout;
    private SalesShell shell;

    @Override
    public void start(Stage stage) {
        layout = new MainLayoutUI();
        shell = new SalesShell(layout);

        String screen = "edit";
        String code = DEFAULT_CODE;
        if (!getParameters().getRaw().isEmpty()) {
            screen = getParameters().getRaw().get(0);
        }
        if (getParameters().getRaw().size() > 1) {
            code = getParameters().getRaw().get(1);
        }

        if ("view".equalsIgnoreCase(screen)) {
            shell.showRequestList();
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

    private void showEdit(String code) {
        shell.showRequestEdit(code);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
