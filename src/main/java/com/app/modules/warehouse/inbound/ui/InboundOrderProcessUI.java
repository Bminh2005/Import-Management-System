package com.app.modules.warehouse.inbound.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.modules.warehouse.dashboard.ui.WarehouseSidebar;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

public class InboundOrderProcessUI extends BorderPane {
    private final InboundOrderService inboundOrderService = new InboundOrderService();

    @FXML
    private Label orderCodeLabel;

    @FXML
    private Label supplierLabel;

    @FXML
    private Label receivedDateLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField expectedQuantityField;

    @FXML
    private TextField actualQuantityField;

    @FXML
    private TextArea noteArea;

    @FXML
    private TableView<InboundOrderItemResponse> itemTable;

    @FXML
    private TableColumn<InboundOrderItemResponse, String> itemCodeColumn;

    @FXML
    private TableColumn<InboundOrderItemResponse, String> itemNameColumn;

    @FXML
    private TableColumn<InboundOrderItemResponse, Integer> orderedQuantityColumn;

    @FXML
    private TableColumn<InboundOrderItemResponse, Integer> itemActualQuantityColumn;

    @FXML
    private VBox pageRoot;

    @FXML
    private WarehouseSidebar sidebar;

    private InboundOrderResponse inboundOrder;
    private ObservableList<InboundOrderItemResponse> inboundOrderItems = FXCollections.observableArrayList();
    private final long initialOrderId;

    public InboundOrderProcessUI() {
        this(0);
    }

    public InboundOrderProcessUI(long orderId) {
        this.initialOrderId = orderId;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InboundOrderProcessPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new IllegalStateException("Khong the tai InboundOrderProcessPage.fxml", exception);
        }
    }

    @FXML
    private void initialize() {
        sidebar.setActiveMenu("inbound");
        configureItemTable();
        expectedQuantityField.setEditable(false);
        actualQuantityField.setEditable(false);
        if (initialOrderId > 0) {
            setInboundOrder(inboundOrderService.getOrderById(initialOrderId));
        } else {
            setInboundOrder(inboundOrderService.getFirstProcessableOrder());
        }
    }

    @FXML
    private void onBackClick() {
        System.out.println("Noi dung chuc nang: Quay lai danh sach don nhap kho");
        WarehouseNavigation.showInboundOrderList(pageRoot);
    }

    @FXML
    private void onScanBarcodeClick() {
        System.out.println("Noi dung chuc nang: Quet ma vach hang nhap kho");
    }

    @FXML
    private void onSaveDraftClick() {
        System.out.println("Noi dung chuc nang: Luu tam ket qua xu ly don nhap kho");
    }

    @FXML
    private void onConfirmInboundClick() {
        System.out.println("Noi dung chuc nang: Xac nhan nhap kho");
        if (inboundOrder == null || inboundOrder.getOrderId() <= 0) {
            System.out.println("Noi dung chuc nang: Khong co don nhap kho hop le de xac nhan");
            return;
        }
        try {
            inboundOrderService.confirmInboundOrder(inboundOrder.getOrderId(), inboundOrderItems,
                    getProcessNote(), 5);
            setInboundOrder(inboundOrderService.getOrderById(inboundOrder.getOrderId()));
            System.out.println("Noi dung chuc nang: Xac nhan nhap kho thanh cong");
        } catch (RuntimeException exception) {
            System.out.println("Noi dung chuc nang: Xac nhan nhap kho that bai - " + exception.getMessage());
            statusLabel.setText("Can bo sung thong tin");
        }
    }

    @FXML
    private void onReportMismatchClick() {
        System.out.println("Noi dung chuc nang: Bao cao sai lech don nhap kho");
    }

    public InboundOrderResponse getInboundOrder() {
        return inboundOrder;
    }

    public void setInboundOrder(InboundOrderResponse inboundOrder) {
        this.inboundOrder = inboundOrder;
        if (inboundOrder == null) {
            return;
        }
        orderCodeLabel.setText(inboundOrder.getOrderCode());
        supplierLabel.setText(inboundOrder.getSupplier());
        receivedDateLabel.setText(inboundOrder.getReceivedDate());
        statusLabel.setText(inboundOrder.getStatus());
        noteArea.setText(inboundOrder.getNote());
        setInboundOrderItems(FXCollections.observableArrayList(
                inboundOrderService.getOrderItems(inboundOrder.getOrderId())
        ));
    }

    public String getActualQuantity() {
        return actualQuantityField.getText();
    }

    public void setActualQuantity(String actualQuantity) {
        actualQuantityField.setText(actualQuantity);
    }

    public String getProcessNote() {
        return noteArea.getText();
    }

    public void setProcessNote(String processNote) {
        noteArea.setText(processNote);
    }

    public ObservableList<InboundOrderItemResponse> getInboundOrderItems() {
        return inboundOrderItems;
    }

    public void setInboundOrderItems(ObservableList<InboundOrderItemResponse> inboundOrderItems) {
        this.inboundOrderItems = inboundOrderItems;
        itemTable.setItems(inboundOrderItems);
        refreshTotals();
    }

    private void configureItemTable() {
        itemTable.setEditable(true);
        itemCodeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductCode()));
        itemNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProductName()));
        orderedQuantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getOrderedQuantity()).asObject());
        itemActualQuantityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getActualQuantity()).asObject());
        itemActualQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        itemActualQuantityColumn.setOnEditCommit(event -> {
            event.getRowValue().setActualQuantity(event.getNewValue());
            refreshTotals();
        });
    }

    private void refreshTotals() {
        int expectedTotal = inboundOrderItems.stream()
                .mapToInt(InboundOrderItemResponse::getOrderedQuantity)
                .sum();
        int actualTotal = inboundOrderItems.stream()
                .mapToInt(InboundOrderItemResponse::getActualQuantity)
                .sum();
        expectedQuantityField.setText(String.valueOf(expectedTotal));
        actualQuantityField.setText(String.valueOf(actualTotal));
    }
}
