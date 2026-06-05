package com.app.modules.warehouse.common.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class WarehouseMetricCardUI extends VBox {
    private final Label titleLabel = new Label();
    private final Label valueLabel = new Label("0");
    private final Label iconLabel = new Label();

    public WarehouseMetricCardUI() {
        getStyleClass().addAll("card", "metric-card");

        titleLabel.getStyleClass().add("metric-title");
        valueLabel.getStyleClass().add("metric-value");
        iconLabel.getStyleClass().add("metric-icon");

        VBox textBox = new VBox(8, titleLabel, valueLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox content = new HBox(textBox, spacer, iconLabel);
        content.setAlignment(Pos.TOP_LEFT);
        getChildren().add(content);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setValue(String value) {
        valueLabel.setText(value == null ? "0" : value);
    }

    public void setIcon(String svgContent, String fillColor, String toneClass) {
        iconLabel.getStyleClass().removeAll("orange", "blue", "green", "red");
        if (toneClass != null && !toneClass.isBlank()) {
            iconLabel.getStyleClass().add(toneClass);
        }
        SVGPath icon = new SVGPath();
        icon.setContent(svgContent);
        icon.setStyle("-fx-fill: " + fillColor + ";");
        icon.setScaleX(0.9);
        icon.setScaleY(0.9);
        iconLabel.setGraphic(icon);
    }
}
