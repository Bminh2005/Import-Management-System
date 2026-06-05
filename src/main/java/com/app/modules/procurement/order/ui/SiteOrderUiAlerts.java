package com.app.modules.procurement.order.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class SiteOrderUiAlerts {

    private SiteOrderUiAlerts() {
    }

    public static void warn(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void info(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
