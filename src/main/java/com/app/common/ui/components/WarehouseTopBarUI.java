package com.app.common.ui.components;

import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class WarehouseTopBarUI extends HBox {
    @FXML
    private TextField searchField;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label roleLabel;

    private String userName = "Nguyễn Văn A";
    private String role = "Bộ phận Quản lý Kho";

    public WarehouseTopBarUI() {
        FxmlUiHelper.loadSelf(this, "WarehouseTopBar.fxml");
    }

    @FXML
    private void initialize() {
        bindUserInfo();
    }

    @FXML
    private void onLanguageClick() {
        System.out.println("Nội dung chức năng: Đổi ngôn ngữ");
    }

    @FXML
    private void onNotificationClick() {
        System.out.println("Nội dung chức năng: Xem thông báo");
    }

    @FXML
    private void onUserMenuClick() {
        System.out.println("Nội dung chức năng: Mở menu người dùng");
    }

    public String getSearchKeyword() {
        return searchField == null ? "" : searchField.getText();
    }

    public void setSearchKeyword(String searchKeyword) {
        if (searchField != null) {
            searchField.setText(searchKeyword);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        bindUserInfo();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
        bindUserInfo();
    }

    private void bindUserInfo() {
        if (userNameLabel != null) {
            userNameLabel.setText(userName);
        }
        if (roleLabel != null) {
            roleLabel.setText(role);
        }
    }
}
