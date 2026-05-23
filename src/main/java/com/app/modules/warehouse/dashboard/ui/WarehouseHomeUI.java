package com.app.modules.warehouse.dashboard.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.ui.components.WarehouseSidebarUI;
import com.app.modules.warehouse.dashboard.dto.WarehouseDashboardSummary;
import com.app.modules.warehouse.dashboard.service.WarehouseDashboardService;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private WarehouseSidebarUI sidebar;

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
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        actionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        recentOrderTable.setSelectionModel(null);
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
