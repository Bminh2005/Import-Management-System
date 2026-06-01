package com.importassignment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple I18n Test - Test đơn giản để kiểm tra chuyển đổi ngôn ngữ
 * Chạy class này để test i18n trước khi chạy app chính
 */
public class SimpleI18nTest extends Application {

    private ResourceBundle bundle;
    private Locale currentLocale = new Locale("vi", "VN");

    // UI Components
    private Label titleLabel;
    private Label menuLabel;
    private Label ordersLabel;
    private Label welcomeLabel;
    private Button switchButton;

    @Override
    public void start(Stage primaryStage) {
        // Load initial bundle
        loadBundle();

        // Create UI
        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #F9FAFB;");

        // Title
        Label header = new Label("🌍 I18N Test - Kiểm tra Đa ngôn ngữ");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Separator
        Separator sep = new Separator();

        // Test Labels
        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px;");

        menuLabel = new Label();
        menuLabel.setStyle("-fx-font-size: 16px;");

        ordersLabel = new Label();
        ordersLabel.setStyle("-fx-font-size: 16px;");

        welcomeLabel = new Label();
        welcomeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
        welcomeLabel.setWrapText(true);
        welcomeLabel.setMaxWidth(400);

        // Current Locale Label
        Label localeLabel = new Label("Current Locale: " + currentLocale.toString());
        localeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #999;");

        // Switch Button
        switchButton = new Button();
        switchButton.setStyle(
            "-fx-background-color: #2563EB; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 12 24; " +
            "-fx-font-size: 14px; " +
            "-fx-cursor: hand;"
        );
        switchButton.setOnAction(e -> {
            // Toggle locale
            if (currentLocale.getLanguage().equals("vi")) {
                currentLocale = new Locale("en", "US");
            } else {
                currentLocale = new Locale("vi", "VN");
            }

            // Reload bundle and update UI
            loadBundle();
            updateUI();
            localeLabel.setText("Current Locale: " + currentLocale.toString());

            System.out.println("Switched to: " + currentLocale.toString());
        });

        // Initial update
        updateUI();

        // Add to layout
        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.getChildren().addAll(
            new Label("📋 Test Values:"),
            titleLabel,
            menuLabel,
            ordersLabel,
            new Label(""),
            new Label("💬 Message:"),
            welcomeLabel
        );
        contentBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-border-radius: 8; -fx-background-radius: 8;");

        root.getChildren().addAll(
            header,
            localeLabel,
            sep,
            contentBox,
            switchButton
        );

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("I18N Test");
        primaryStage.show();
    }

    private void loadBundle() {
        try {
            bundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
            System.out.println("✅ Bundle loaded: " + bundle.getLocale());
        } catch (Exception e) {
            System.err.println("❌ Error loading bundle: " + e.getMessage());
            e.printStackTrace();

            // Show error dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot load resource bundle");
            alert.setContentText(
                "Không thể load file: i18n/messages_" + currentLocale.toString() + ".properties\n\n" +
                "Kiểm tra:\n" +
                "1. File có trong src/main/resources/i18n/\n" +
                "2. Maven đã compile: mvn compile\n" +
                "3. Encoding là UTF-8"
            );
            alert.showAndWait();
        }
    }

    private void updateUI() {
        if (bundle == null) {
            titleLabel.setText("❌ Bundle not loaded");
            return;
        }

        try {
            titleLabel.setText("app.title = " + bundle.getString("app.title"));
            menuLabel.setText("menu.dashboard = " + bundle.getString("menu.dashboard"));
            ordersLabel.setText("menu.orders = " + bundle.getString("menu.orders"));
            welcomeLabel.setText("dashboard.welcome = " + bundle.getString("dashboard.welcome"));

            if (currentLocale.getLanguage().equals("vi")) {
                switchButton.setText("🔄 Chuyển sang English");
            } else {
                switchButton.setText("🔄 Switch to Tiếng Việt");
            }

            System.out.println("UI updated with locale: " + bundle.getLocale());
        } catch (Exception e) {
            System.err.println("❌ Error updating UI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("SIMPLE I18N TEST - Kiểm tra Đa ngôn ngữ");
        System.out.println("===========================================");
        System.out.println("Nếu test này hoạt động, thì i18n đã OK!");
        System.out.println("Nếu lỗi, xem console để debug.");
        System.out.println("===========================================\n");

        launch(args);
    }
}
