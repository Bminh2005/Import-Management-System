package com.app.auth.login.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SignupUI extends StackPane {
    @FXML
    private TextField fullName;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Button signupButton;
    @FXML
    private Hyperlink loginLink;

    public SignupUI() {
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("com/app/common/ui/signup.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        signupButton.setOnAction(e -> {
            System.out.println("FULLNAME:" + this.getFullName());
            System.out.println("EMAIL:" + this.getEmail());
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

    public String getEmail() {
        return email.getText();
    }

    public String getPassword() {
        return password.getText();
    }

    public String getRole() {
        return roleComboBox.getValue();
    }


}
