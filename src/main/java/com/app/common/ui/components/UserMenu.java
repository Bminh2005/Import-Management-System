package com.app.common.ui.components;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class UserMenu extends ContextMenu {

    public UserMenu() {
        // Tạo các Menu Item
        MenuItem profileItem = new MenuItem("Hồ sơ cá nhân");
        MenuItem settingsItem = new MenuItem("Cài đặt");

        // Đường kẻ phân cách
        SeparatorMenuItem separator = new SeparatorMenuItem();

        MenuItem logoutItem = new MenuItem("Đăng xuất");

        // Định dạng mục Đăng xuất (màu đỏ)
        logoutItem.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // Gán sự kiện (Bạn có thể truyền Lambda hoặc Interface vào constructor để xử lý logic)
        profileItem.setOnAction(e -> System.out.println("Mở Hồ sơ..."));
        settingsItem.setOnAction(e -> System.out.println("Mở Cài đặt..."));
        logoutItem.setOnAction(e -> System.out.println("Đang đăng xuất..."));

        // Thêm vào ContextMenu
        this.getItems().addAll(profileItem, settingsItem, separator, logoutItem);

        // Tùy chỉnh CSS cho cả menu (tùy chọn)
        this.getStyleClass().add("user-context-menu");
    }
}
