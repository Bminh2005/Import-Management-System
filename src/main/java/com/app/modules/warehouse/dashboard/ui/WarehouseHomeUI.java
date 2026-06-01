package com.app.modules.warehouse.dashboard.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.warehouse.dashboard.dto.WarehouseDashboardSummary;
import com.app.modules.warehouse.dashboard.service.WarehouseDashboardService;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.ui.WarehouseInboundStatus;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.SVGPath;

public class WarehouseHomeUI extends BorderPane {
    private static final String CLOCK_ICON = "M12 20c4.41 0 8-3.59 8-8s-3.59-8-8-8-8 3.59-8 8 3.59 8 8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67V7z";
    private static final String PACKAGE_ICON = "M21 8.5 12 3 3 8.5l9 5.2 9-5.2zM5 10.2v5.4l6 3.5v-5.4l-6-3.5zm14 0-6 3.5v5.4l6-3.5v-5.4z";
    private static final String TREND_ICON = "M16 6l2.29 2.29-4.88 4.88-4-4L2 16.59 3.41 18l6-6 4 4 6.3-6.29L22 12V6z";
    private static final String ALERT_ICON = "M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z";

    private final WarehouseDashboardService dashboardService = new WarehouseDashboardService();
    private final InboundOrderService inboundOrderService = new InboundOrderService();

    @FXML
    private Label pendingInboundLabel;

    @FXML
    private Label processingLabel;

    @FXML
    private Label importedThisMonthLabel;

    @FXML
    private Label mismatchLabel;

    @FXML
    private Label pendingMetricIcon;

    @FXML
    private Label processingMetricIcon;

    @FXML
    private Label importedMetricIcon;

    @FXML
    private Label mismatchMetricIcon;

    @FXML
    private Label quickProcessIcon;

    @FXML
    private Label quickMismatchIcon;

    @FXML
    private TableView<InboundOrderResponse> recentOrderTable;

    @FXML
    private TableColumn<InboundOrderResponse, String> orderCodeColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> receivedDateColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> supplierColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> statusColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> actionColumn;

    @FXML
    private WarehouseSidebar sidebar;

    private WarehouseDashboardSummary dashboardSummary;
    private ObservableList<InboundOrderResponse> recentOrders = FXCollections.observableArrayList();

    public WarehouseHomeUI() {
        FxmlUiHelper.loadSelf(this, "WarehouseHomePage.fxml");
    }

    @FXML
    private void initialize() {
        sidebar.setActiveMenu("home");
        installIcons();
        configureTable();
        setDashboardSummary(dashboardService.getDashboardSummary());
        setRecentOrders(FXCollections.observableArrayList(inboundOrderService.getRecentInboundOrders()));
    }

    @FXML
    private void onProcessInboundClick() {
        System.out.println("Nội dung chức năng: Xử lý đơn nhập kho");
        WarehouseNavigation.showInboundOrderProcess(recentOrderTable);
    }

    @FXML
    private void onMismatchClick() {
        System.out.println("Nội dung chức năng: Xem đơn có sai lệch");
        WarehouseNavigation.showInboundOrderList(recentOrderTable, "MISMATCH");
    }

    @FXML
    private void onViewAllClick() {
        System.out.println("Nội dung chức năng: Xem tất cả đơn nhập kho");
        WarehouseNavigation.showInboundOrderList(recentOrderTable);
    }

    private void configureTable() {
        orderCodeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderCode()));
        receivedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReceivedDate()));
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusCode()));
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty("Xử lý"));
        statusColumn.setCellFactory(column -> new StatusChipCell<>());
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button actionButton = new Button("Xử lý");

            {
                actionButton.getStyleClass().add("table-action");
                actionButton.setOnAction(event -> {
                    InboundOrderResponse order = getTableView().getItems().get(getIndex());
                    WarehouseNavigation.showInboundOrderProcess(actionButton, order.getOrderId());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionButton);
                setText(null);
            }
        });
        recentOrderTable.setSelectionModel(null);
        recentOrderTable.setPlaceholder(new Label("Chưa có đơn nhập kho"));
    }

    private static class StatusChipCell<S> extends TableCell<S, String> {
        private final Label chip = new Label();

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                return;
            }
            chip.setText(WarehouseInboundStatus.label(item));
            chip.getStyleClass().setAll("status-chip", WarehouseInboundStatus.cssClass(item));
            setGraphic(chip);
            setText(null);
        }
    }

    private void installIcons() {
        setIcon(pendingMetricIcon, CLOCK_ICON, "#ea580c");
        setIcon(processingMetricIcon, PACKAGE_ICON, "#2563eb");
        setIcon(importedMetricIcon, TREND_ICON, "#2f9b6e");
        setIcon(mismatchMetricIcon, ALERT_ICON, "#dc2626");
        setIcon(quickProcessIcon, PACKAGE_ICON, "white");
        setIcon(quickMismatchIcon, ALERT_ICON, "white");
    }

    private void setIcon(Label target, String svgContent, String fillColor) {
        if (target == null) {
            return;
        }
        SVGPath icon = new SVGPath();
        icon.setContent(svgContent);
        icon.setStyle("-fx-fill: " + fillColor + ";");
        icon.setScaleX(0.9);
        icon.setScaleY(0.9);
        target.setGraphic(icon);
    }

    public WarehouseDashboardSummary getDashboardSummary() {
        return dashboardSummary;
    }

    public void setDashboardSummary(WarehouseDashboardSummary dashboardSummary) {
        this.dashboardSummary = dashboardSummary;
        if (dashboardSummary == null) {
            return;
        }
        pendingInboundLabel.setText(String.valueOf(dashboardSummary.getPendingInboundCount()));
        processingLabel.setText(String.valueOf(dashboardSummary.getProcessingCount()));
        importedThisMonthLabel.setText(String.valueOf(dashboardSummary.getImportedThisMonthCount()));
        mismatchLabel.setText(String.valueOf(dashboardSummary.getMismatchCount()));
    }

    public ObservableList<InboundOrderResponse> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(ObservableList<InboundOrderResponse> recentOrders) {
        this.recentOrders = recentOrders;
        recentOrderTable.setItems(recentOrders);
    }
}
