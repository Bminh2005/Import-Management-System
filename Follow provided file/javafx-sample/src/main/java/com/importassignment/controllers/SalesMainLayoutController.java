package com.importassignment.controllers;

import com.importassignment.SalesMainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main Layout Controller for Sales Department
 */
public class SalesMainLayoutController implements Initializable {

    @FXML private VBox sidebar;
    @FXML private StackPane contentArea;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private TextField searchField;

    // Sidebar buttons
    @FXML private Button dashboardButton;
    @FXML private Button merchandiseButton;
    @FXML private Button requestsButton;

    @FXML private Label roleLabel;

    private ResourceBundle resources;
    private String currentView = "dashboard";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        setupLanguageComboBox();
        setupSidebar();
        loadView("dashboard");
    }

    private void setupLanguageComboBox() {
        languageComboBox.getItems().addAll("Tiếng Việt", "English");

        // Set initial value based on current locale
        Locale currentLocale = SalesMainApp.getCurrentLocale();
        if (currentLocale.getLanguage().equals("vi")) {
            languageComboBox.setValue("Tiếng Việt");
        } else {
            languageComboBox.setValue("English");
        }

        // Language change listener
        languageComboBox.setOnAction(event -> onLanguageChange());
    }

    private void setupSidebar() {
        // Set initial active button
        setActiveButton(dashboardButton);
    }

    @FXML
    private void onDashboardClick() {
        loadView("dashboard");
        setActiveButton(dashboardButton);
    }

    @FXML
    private void onMerchandiseClick() {
        loadView("merchandise");
        setActiveButton(merchandiseButton);
    }

    @FXML
    private void onRequestsClick() {
        loadView("requests");
        setActiveButton(requestsButton);
    }

    private void setActiveButton(Button activeButton) {
        // Clear all active states
        dashboardButton.getStyleClass().remove("sidebar-btn-active");
        merchandiseButton.getStyleClass().remove("sidebar-btn-active");
        requestsButton.getStyleClass().remove("sidebar-btn-active");

        // Set active state
        if (!activeButton.getStyleClass().contains("sidebar-btn-active")) {
            activeButton.getStyleClass().add("sidebar-btn-active");
        }
    }

    private void loadView(String viewName) {
        try {
            currentView = viewName;
            String fxmlPath = null;

            switch (viewName) {
                case "dashboard":
                    fxmlPath = "/fxml/SalesDashboard.fxml";
                    break;
                case "merchandise":
                    fxmlPath = "/fxml/MerchandiseManagement.fxml";
                    break;
                case "requests":
                    fxmlPath = "/fxml/ImportRequestsView.fxml";
                    break;
            }

            if (fxmlPath != null) {
                FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxmlPath),
                    resources
                );
                Parent view = loader.load();
                contentArea.getChildren().setAll(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể tải view: " + viewName);
        }
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

        SalesMainApp.changeLanguage(newLocale);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
