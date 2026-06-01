package com.importassignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Import Assignment System - Main Application
 * Hệ thống Quản lý Đặt hàng Nhập khẩu
 */
public class MainApp extends Application {

    private static Stage primaryStage;
    private static ResourceBundle resourceBundle;
    private static Locale currentLocale = new Locale("vi", "VN"); // Default: Vietnamese

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load resource bundle
        resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);

        // Load main layout
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
        loader.setResources(resourceBundle);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        primaryStage.setTitle(resourceBundle.getString("app.title"));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void changeLanguage(Locale locale) {
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);
        // Reload current scene to apply new language
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/MainLayout.fxml"));
            loader.setResources(resourceBundle);
            Parent root = loader.load();

            // Update scene
            Scene scene = primaryStage.getScene();
            scene.setRoot(root);

            // Reapply stylesheet
            scene.getStylesheets().clear();
            scene.getStylesheets().add(MainApp.class.getResource("/css/application.css").toExternalForm());

            // Update title
            primaryStage.setTitle(resourceBundle.getString("app.title"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi khi chuyển đổi ngôn ngữ: " + e.getMessage());
        }
    }

    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
