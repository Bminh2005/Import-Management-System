package com.app.modules.warehouse.inbound.ui;

import com.app.Ioms.navigation.WarehouseNavigation;
import com.app.common.util.FxmlUiHelper;
import com.app.modules.warehouse.dashboard.ui.WarehouseSidebar;
import com.app.modules.warehouse.common.ui.WarehouseStatusBadge;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import com.app.modules.warehouse.inbound.dto.InboundOrderResponse;
import com.app.modules.warehouse.inbound.integration.WarehouseOrderSyncResult;
import com.app.modules.warehouse.inbound.service.InboundOrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class InboundOrderProcessUI extends BorderPane {
    private static final long TEMP_INSPECTED_BY_USER_ID = 5;
    private static final String PACKAGE_CHECK_ICON =
            "M16.5 9.4 7.55 4.24 3 6.87l8.95 5.16 4.55-2.63Zm-9.2 5.82 3.65 2.1v-4.45L2 7.7v6.9l5.3 3.06v-2.44Zm6.65 2.1 8.05-4.65V7.7l-8.05 4.65v4.97ZM12 2l10 5.77v8.46L12 22 2 16.23V7.77L12 2Z";

    private final InboundOrderService inboundOrderService = new InboundOrderService();
    private final long initialOrderId;
    private InboundOrderProcessTableController tableController;

    @FXML private Label orderIconLabel;
    @FXML private Label orderCodeLabel;
    @FXML private Label requestCodeLabel;
    @FXML private Label supplierLabel;
    @FXML private Label receivedDateLabel;
    @FXML private Label expectedDateLabel;
    @FXML private Label statusLabel;
    @FXML private TextField expectedQuantityField;
    @FXML private TextField actualQuantityField;
    @FXML private TextArea noteArea;
    @FXML private TableView<InboundOrderItemResponse> itemTable;
    @FXML private TableColumn<InboundOrderItemResponse, String> itemCodeColumn;
    @FXML private TableColumn<InboundOrderItemResponse, String> itemNameColumn;
    @FXML private TableColumn<InboundOrderItemResponse, String> unitColumn;
    @FXML private TableColumn<InboundOrderItemResponse, Integer> orderedQuantityColumn;
    @FXML private TableColumn<InboundOrderItemResponse, InboundOrderItemResponse> actualQuantityColumn;
    @FXML private TableColumn<InboundOrderItemResponse, InboundOrderItemResponse> differenceColumn;
    @FXML private TableColumn<InboundOrderItemResponse, InboundOrderItemResponse> reasonColumn;
    @FXML private VBox pageRoot;
    @FXML private WarehouseSidebar sidebar;

    private InboundOrderResponse inboundOrder;
    private ObservableList<InboundOrderItemResponse> inboundOrderItems = FXCollections.observableArrayList();

    public InboundOrderProcessUI() {
        this(0);
    }

    public InboundOrderProcessUI(long orderId) {
        this.initialOrderId = orderId;
        FxmlUiHelper.loadSelf(this, "InboundOrderProcessPage.fxml");
    }

    @FXML
    private void initialize() {
        sidebar.setActiveMenu("inbound");
        expectedQuantityField.setEditable(false);
        actualQuantityField.setEditable(false);
        orderIconLabel.setGraphic(createIcon(PACKAGE_CHECK_ICON, "#059669"));
        tableController = new InboundOrderProcessTableController(this, this::onItemsChanged);
        tableController.setupItemsTable();
        if (initialOrderId > 0) {
            setInboundOrder(inboundOrderService.getOrderById(initialOrderId));
        } else {
            setInboundOrder(inboundOrderService.getFirstProcessableOrder());
        }
    }

    @FXML
    private void onBackClick() {
        System.out.println("Nội dung chức năng: Quay lại danh sách đơn nhập kho");
        WarehouseNavigation.showInboundOrderList(pageRoot);
    }

    @FXML
    private void onSaveDraftClick() {
        System.out.println("Nội dung chức năng: Lưu tạm kết quả xử lý đơn nhập kho");
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
        System.out.println("Nội dung chức năng: Xác nhận nhập kho");
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
            WarehouseOrderSyncResult syncResult = inboundOrderService.confirmInboundOrder(
                    inboundOrder.getOrderId(), inboundOrderItems, getProcessNote(), TEMP_INSPECTED_BY_USER_ID);
            setInboundOrder(inboundOrderService.getOrderById(inboundOrder.getOrderId()));
            showInboundSuccess(syncResult);
            System.out.println("Nội dung chức năng: Xác nhận nhập kho thành công");
        } catch (RuntimeException exception) {
            showError("Xác nhận thất bại", exception.getMessage());
        }
    }

    @FXML
    private void onReportMismatchClick() {
        System.out.println("Nội dung chức năng: Từ chối đơn nhập kho");
        showInfo("Từ chối đơn nhập kho", "Chức năng từ chối đang ở mức demo: hệ thống đã ghi nhận thao tác.");
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
        WarehouseStatusBadge.apply(statusLabel, inboundOrder.getStatusCode());
        noteArea.setText(inboundOrder.getNote());
        setInboundOrderItems(FXCollections.observableArrayList(
                inboundOrderService.getOrderItems(inboundOrder.getOrderId())
        ));
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
        return itemReasons.isBlank() ? note : note + " | " + itemReasons;
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

    private void onItemsChanged() {
        refreshTotals();
        itemTable.refresh();
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

    private SVGPath createIcon(String path, String fillColor) {
        SVGPath icon = new SVGPath();
        icon.setContent(path);
        icon.setStyle("-fx-fill: " + fillColor + ";");
        icon.setScaleX(0.86);
        icon.setScaleY(0.86);
        return icon;
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInboundSuccess(WarehouseOrderSyncResult syncResult) {
        String successMessage = "Đơn nhập kho đã cập nhật trạng thái và cộng số lượng thực nhận vào tồn kho.";
        if (syncResult == null || syncResult.skipped()) {
            showInfo("Xác nhận thành công", successMessage);
            return;
        }
        if (syncResult.success()) {
            showInfo("Xác nhận thành công", successMessage + "\n" + syncResult.message());
            return;
        }
        showWarning("Nhập kho thành công, đồng bộ thất bại",
                successMessage + "\n" + syncResult.message());
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public TableView<InboundOrderItemResponse> getItemTable() {
        return itemTable;
    }

    public TableColumn<InboundOrderItemResponse, String> getItemCodeColumn() {
        return itemCodeColumn;
    }

    public TableColumn<InboundOrderItemResponse, String> getItemNameColumn() {
        return itemNameColumn;
    }

    public TableColumn<InboundOrderItemResponse, String> getUnitColumn() {
        return unitColumn;
    }

    public TableColumn<InboundOrderItemResponse, Integer> getOrderedQuantityColumn() {
        return orderedQuantityColumn;
    }

    public TableColumn<InboundOrderItemResponse, InboundOrderItemResponse> getActualQuantityColumn() {
        return actualQuantityColumn;
    }

    public TableColumn<InboundOrderItemResponse, InboundOrderItemResponse> getDifferenceColumn() {
        return differenceColumn;
    }

    public TableColumn<InboundOrderItemResponse, InboundOrderItemResponse> getReasonColumn() {
        return reasonColumn;
    }
}
