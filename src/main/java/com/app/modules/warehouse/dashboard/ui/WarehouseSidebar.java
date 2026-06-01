package com.app.modules.warehouse.dashboard.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.util.FxmlUiHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class WarehouseSidebar extends VBox {
    private static final String GLOBE_ICON = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM18.93 8h-2.95c-.32-1.25-.82-2.41-1.47-3.37A8.05 8.05 0 0 1 18.93 8zM12 4.04c.83 1.2 1.48 2.53 1.91 3.96h-3.82C10.52 6.57 11.17 5.24 12 4.04zM4.26 14A8.22 8.22 0 0 1 4 12c0-.69.09-1.36.26-2h3.38A16.6 16.6 0 0 0 7.5 12c0 .68.05 1.35.14 2H4.26zm.81 2h2.95c.32 1.25.82 2.41 1.47 3.37A8.05 8.05 0 0 1 5.07 16zM8.02 8H5.07a8.05 8.05 0 0 1 4.42-3.37A13.85 13.85 0 0 0 8.02 8zM12 19.96c-.83-1.2-1.48-2.53-1.91-3.96h3.82c-.43 1.43-1.08 2.76-1.91 3.96zM14.34 14H9.66A14.71 14.71 0 0 1 9.5 12c0-.68.06-1.35.16-2h4.68c.1.65.16 1.32.16 2s-.06 1.35-.16 2zm.17 5.37A13.85 13.85 0 0 0 15.98 16h2.95a8.05 8.05 0 0 1-4.42 3.37zM16.36 14c.09-.65.14-1.32.14-2s-.05-1.35-.14-2h3.38c.17.64.26 1.31.26 2s-.09 1.36-.26 2h-3.38z";
    private static final String HOME_ICON = "M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z";
    private static final String PACKAGE_ICON = "M21 8.5 12 3 3 8.5l9 5.2 9-5.2zM5 10.2v5.4l6 3.5v-5.4l-6-3.5zm14 0-6 3.5v5.4l6-3.5v-5.4z";
    private static final String CHEVRON_RIGHT_ICON = "M9 6l6 6-6 6";

    @FXML
    private Label brandIcon;

    @FXML
    private Button homeButton;

    @FXML
    private Button inboundButton;

    @FXML
    private Button collapseButton;

    public WarehouseSidebar() {
        FxmlUiHelper.loadSelf(this, "WarehouseSidebar.fxml");
    }

    @FXML
    private void initialize() {
        installIcons();
        setActiveMenu("home");
    }

    @FXML
    private void onHomeClick() {
        System.out.println("Chuyển trang: Trang chủ quản lý kho");
        setActiveMenu("home");
        WarehouseNavigation.showWarehouseHome(this);
    }

    @FXML
    private void onInboundClick() {
        System.out.println("Chuyển trang: Danh sách đơn nhập kho");
        setActiveMenu("inbound");
        WarehouseNavigation.showInboundOrderList(this);
    }

    @FXML
    private void onCollapseClick() {
        System.out.println("Nội dung chức năng: Thu gọn/mở rộng sidebar kho");
    }

    public void setActiveMenu(String activeMenu) {
        setButtonActive(homeButton, !"inbound".equals(activeMenu));
        setButtonActive(inboundButton, "inbound".equals(activeMenu));
    }

    private void setButtonActive(Button button, boolean active) {
        if (button == null) {
            return;
        }
        button.getStyleClass().remove("active");
        if (active) {
            button.getStyleClass().add("active");
        }
    }

    private void installIcons() {
        brandIcon.setGraphic(icon(GLOBE_ICON, "white", 0.95));
        homeButton.setGraphic(icon(HOME_ICON, "white", 0.9));
        inboundButton.setGraphic(icon(PACKAGE_ICON, "white", 0.9));
        collapseButton.setGraphic(icon(CHEVRON_RIGHT_ICON, "white", 0.9));
        homeButton.setTooltip(new Tooltip("Trang chủ kho"));
        inboundButton.setTooltip(new Tooltip("Đơn chờ nhập kho"));
        collapseButton.setTooltip(new Tooltip("Thu gọn/mở rộng"));
    }

    private SVGPath icon(String svgContent, String fillColor, double scale) {
        SVGPath icon = new SVGPath();
        icon.setContent(svgContent);
        icon.setStyle("-fx-fill: transparent; -fx-stroke: " + fillColor + "; -fx-stroke-width: 1.8;");
        icon.setScaleX(scale);
        icon.setScaleY(scale);
        return icon;
    }
}
