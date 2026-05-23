package com.app.common.ui.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class WarehouseTopBarUI extends HBox {
    @FXML
    private TextField searchField;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label roleLabel;

    private String userName = "Nguyen Van A";
    private String role = "Bo phan Quan ly Kho";

    public WarehouseTopBarUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WarehouseTopBar.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Khong the tai WarehouseTopBar.fxml", exception);
        }
    }

    @FXML
    private void initialize() {
        bindUserInfo();
    }

    @FXML
    private void onLanguageClick() {
        System.out.println("Noi dung chuc nang: Doi ngon ngu");
    }

    @FXML
    private void onNotificationClick() {
        System.out.println("Noi dung chuc nang: Xem thong bao");
    }

    @FXML
    private void onUserMenuClick() {
        System.out.println("Noi dung chuc nang: Mo menu nguoi dung");
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
