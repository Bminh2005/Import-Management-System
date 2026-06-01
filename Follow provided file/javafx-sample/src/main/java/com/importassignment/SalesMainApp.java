package com.importassignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main Application for Sales Department
 * Bộ phận Bán hàng
 */
public class SalesMainApp extends Application {

    private static Stage primaryStage;
    private static ResourceBundle resourceBundle;
    private static Locale currentLocale = new Locale("vi", "VN"); // Default: Vietnamese

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Load resource bundle
        resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);

        // Load main layout
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/SalesMainLayout.fxml")
        );
        loader.setResources(resourceBundle);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(
            getClass().getResource("/css/sales-application.css").toExternalForm()
        );

        primaryStage.setTitle(resourceBundle.getString("app.title"));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void changeLanguage(Locale locale) {
        currentLocale = locale;
        resourceBundle = ResourceBundle.getBundle("i18n.messages", currentLocale);

        try {
            FXMLLoader loader = new FXMLLoader(
                SalesMainApp.class.getResource("/fxml/SalesMainLayout.fxml")
            );
            loader.setResources(resourceBundle);
            Parent root = loader.load();

            Scene scene = primaryStage.getScene();
            scene.setRoot(root);

            // Reapply stylesheet
            scene.getStylesheets().clear();
            scene.getStylesheets().add(
                SalesMainApp.class.getResource("/css/sales-application.css").toExternalForm()
            );

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

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("SALES DEPARTMENT APPLICATION");
        System.out.println("Ứng dụng Bộ phận Bán hàng");
        System.out.println("========================================");
        launch(args);
    }
}
