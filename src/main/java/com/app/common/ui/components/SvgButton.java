package com.app.common.ui.components;

import javafx.scene.control.Button;
import javafx.scene.shape.SVGPath;

/**
 * Một Button chứa Icon vẽ từ chuỗi SVG, dễ dàng tái sử dụng trong hệ thống.
 * Hỗ trợ cấu hình màu fill của icon.
 */
public class SvgButton extends Button {

    public SvgButton(String svgContent, String fillColor) {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent(svgContent);
        icon.setStyle("-fx-fill: " + fillColor + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        setGraphic(icon);
    }
}
