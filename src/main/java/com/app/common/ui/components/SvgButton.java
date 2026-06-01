package com.app.common.ui.components;

import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

public class SvgButton extends Button {
    public SvgButton(String svgContent, String fillColor) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgContent);
        icon.setStyle("-fx-fill: " + fillColor + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        setGraphic(icon);
    }
}
