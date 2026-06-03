package com.app.common.ui.components;

import com.app.auth.security.CurrentUserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class HeaderUIController {
    @FXML
    private StackPane notificationBell;
    @FXML
    private HBox userProfileBox;
    @FXML
    private Label fullnameLabel;
    @FXML
    private Label roleLabel;
    private UserMenu userMenu;

    public void initialize() {
        if(CurrentUserSession.getFullname() != null) {
            fullnameLabel.setText(CurrentUserSession.getFullname());
            roleLabel.setText(CurrentUserSession.getRole());
        }
        userMenu = new UserMenu();
        userProfileBox.setOnMouseClicked(e -> {
            if (userMenu.isShowing()) {
                userMenu.hide();
                return;
            }
            userMenu.show(userProfileBox, javafx.geometry.Side.BOTTOM, 0, 5);
        });

    }

//    private MenuItem createLogoutMenuItem() {
//        MenuItem logoutItem = new MenuItem("Đăng xuất");
//        logoutItem.setStyle("-fx-text-fill: red; -fx-font-weight: bold;"); // Chữ đỏ
//        logoutItem.setOnAction(e -> System.out.println("Đang đăng xuất..."));
//        return logoutItem;
//    }

}
