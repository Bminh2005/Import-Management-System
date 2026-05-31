package com.app.modules.warehouse.inbound.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.ui.components.WarehouseSidebarUI;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class InboundOrderListUI extends BorderPane {
    private final InboundOrderService inboundOrderService = new InboundOrderService();

    @FXML
    private TextField keywordField;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private TableView<InboundOrderResponse> inboundOrderTable;

    @FXML
    private TableColumn<InboundOrderResponse, String> orderCodeColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> receivedDateColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> supplierColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> statusColumn;

    @FXML
    private TableColumn<InboundOrderResponse, Integer> expectedQuantityColumn;

    @FXML
    private TableColumn<InboundOrderResponse, Integer> actualQuantityColumn;

    @FXML
    private TableColumn<InboundOrderResponse, String> actionColumn;

    @FXML
    private WarehouseSidebarUI sidebar;

    private ObservableList<InboundOrderResponse> inboundOrders = FXCollections.observableArrayList();

    public InboundOrderListUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InboundOrderListPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Khong the tai InboundOrderListPage.fxml", exception);
        }
    }

    @FXML
    private void initialize() {
        sidebar.setActiveMenu("inbound");
        statusFilter.setItems(FXCollections.observableArrayList(
                "Tat ca", "Cho xu ly", "Dang xu ly", "Da nhap kho", "Co sai lech"
        ));
        statusFilter.getSelectionModel().selectFirst();
        configureTable();
        setInboundOrders(FXCollections.observableArrayList(inboundOrderService.getAllInboundOrders()));
    }

    @FXML
    private void onSearchClick() {
        System.out.println("Noi dung chuc nang: Tim kiem don nhap kho - " + getKeyword());
    }

    @FXML
    private void onResetFilterClick() {
        System.out.println("Noi dung chuc nang: Dat lai bo loc don nhap kho");
        setKeyword("");
        statusFilter.getSelectionModel().selectFirst();
    }

    @FXML
    private void onCreateInboundClick() {
        System.out.println("Noi dung chuc nang: Tao don nhap kho thu cong");
    }

    @FXML
    private void onProcessSelectedClick() {
        System.out.println("Noi dung chuc nang: Xu ly don nhap kho duoc chon");
        InboundOrderResponse selectedOrder = inboundOrderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null && !inboundOrderTable.getItems().isEmpty()) {
            selectedOrder = inboundOrderTable.getItems().get(0);
        }
        if (selectedOrder == null) {
            System.out.println("Noi dung chuc nang: Chua co don nhap kho de xu ly");
            return;
        }
        WarehouseNavigation.showInboundOrderProcess(inboundOrderTable, selectedOrder.getOrderId());
    }

    private void configureTable() {
        orderCodeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrderCode()));
        receivedDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReceivedDate()));
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSupplier()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        expectedQuantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getExpectedQuantity()).asObject());
        actualQuantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getActualQuantity()).asObject());
        actionColumn.setCellValueFactory(data -> new SimpleStringProperty("Xu ly"));
        statusColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        actionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public String getKeyword() {
        return keywordField.getText();
    }

    public void setKeyword(String keyword) {
        keywordField.setText(keyword);
    }

    public String getSelectedStatus() {
        return statusFilter.getValue();
    }

    public void setSelectedStatus(String selectedStatus) {
        statusFilter.getSelectionModel().select(selectedStatus);
    }

    public ObservableList<InboundOrderResponse> getInboundOrders() {
        return inboundOrders;
    }

    public void setInboundOrders(ObservableList<InboundOrderResponse> inboundOrders) {
        this.inboundOrders = inboundOrders;
        inboundOrderTable.setItems(inboundOrders);
    }
}
