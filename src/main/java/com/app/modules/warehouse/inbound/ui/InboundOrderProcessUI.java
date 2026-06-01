package com.app.modules.warehouse.inbound.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.modules.warehouse.dashboard.ui.WarehouseSidebar;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class InboundOrderProcessUI extends BorderPane {
    private static final long TEMP_INSPECTED_BY_USER_ID = 5;

    private final InboundOrderService inboundOrderService = new InboundOrderService();

    @FXML
    private Label orderCodeLabel;

    @FXML
    private Label requestCodeLabel;

    @FXML
    private Label supplierLabel;

    @FXML
    private Label receivedDateLabel;

    @FXML
    private Label expectedDateLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField expectedQuantityField;

    @FXML
    private TextField actualQuantityField;

    @FXML
    private TextArea noteArea;

    @FXML
    private VBox itemsContainer;

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
        if (!hasValidInboundOrder()) {
            return;
        }
        try {
            inboundOrderService.saveDraft(inboundOrder.getOrderId(), inboundOrderItems);
            setInboundOrder(inboundOrderService.getOrderById(inboundOrder.getOrderId()));
            showInfo("Đã lưu tạm", "Kết quả kiểm đếm đã được lưu với trạng thái Đang xử lý.");
        } catch (RuntimeException exception) {
            showError("Không thể lưu tạm", exception.getMessage());
        }
    }

    @FXML
    private void onConfirmInboundClick() {
        System.out.println("Noi dung chuc nang: Xac nhan nhap kho");
        if (!hasValidInboundOrder()) {
            return;
        }
        if ("IMPORTED".equals(inboundOrder.getStatusCode()) || "MISMATCH".equals(inboundOrder.getStatusCode())) {
            showError("Không thể xác nhận", "Đơn này đã được xác nhận, không thể cộng kho lần nữa.");
            return;
        }
        if (hasMissingMismatchReason()) {
            showError("Thiếu lý do sai lệch", "Nhập lý do cho từng mặt hàng sai lệch trước khi xác nhận.");
            return;
        }

        if (hasMismatch() && !confirmMismatch()) {
            return;
        }

        try {
            inboundOrderService.confirmInboundOrder(inboundOrder.getOrderId(), inboundOrderItems,
                    getProcessNote(), TEMP_INSPECTED_BY_USER_ID);
            setInboundOrder(inboundOrderService.getOrderById(inboundOrder.getOrderId()));
            showInfo("Xác nhận thành công",
                    "Đơn nhập kho đã cập nhật trạng thái và cộng số lượng thực nhận vào tồn kho.");
            System.out.println("Noi dung chuc nang: Xac nhan nhap kho thanh cong");
        } catch (RuntimeException exception) {
            System.out.println("Noi dung chuc nang: Xac nhan nhap kho that bai - " + exception.getMessage());
            showError("Xác nhận thất bại", exception.getMessage());
        }
    }

    @FXML
    private void onReportMismatchClick() {
        System.out.println("Noi dung chuc nang: Tu choi don nhap kho");
        showInfo("Từ chối đơn nhập kho", "Chức năng từ chối đang ở mức demo: hệ thống đã ghi nhận thao tác trên console.");
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
        requestCodeLabel.setText("Mã yêu cầu: " + inboundOrder.getRequestCode());
        supplierLabel.setText(inboundOrder.getSupplier());
        receivedDateLabel.setText(inboundOrder.getReceivedDate());
        expectedDateLabel.setText(inboundOrder.getExpectedDate());
        statusLabel.setText(inboundOrder.getStatus());
        statusLabel.getStyleClass().setAll("status-chip", statusClass(inboundOrder.getStatus()));
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
        String note = noteArea.getText() == null ? "" : noteArea.getText().trim();
        String itemReasons = inboundOrderItems.stream()
                .filter(InboundOrderItemResponse::hasMismatch)
                .map(item -> item.getProductCode() + ": " + item.getDiscrepancyReason())
                .reduce((left, right) -> left + "; " + right)
                .orElse("");
        if (note.isBlank()) {
            return itemReasons;
        }
        if (itemReasons.isBlank()) {
            return note;
        }
        return note + " | " + itemReasons;
    }

    public void setProcessNote(String processNote) {
        noteArea.setText(processNote);
    }

    public ObservableList<InboundOrderItemResponse> getInboundOrderItems() {
        return inboundOrderItems;
    }

    public void setInboundOrderItems(ObservableList<InboundOrderItemResponse> inboundOrderItems) {
        this.inboundOrderItems = inboundOrderItems;
        renderItemCards();
        refreshTotals();
    }

    private void renderItemCards() {
        itemsContainer.getChildren().clear();
        if (inboundOrderItems.isEmpty()) {
            Label emptyLabel = new Label("Không có mặt hàng trong đơn nhập kho này.");
            emptyLabel.getStyleClass().add("field-label");
            itemsContainer.getChildren().add(emptyLabel);
            return;
        }
        for (InboundOrderItemResponse item : inboundOrderItems) {
            itemsContainer.getChildren().add(buildItemCard(item));
        }
    }

    private VBox buildItemCard(InboundOrderItemResponse item) {
        VBox card = new VBox(14);
        card.getStyleClass().add("item-card");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(4);
        Label nameLabel = new Label(item.getProductName());
        nameLabel.getStyleClass().add("item-name");
        Label codeLabel = new Label("Mã: " + item.getProductCode());
        codeLabel.getStyleClass().add("item-code");
        titleBox.getChildren().addAll(nameLabel, codeLabel);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label statusChip = new Label();
        header.getChildren().addAll(titleBox, spacer, statusChip);

        GridPane quantityGrid = new GridPane();
        quantityGrid.setHgap(16);
        quantityGrid.getColumnConstraints().addAll(percentColumn(), percentColumn(), percentColumn());

        VBox orderedBox = new VBox(6);
        Label orderedLabel = new Label("Số lượng đặt");
        orderedLabel.getStyleClass().add("mini-label");
        Label orderedValue = new Label(String.valueOf(item.getOrderedQuantity()));
        orderedValue.getStyleClass().add("readonly-quantity");
        orderedBox.getChildren().addAll(orderedLabel, orderedValue);

        VBox actualBox = new VBox(6);
        Label actualLabel = new Label("Số lượng thực tế *");
        actualLabel.getStyleClass().add("mini-label");
        TextField actualField = new TextField(String.valueOf(item.getActualQuantity()));
        actualField.getStyleClass().add("quantity-input");
        actualBox.getChildren().addAll(actualLabel, actualField);

        VBox differenceBox = new VBox(6);
        Label differenceLabel = new Label("Chênh lệch");
        differenceLabel.getStyleClass().add("mini-label");
        Label differenceValue = new Label();
        differenceValue.getStyleClass().add("difference-box");
        differenceBox.getChildren().addAll(differenceLabel, differenceValue);

        quantityGrid.add(orderedBox, 0, 0);
        quantityGrid.add(actualBox, 1, 0);
        quantityGrid.add(differenceBox, 2, 0);

        VBox reasonBox = new VBox(6);
        Label reasonLabel = new Label("Lý do sai lệch *");
        reasonLabel.getStyleClass().add("reason-title");
        TextArea reasonArea = new TextArea(item.getDiscrepancyReason());
        reasonArea.setWrapText(true);
        reasonArea.setPrefRowCount(2);
        reasonArea.setPromptText("Nhập lý do sai lệch, ví dụ: thiếu hàng từ nhà cung cấp, hàng hỏng...");
        reasonArea.getStyleClass().add("reason-area");
        reasonBox.getChildren().addAll(reasonLabel, reasonArea);

        actualField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBlank()) {
                item.setActualQuantity(0);
                updateItemVisualState(item, card, statusChip, differenceValue, reasonBox);
                refreshTotals();
                return;
            }
            try {
                int parsed = Integer.parseInt(newValue);
                if (parsed < 0) {
                    throw new NumberFormatException("negative");
                }
                item.setActualQuantity(parsed);
                if (!item.hasMismatch()) {
                    item.setDiscrepancyReason("");
                    reasonArea.setText("");
                }
                updateItemVisualState(item, card, statusChip, differenceValue, reasonBox);
                refreshTotals();
            } catch (NumberFormatException exception) {
                actualField.setText(oldValue);
            }
        });
        reasonArea.textProperty().addListener((observable, oldValue, newValue) ->
                item.setDiscrepancyReason(newValue == null ? "" : newValue.trim()));

        card.getChildren().addAll(header, quantityGrid, reasonBox);
        VBox.setMargin(reasonBox, new Insets(2, 0, 0, 0));
        updateItemVisualState(item, card, statusChip, differenceValue, reasonBox);
        return card;
    }

    private ColumnConstraints percentColumn() {
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(33.333);
        column.setHgrow(Priority.ALWAYS);
        return column;
    }

    private void updateItemVisualState(InboundOrderItemResponse item, VBox card, Label statusChip,
                                       Label differenceValue, VBox reasonBox) {
        int difference = item.getActualQuantity() - item.getOrderedQuantity();
        boolean hasMismatch = difference != 0;
        card.getStyleClass().setAll(hasMismatch ? new String[]{"item-card", "item-card-mismatch"}
                : new String[]{"item-card"});
        statusChip.getStyleClass().setAll("status-chip", hasMismatch ? "status-mismatch" : "status-ok");
        statusChip.setText(hasMismatch ? "Có sai lệch" : "Khớp");
        differenceValue.getStyleClass().setAll("difference-box", differenceClass(difference));
        if (difference == 0) {
            differenceValue.setText("Khớp");
        } else if (difference > 0) {
            differenceValue.setText("+" + difference);
        } else {
            differenceValue.setText(String.valueOf(difference));
        }
        reasonBox.setVisible(hasMismatch);
        reasonBox.setManaged(hasMismatch);
    }

    private String differenceClass(int difference) {
        if (difference == 0) {
            return "difference-ok";
        }
        if (difference > 0) {
            return "difference-extra";
        }
        return "difference-missing";
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

    private boolean hasValidInboundOrder() {
        if (inboundOrder == null || inboundOrder.getOrderId() <= 0) {
            showError("Không có đơn", "Không có đơn nhập kho hợp lệ để xử lý.");
            return false;
        }
        return true;
    }

    private boolean hasMismatch() {
        return inboundOrderItems.stream().anyMatch(InboundOrderItemResponse::hasMismatch);
    }

    private boolean hasMissingMismatchReason() {
        return inboundOrderItems.stream()
                .anyMatch(item -> item.hasMismatch()
                        && (item.getDiscrepancyReason() == null || item.getDiscrepancyReason().isBlank()));
    }

    private boolean confirmMismatch() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Phát hiện sai lệch số lượng");
        alert.setHeaderText("Đơn nhập kho có mặt hàng sai lệch.");
        alert.setContentText("Hệ thống sẽ ghi nhận sai lệch, cộng số lượng thực nhận vào kho và đánh dấu đơn Có sai lệch.");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private String statusClass(String status) {
        if (status != null && status.contains("Đang")) {
            return "status-processing";
        }
        if (status != null && status.contains("Đã")) {
            return "status-imported";
        }
        if (status != null && (status.contains("Sai") || status.contains("sai"))) {
            return "status-mismatch";
        }
        return "status-pending";
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
