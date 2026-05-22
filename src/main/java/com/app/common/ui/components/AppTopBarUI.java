package com.app.common.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * UI Class cho thanh trên cùng (TopBar) dùng chung:
 * ô tìm kiếm toàn cục, nút đổi ngôn ngữ, chuông thông báo và avatar user.
 */
public class AppTopBarUI extends HBox {

    @FXML private TextField searchField;
    @FXML private Label userNameLabel;
    @FXML private Label roleLabel;

    private String userName = "Nguyễn Văn A";
    private String role = "Bộ phận Bán hàng";

    public AppTopBarUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppTopBar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        applyUserInfo();
    }

    public String getSearchKeyword() {
        return searchField == null ? "" : searchField.getText();
    }

    public void setSearchKeyword(String keyword) {
        if (searchField != null) searchField.setText(keyword);
    }

    public void setUserName(String userName) {
        this.userName = userName;
        applyUserInfo();
    }

    public void setRole(String role) {
        this.role = role;
        applyUserInfo();
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

    private void applyUserInfo() {
        if (userNameLabel != null) userNameLabel.setText(userName);
        if (roleLabel != null) roleLabel.setText(role);
    }
}
