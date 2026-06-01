package com.importassignment.controllers;

import com.importassignment.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private VBox navigationMenu;

    @FXML
    private Label currentRoleLabel;

    @FXML
    private Button toggleButton;

    private ResourceBundle resourceBundle;
    private boolean isCollapsed = false;

    private static class MenuItem {
        String icon;
        String key;
        String view;
        String shortcut;

        MenuItem(String icon, String key, String view, String shortcut) {
            this.icon = icon;
            this.key = key;
            this.view = view;
            this.shortcut = shortcut;
        }
    }

    private final MenuItem[] menuItems = {
        new MenuItem("🏠", "menu.dashboard", "Dashboard", "Ctrl+1"),
        new MenuItem("🛒", "menu.orders", "OrdersList", "Ctrl+2"),
        new MenuItem("🌍", "menu.sites", "SiteManagement", "Ctrl+3"),
        new MenuItem("📦", "menu.warehouse", "Warehouse", "Ctrl+4"),
        new MenuItem("📊", "menu.reports", "Reports", "Ctrl+5"),
        new MenuItem("⚙️", "menu.settings", "Settings", "Ctrl+,")
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        buildNavigationMenu();
        updateRoleLabel();
    }

    private void updateRoleLabel() {
        // Update role label based on current language
        if (resourceBundle.getLocale().getLanguage().equals("vi")) {
            currentRoleLabel.setText("Bộ phận Bán hàng");
        } else {
            currentRoleLabel.setText("Sales Department");
        }
    }

    private void buildNavigationMenu() {
        navigationMenu.getChildren().clear();

        for (MenuItem item : menuItems) {
            Button menuButton = createMenuButton(item);
            navigationMenu.getChildren().add(menuButton);
        }
    }

    private Button createMenuButton(MenuItem item) {
        Button button = new Button();
        button.setMaxWidth(Double.MAX_VALUE);
        button.getStyleClass().add("menu-item");

        // Create button content
        HBox content = new HBox(12);
        content.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label iconLabel = new Label(item.icon);
        iconLabel.setStyle("-fx-font-size: 18px;");

        Label textLabel = new Label(resourceBundle.getString(item.key));
        textLabel.getStyleClass().add("menu-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Label shortcutLabel = new Label(item.shortcut);
        shortcutLabel.getStyleClass().add("shortcut-hint");
        shortcutLabel.setStyle("-fx-font-size: 10px; -fx-opacity: 0.6;");

        content.getChildren().addAll(iconLabel, textLabel, spacer, shortcutLabel);
        button.setGraphic(content);

        // Click handler
        button.setOnAction(e -> loadView(item.view));

        return button;
    }

    private void loadView(String viewName) {
        try {
            // Find MainLayoutController by traversing up the scene graph
            javafx.scene.Parent root = MainApp.getPrimaryStage().getScene().getRoot();

            // The root should be BorderPane from MainLayout
            if (root instanceof javafx.scene.layout.BorderPane) {
                javafx.scene.layout.BorderPane borderPane = (javafx.scene.layout.BorderPane) root;
                javafx.scene.Node center = borderPane.getCenter();

                if (center instanceof javafx.scene.layout.VBox) {
                    javafx.scene.layout.VBox vbox = (javafx.scene.layout.VBox) center;

                    // Find StackPane (contentContainer)
                    for (javafx.scene.Node node : vbox.getChildren()) {
                        if (node instanceof javafx.scene.layout.StackPane) {
                            javafx.scene.layout.StackPane contentContainer = (javafx.scene.layout.StackPane) node;

                            // Load new view into content container
                            FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/fxml/" + viewName + ".fxml"),
                                MainApp.getResourceBundle()
                            );
                            javafx.scene.Parent view = loader.load();
                            contentContainer.getChildren().setAll(view);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải view: " + viewName);
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleSidebar() {
        isCollapsed = !isCollapsed;

        if (isCollapsed) {
            toggleButton.setText("►");
            // Hide text labels, show only icons
            navigationMenu.setPrefWidth(80);
        } else {
            toggleButton.setText("◄");
            navigationMenu.setPrefWidth(280);
        }
    }
}
