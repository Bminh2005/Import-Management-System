package com.app.auth.login.ui;

import com.app.common.ui.IScreen;
import com.app.common.ui.components.ToastNotification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import javax.management.relation.Role;
import java.io.IOException;

public class LoginUI extends StackPane implements IScreen {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<RoleItem> roleComboBox;
    @FXML
    private Button loginButton;
    @FXML
    private Hyperlink signupLink;

    public LoginUI() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/app/common/ui/Login.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        signupLink.setOnAction(e -> {
            System.out.println("Navigated to Signup Page");
        });
    }


    public String getPassword() {
        return password.getText();
    }

    public String getRole() {
        RoleItem item = roleComboBox.getValue();

        if(item == null){
            return null;
        }

        return item.getValue();
    }

    public String getUsername() {
        return username.getText();
    }

    public void setLoginButtonAction(Runnable r){
        loginButton.setOnAction(e -> {
            r.run();
        });
    }
    public void showToastNotification(String message, boolean isSuccess) {
        ToastNotification.show(this.getScene().getWindow(), message, isSuccess);
    }

    public void setOnActionForSignupLink(Runnable r){
        signupLink.setOnAction(e -> {
            r.run();
        });
    }

    @Override
    public Parent getRoot() {
        return this;
    }
}
