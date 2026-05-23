package com.app.modules.procurement.importorder.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * UI Class cho màn "Trang Chủ - Bộ phận Đặt hàng Quốc tế".
 * Kế thừa ScrollPane và nạp FXML thông qua fx:root.
 */
public class ProcurementDashboardUI extends ScrollPane {

    @FXML private Label totalRequestsLabel;
    @FXML private Label pendingAllocationLabel;
    @FXML private Label allocatedThisMonthLabel;
    @FXML private Label activeSitesLabel;

    @FXML private VBox recentRequestsList;

    private final List<RecentRequestRow> recentRequests = new ArrayList<>();

    public ProcurementDashboardUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProcurementDashboardPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadDashboardData();
        renderMetrics();
        renderRecentRequests();
    }

    private void loadDashboardData() {
        recentRequests.clear();

        recentRequests.add(new RecentRequestRow(
                "REQ-2024-004",
                "2024-05-10",
                "Chưa phân bổ",
                "pending"
        ));

        recentRequests.add(new RecentRequestRow(
                "REQ-2024-002",
                "2024-05-09",
                "Amazon US",
                "allocated"
        ));

        recentRequests.add(new RecentRequestRow(
                "REQ-2024-001",
                "2024-05-08",
                "Alibaba CN",
                "allocated"
        ));
    }

    private void renderMetrics() {
        totalRequestsLabel.setText("156");
        pendingAllocationLabel.setText("24");
        allocatedThisMonthLabel.setText("132");
        activeSitesLabel.setText("18");
    }

    private void renderRecentRequests() {
        recentRequestsList.getChildren().clear();

        for (RecentRequestRow request : recentRequests) {
            recentRequestsList.getChildren().add(buildRecentRequestRow(request));
        }
    }

    private GridPane buildRecentRequestRow(RecentRequestRow request) {
        GridPane row = new GridPane();
        row.getStyleClass().add("table-row");
        row.setHgap(12);
        applyRecentTableColumns(row);

        Label codeLabel = new Label(request.code());
        codeLabel.getStyleClass().add("request-code");
        row.add(codeLabel, 0, 0);

        Label dateLabel = new Label(request.receivedDate());
        dateLabel.getStyleClass().add("table-cell-muted");
        row.add(dateLabel, 1, 0);

        Label siteLabel = new Label(request.allocatedSite());
        siteLabel.getStyleClass().add("table-cell-text");
        row.add(siteLabel, 2, 0);

        Label statusLabel = new Label(getStatusLabel(request.status()));
        statusLabel.getStyleClass().addAll("status-badge", getStatusStyleClass(request.status()));

        HBox statusBox = new HBox(statusLabel);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        row.add(statusBox, 3, 0);

        Button detailButton = new Button("Xem chi tiết");
        detailButton.getStyleClass().add("row-action-button");
        detailButton.setOnAction(event -> onViewRequestDetail(request.code()));

        HBox actionBox = new HBox(detailButton);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        row.add(actionBox, 4, 0);
        GridPane.setHalignment(actionBox, HPos.RIGHT);

        return row;
    }

    private void applyRecentTableColumns(GridPane grid) {
        addColumn(grid, 22);
        addColumn(grid, 18);
        addColumn(grid, 25);
        addColumn(grid, 20);
        addColumn(grid, 15);
    }

    private void addColumn(GridPane grid, double percent) {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(percent);
        grid.getColumnConstraints().add(column);
    }

    private String getStatusLabel(String status) {
        return switch (status) {
            case "pending" -> "Chờ phân bổ";
            case "allocated" -> "Đã phân bổ";
            default -> "Không xác định";
        };
    }

    private String getStatusStyleClass(String status) {
        return switch (status) {
            case "pending" -> "status-pending";
            case "allocated" -> "status-allocated";
            default -> "status-unknown";
        };
    }

    @FXML
    private void onProcessPendingRequests(MouseEvent event) {
        System.out.println("Nội dung chức năng: Mở màn xử lý yêu cầu đang chờ phân bổ");

        ProcessImportRequestsUI root = new ProcessImportRequestsUI();

        Scene scene = recentRequestsList.getScene();
        if (scene != null) {
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Xử lý Yêu cầu nhập hàng");
        }
    }

    @FXML
    private void onViewSites(MouseEvent event) {
        System.out.println("Nội dung chức năng: Mở danh sách site / nhà cung cấp");
    }

    @FXML
    private void onViewAllRequests() {
        System.out.println("Nội dung chức năng: Xem tất cả yêu cầu cần xử lý");
    }

    private void onViewRequestDetail(String requestCode) {
        System.out.println("Nội dung chức năng: Xem chi tiết yêu cầu " + requestCode);
    }

    private record RecentRequestRow(
            String code,
            String receivedDate,
            String allocatedSite,
            String status
    ) {
    }
}
