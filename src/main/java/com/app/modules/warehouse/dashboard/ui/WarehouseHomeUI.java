package com.app.modules.warehouse.dashboard.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.modules.warehouse.dashboard.dto.WarehouseDashboardSummary;
import com.app.modules.warehouse.dashboard.service.WarehouseDashboardService;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class WarehouseHomeUI extends BorderPane {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WarehouseHomePage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Khong the tai WarehouseHomePage.fxml", exception);
        }
    }

    @FXML
    private void initialize() {
        sidebar.setActiveMenu("home");
        configureTable();
        setDashboardSummary(dashboardService.getDashboardSummary());
        setRecentOrders(FXCollections.observableArrayList(inboundOrderService.getRecentInboundOrders()));
    }

    @FXML
    private void onProcessInboundClick() {
        System.out.println("Noi dung chuc nang: Xu ly don nhap kho");
        WarehouseNavigation.showInboundOrderProcess(recentOrderTable);
    }

    @FXML
    private void onMismatchClick() {
        System.out.println("Noi dung chuc nang: Xem don co sai lech");
    }

    @FXML
    private void onViewAllClick() {
        System.out.println("Noi dung chuc nang: Xem tat ca don nhap kho");
        WarehouseNavigation.showInboundOrderList(recentOrderTable);
    }

    private void configureTable() {
        orderCodeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderCode()));
        receivedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReceivedDate()));
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty("Xu ly"));
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
            chip.setText(item);
            chip.getStyleClass().setAll("status-chip", statusClass(item));
            setGraphic(chip);
            setText(null);
        }

        private String statusClass(String item) {
            if (item.contains("Đang")) {
                return "status-processing";
            }
            if (item.contains("Đã")) {
                return "status-imported";
            }
            if (item.contains("Sai") || item.contains("sai")) {
                return "status-mismatch";
            }
            return "status-pending";
        }
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
