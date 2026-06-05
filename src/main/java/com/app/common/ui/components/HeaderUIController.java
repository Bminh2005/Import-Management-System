package com.app.common.ui.components;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class HeaderUIController {
    @FXML
    private StackPane notificationBell;
    @FXML
    private HBox userProfileBox;

    private UserMenu userMenu;

    public void initialize() {
        userMenu = new UserMenu();
        userProfileBox.setOnMouseClicked(e -> {
            if (userMenu.isShowing()) {
                userMenu.hide();
                return;
            }
            userMenu.show(userProfileBox, javafx.geometry.Side.BOTTOM, 0, 5);
        });

    }

    private MenuItem createLogoutMenuItem() {
        MenuItem logoutItem = new MenuItem("Đăng xuất");
        logoutItem.setStyle("-fx-text-fill: red; -fx-font-weight: bold;"); // Chữ đỏ
        logoutItem.setOnAction(e -> System.out.println("Đang đăng xuất..."));
        return logoutItem;
    }

}
