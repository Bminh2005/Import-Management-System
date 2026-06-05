package com.app.modules.sales.apptest;

import com.app.common.ui.MainLayoutUI;
import com.app.database.manager.DatabaseManager;
import com.app.modules.sales.dashboard.ui.SalesShell;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App test cho use case <b>Xem chi tiết thông tin yêu cầu nhập hàng</b>.
 *
 * <p>Luồng: Danh sách yêu cầu → bấm icon mắt → Chi tiết → Quay lại danh sách.</p>
 *
 * <p>Chạy: Run {@link SalesRequestDetailLauncher}</p>
 */
public class SalesRequestDetailApp extends Application {

    private MainLayoutUI layout;
    private SalesShell shell;

    @Override
    public void start(Stage stage) {
        layout = new MainLayoutUI();
        shell = new SalesShell(layout);
        shell.showRequestList();

        Scene scene = new Scene(layout, 1280, 860);
        stage.setTitle("UC: Yêu cầu Nhập hàng — Danh sách → Xem chi tiết");
        stage.setMinWidth(1100);
        stage.setMinHeight(720);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> DatabaseManager.shutdown());
        stage.show();
    }
}
