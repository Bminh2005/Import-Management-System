package com.app.ui.base;

import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Base class cho các màn UI JavaFX.
 *
 * Thay thế mô hình:
 * FXML file + FXMLLoader + Controller
 *
 * Bằng mô hình:
 * View class extends ParentNode
 */
public abstract class ParentNode extends StackPane {

    protected ParentNode() {
        buildView();
        bindEvents();
        loadData();
    }

    /**
     * Dựng layout và component UI.
     */
    protected abstract void buildView();

    /**
     * Gắn event cho button, card, input...
     */
    protected void bindEvents() {
    }

    /**
     * Nạp dữ liệu ban đầu.
     */
    protected void loadData() {
    }

    /**
     * Chuyển màn trong cùng Stage.
     */
    protected void navigateTo(ParentNode nextView, String title) {
        Scene scene = getScene();
        if (scene == null) {
            return;
        }

        scene.setRoot(nextView);

        if (scene.getWindow() instanceof Stage stage) {
            stage.setTitle(title);
        }
    }

    /**
     * Load CSS theo resource path.
     */
    protected void addStylesheet(String resourcePath) {
        String css = getClass().getResource(resourcePath).toExternalForm();
        getStylesheets().add(css);
    }
}