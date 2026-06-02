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

import java.io.IOException;

public class SignupUI extends StackPane implements IScreen {
    @FXML
    private TextField fullName;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<RoleItem> roleComboBox;
    @FXML
    private Button signupButton;
    @FXML
    private Hyperlink loginLink;

    public SignupUI() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/app/common/ui/signup.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        signupButton.setOnAction(e -> {
            System.out.println("FULLNAME:" + this.getFullName());
            System.out.println("EMAIL:" + this.getUsername());
            System.out.println("PASSWORD:" + this.getPassword());
            System.out.println("ROLE:" + this.getRole());
        });
        loginLink.setOnAction(e -> {
            System.out.println("Navigated to Login Page");
        });
    }

    public String getFullName() {
        return fullName.getText();
    }

    public String getUsername() {
        return username.getText();
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

    public void setSignupButtonAction(Runnable r){
        signupButton.setOnAction(e -> {
            r.run();
        });
    }
    public void setOnActionForLoginLink(Runnable r){
        loginLink.setOnAction(e -> {
            r.run();
        });
    }
    public void showToastNotification(String message, boolean isSuccess) {
        ToastNotification.show(this.getScene().getWindow(), message, isSuccess);
    }
    @Override
    public Parent getRoot() {
        return this;
    }
}
