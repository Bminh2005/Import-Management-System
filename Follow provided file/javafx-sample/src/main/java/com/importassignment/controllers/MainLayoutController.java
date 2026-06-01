package com.importassignment.controllers;

import com.importassignment.MainApp;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Button notificationButton;

    @FXML
    private StackPane contentContainer;

    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;

        // Setup language combo box
        languageComboBox.setItems(FXCollections.observableArrayList("Tiếng Việt", "English"));

        // Set initial value based on current locale
        Locale currentLocale = resources.getLocale();
        if (currentLocale.getLanguage().equals("vi")) {
            languageComboBox.setValue("Tiếng Việt");
        } else {
            languageComboBox.setValue("English");
        }

        // Setup search field with keyboard shortcut hint
        searchField.setPromptText(resources.getString("action.search") + " (Ctrl+F)");

        // Load Dashboard by default
        loadView("Dashboard");
    }

    @FXML
    private void onLanguageChange() {
        String selected = languageComboBox.getValue();
        Locale newLocale;

        if ("Tiếng Việt".equals(selected)) {
            newLocale = new Locale("vi", "VN");
        } else {
            newLocale = new Locale("en", "US");
        }

        MainApp.changeLanguage(newLocale);
    }

    public void loadView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/" + viewName + ".fxml"),
                MainApp.getResourceBundle()
            );
            Parent view = loader.load();
            contentContainer.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể tải view: " + viewName);
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
