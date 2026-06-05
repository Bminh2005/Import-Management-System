package com.app.modules.procurement.importorder.ui;

import com.app.common.ui.IScreen;
import com.app.database.manager.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UI Class cho màn "Trang Chủ - Bộ phận Đặt hàng Quốc tế".
 * Kế thừa ScrollPane và nạp FXML thông qua fx:root.
 */
public class ProcurementDashboardUI extends ScrollPane implements IScreen {

    @FXML private Label totalRequestsLabel;
    @FXML private Label pendingAllocationLabel;
    @FXML private Label allocatedThisMonthLabel;
    @FXML private Label activeSitesLabel;

    public ProcurementDashboardUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProcurementDashboardPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderMetrics();
    }

    private void renderMetrics() {
        totalRequestsLabel.setText(String.valueOf(getTotalRequestsCount()));
        pendingAllocationLabel.setText(String.valueOf(getPendingRequestsCount()));
        allocatedThisMonthLabel.setText(String.valueOf(getAllocatedRequestsCount()));
        activeSitesLabel.setText(String.valueOf(getSitesCount()));
    }

    private int getPendingRequestsCount() {
        String sql = "SELECT COUNT(*) FROM \"ImportRequest\" WHERE status = 'PENDING'::request_status";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getSitesCount() {
        String sql = "SELECT COUNT(*) FROM \"Site\"";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTotalRequestsCount() {
        String sql = "SELECT COUNT(*) FROM \"ImportRequest\"";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getAllocatedRequestsCount() {
        String sql = "SELECT COUNT(*) FROM \"ImportRequest\" WHERE status = 'PROCESSED'::request_status";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @FXML
    private void onProcessPendingRequests(MouseEvent event) {
        System.out.println("Nội dung chức năng: Mở màn xử lý yêu cầu đang chờ phân bổ");

        ProcessImportRequestsUI root = new ProcessImportRequestsUI();

        Scene scene = totalRequestsLabel.getScene();
        if (scene != null) {
            javafx.scene.Parent parentRoot = scene.getRoot();
            if (parentRoot instanceof com.app.common.ui.MainLayoutUI) {
                ((com.app.common.ui.MainLayoutUI) parentRoot).setPage(root);
            } else {
                scene.setRoot(root);
            }
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Xử lý Yêu cầu nhập hàng");
        }
    }

    @FXML
    private void onViewSites(MouseEvent event) {
        System.out.println("Nội dung chức năng: Mở danh sách site / nhà cung cấp");
        com.app.modules.site.catalog.ui.SitesListUI sitesList = new com.app.modules.site.catalog.ui.SitesListUI();
        sitesList.setOnViewDetail(siteId -> {
            com.app.modules.site.catalog.ui.SiteDetailUI siteDetail = new com.app.modules.site.catalog.ui.SiteDetailUI();
            siteDetail.loadSite(siteId);
            siteDetail.setOnBack(() -> {
                Scene scene = activeSitesLabel.getScene();
                if (scene != null && scene.getRoot() instanceof com.app.common.ui.MainLayoutUI) {
                    ((com.app.common.ui.MainLayoutUI) scene.getRoot()).setPage(sitesList);
                }
            });
            Scene scene = activeSitesLabel.getScene();
            if (scene != null && scene.getRoot() instanceof com.app.common.ui.MainLayoutUI) {
                ((com.app.common.ui.MainLayoutUI) scene.getRoot()).setPage(siteDetail);
            }
        });

        Scene scene = activeSitesLabel.getScene();
        if (scene != null) {
            javafx.scene.Parent parentRoot = scene.getRoot();
            if (parentRoot instanceof com.app.common.ui.MainLayoutUI) {
                ((com.app.common.ui.MainLayoutUI) parentRoot).setPage(sitesList);
            } else {
                scene.setRoot(sitesList);
            }
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Danh sách Site");
        }
    }

    @Override
    public Parent getRoot() {
        return this;
    }
}
