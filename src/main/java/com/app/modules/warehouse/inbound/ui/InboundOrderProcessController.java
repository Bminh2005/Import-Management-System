package com.app.modules.warehouse.inbound.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.ui.components.WarehouseSidebarController;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class InboundOrderProcessController {
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
    private VBox pageRoot;

    @FXML
    private WarehouseSidebarController sidebarController;

    private InboundOrderResponse inboundOrder;

    @FXML
    private void initialize() {
        sidebarController.setActiveMenu("inbound");
        setInboundOrder(inboundOrderService.getFirstProcessableOrder());
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
        expectedQuantityField.setText(String.valueOf(inboundOrder.getExpectedQuantity()));
        actualQuantityField.setText(String.valueOf(inboundOrder.getActualQuantity()));
        noteArea.setText(inboundOrder.getNote());
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
}
