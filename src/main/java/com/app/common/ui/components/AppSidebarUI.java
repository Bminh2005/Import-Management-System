package com.app.common.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * UI Class cho thanh điều hướng bên trái (sidebar) dùng chung
 * cho mọi page của hệ thống. Chứa danh sách menu chính, panel vai trò
 * và nút thu gọn.
 *
 * Active menu được set qua {@link #setActive(String)} với các key:
 * "home", "request", "inventory", "reports", "settings".
 */
public class AppSidebarUI extends VBox {

    public static final String KEY_HOME = "home";
    public static final String KEY_REQUEST = "request";
    public static final String KEY_INVENTORY = "inventory";
    public static final String KEY_REPORTS = "reports";
    public static final String KEY_SETTINGS = "settings";

    @FXML private Button homeButton;
    @FXML private Button requestButton;
    @FXML private Button inventoryButton;
    @FXML private Label roleLabel;
    @FXML private Label brandSubtitleLabel;

    private String activeKey = KEY_HOME;
    private Runnable onHomeAction;
    private Runnable onRequestAction;
    private Runnable onInventoryAction;

    public AppSidebarUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppSidebar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        applyActive();
    }

    /** Đánh dấu menu đang được chọn. */
    public void setActive(String key) {
        this.activeKey = key;
        applyActive();
    }

    /** Tên vai trò hiển thị ở panel dưới sidebar. */
    public void setRole(String role) {
        if (roleLabel != null) roleLabel.setText(role);
    }

    /** Phụ đề dưới tên brand (ví dụ: "Bộ phận Bán hàng"). */
    public void setBrandSubtitle(String subtitle) {
        if (brandSubtitleLabel != null) brandSubtitleLabel.setText(subtitle);
    }

    public void setOnHome(Runnable r) { this.onHomeAction = r; }
    public void setOnRequest(Runnable r) { this.onRequestAction = r; }
    public void setOnInventory(Runnable r) { this.onInventoryAction = r; }

    @FXML
    private void onHomeClick() {
        System.out.println("Nội dung chức năng: Mở Trang chủ");
        if (onHomeAction != null) onHomeAction.run();
    }

    @FXML
    private void onRequestClick() {
        System.out.println("Nội dung chức năng: Mở Yêu cầu Nhập hàng");
        if (onRequestAction != null) onRequestAction.run();
    }

    @FXML
    private void onInventoryClick() {
        System.out.println("Nội dung chức năng: Mở Mặt hàng Tồn kho");
        if (onInventoryAction != null) onInventoryAction.run();
    }

    @FXML
    private void onReportsClick() {
        System.out.println("Nội dung chức năng: Mở Báo cáo");
    }

    @FXML
    private void onSettingsClick() {
        System.out.println("Nội dung chức năng: Mở Cài đặt");
    }

    @FXML
    private void onSwitchDepartmentClick() {
        System.out.println("Nội dung chức năng: Đổi bộ phận");
    }

    @FXML
    private void onCollapseClick() {
        System.out.println("Nội dung chức năng: Thu gọn sidebar");
    }

    private void applyActive() {
        if (homeButton == null) return;
        homeButton.getStyleClass().remove("active");
        requestButton.getStyleClass().remove("active");
        inventoryButton.getStyleClass().remove("active");
        switch (activeKey == null ? "" : activeKey) {
            case KEY_REQUEST:
                requestButton.getStyleClass().add("active"); break;
            case KEY_INVENTORY:
                inventoryButton.getStyleClass().add("active"); break;
            case KEY_HOME:
            default:
                homeButton.getStyleClass().add("active"); break;
        }
    }
}
