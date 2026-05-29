package com.app.modules.sales.request.editrequest.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.editrequest.dto.RequestResponse;
import com.app.modules.sales.request.editrequest.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.editrequest.service.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * UI Class cho màn "Chỉnh sửa Yêu cầu Nhập hàng".
 * Cho phép sửa số lượng / ngày nhận của từng mặt hàng ngay trên bảng,
 * thêm hoặc xóa mặt hàng, hủy yêu cầu, lưu thay đổi. Đồng thời hiển thị
 * danh sách mặt hàng bị từ chối/hủy và đơn hàng liên quan (chỉ đọc).
 *
 * Caller đăng ký {@link #setOnBack(Runnable)} để xử lý thoát màn hình.
 * Theo quy ước README: UI chỉ gọi service.
 */
public class EditRequestUI extends ScrollPane {

    @FXML private Label titleLabel;

    // --- Info card ---
    @FXML private Label codeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label itemCountLabel;

    // --- Items table ---
    @FXML private TableView<RequestItem> itemsTable;
    @FXML private TableColumn<RequestItem, String> codeColumn;
    @FXML private TableColumn<RequestItem, String> nameColumn;
    @FXML private TableColumn<RequestItem, RequestItem> quantityColumn;
    @FXML private TableColumn<RequestItem, String> unitColumn;
    @FXML private TableColumn<RequestItem, RequestItem> deliveryDateColumn;
    @FXML private TableColumn<RequestItem, String> statusColumn;
    @FXML private TableColumn<RequestItem, Void> actionsColumn;

    // --- Rejected panel ---
    @FXML private VBox rejectedList;
    @FXML private Label rejectedCountLabel;
    @FXML private Label rejectedEmptyLabel;

    // --- Related orders ---
    @FXML private TableView<RelatedOrder> ordersTable;
    @FXML private TableColumn<RelatedOrder, String> orderCodeColumn;
    @FXML private TableColumn<RelatedOrder, String> orderDateColumn;
    @FXML private TableColumn<RelatedOrder, String> orderSiteColumn;
    @FXML private TableColumn<RelatedOrder, Number> orderItemCountColumn;
    @FXML private TableColumn<RelatedOrder, String> orderStatusColumn;
    @FXML private TableColumn<RelatedOrder, Void> orderActionsColumn;
    @FXML private Label orderCountLabel;
    @FXML private Label orderEmptyLabel;

    private final RequestService service;
    private RequestResponse current;
    private boolean dirty = false;

    private Runnable onBack;
    private Consumer<String> onSaved;

    public EditRequestUI() {
        this(new RequestService());
    }

    public EditRequestUI(RequestService service) {
        this.service = service;
        FxmlUiHelper.loadSelf(this, "EditRequestPage.fxml");
        itemsTable.setEditable(false);
        setupItemsTable();
        setupOrdersTable();
    }

    public void setOnBack(Runnable callback) { this.onBack = callback; }
    public void setOnSaved(Consumer<String> callback) { this.onSaved = callback; }

    private void setupItemsTable() {
        codeColumn.setCellValueFactory(c -> c.getValue().codeProperty());
        nameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        unitColumn.setCellValueFactory(c -> c.getValue().unitProperty());

        quantityColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue()));
        quantityColumn.setCellFactory(col -> new QuantityCell());

        deliveryDateColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue()));
        deliveryDateColumn.setCellFactory(col -> new DeliveryDateCell());

        statusColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        statusColumn.setCellFactory(
                StatusStyle.badgeCellFactory(StatusStyle::itemStatusLabel));

        actionsColumn.setCellFactory(col -> new TableCell<RequestItem, Void>() {
            private final Button deleteBtn = createIconButton(trashPath(), "#DC2626");

            {
                deleteBtn.getStyleClass().add("icon-delete");
                deleteBtn.setOnAction(e -> {
                    RequestItem item = getTableView().getItems().get(getIndex());
                    onDeleteItem(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(deleteBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    private void setupOrdersTable() {
        orderCodeColumn.setCellValueFactory(c -> c.getValue().codeProperty());
        orderDateColumn.setCellValueFactory(c -> c.getValue().orderDateProperty());
        orderSiteColumn.setCellValueFactory(c -> c.getValue().siteProperty());
        orderItemCountColumn.setCellValueFactory(c -> c.getValue().itemCountProperty());

        orderStatusColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        orderStatusColumn.setCellFactory(
                StatusStyle.badgeCellFactory(StatusStyle::requestStatusLabel));

        orderActionsColumn.setCellFactory(col -> new TableCell<RelatedOrder, Void>() {
            private final Button viewBtn = createIconButton(eyePath(), "#475569");
            private final Button cancelBtn = createIconButton(crossPath(), "#DC2626");
            private final HBox box = new HBox(4, viewBtn, cancelBtn);

            {
                viewBtn.getStyleClass().add("icon-btn");
                cancelBtn.getStyleClass().addAll("icon-btn", "icon-btn-danger");
                box.setAlignment(Pos.CENTER);
                viewBtn.setOnAction(e -> {
                    RelatedOrder order = getTableView().getItems().get(getIndex());
                    System.out.println("Nội dung chức năng: Xem chi tiết đơn hàng "
                            + order.getCode());
                    OrderDetailDialogUI.show(
                            getScene() != null ? getScene().getWindow() : null,
                            order.getCode());
                });
                cancelBtn.setOnAction(e -> {
                    RelatedOrder order = getTableView().getItems().get(getIndex());
                    System.out.println("Nội dung chức năng: Hủy đơn hàng "
                            + order.getCode());
                    CancelOrderDialogUI.show(
                            getScene() != null ? getScene().getWindow() : null,
                            order,
                            () -> {
                                if (current != null) loadRequest(current.getCode());
                            });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private static Button createIconButton(String svg, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(svg);
        icon.setStyle("-fx-fill: " + fill + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        Button btn = new Button();
        btn.setGraphic(icon);
        return btn;
    }

    private static String trashPath() {
        return "M6 19a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z";
    }
    private static String eyePath() {
        return "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5"
                + "C21.27 7.61 17 4.5 12 4.5zM12 17a5 5 0 1 1 0-10 5 5 0 0 1 0 10zm0-8a3 3 0 1 0 0 6 3 3 0 0 0 0-6z";
    }
    private static String crossPath() {
        return "M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59"
                + " 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z";
    }

    /** Cell hiển thị TextField sửa số lượng trực tiếp. */
    private class QuantityCell extends TableCell<RequestItem, RequestItem> {
        private final TextField field = new TextField();

        QuantityCell() {
            field.getStyleClass().add("edit-cell-field");
            field.setPrefWidth(80);
            field.focusedProperty().addListener((obs, oldF, newF) -> {
                if (!newF) commit();
            });
            field.setOnAction(e -> commit());
        }

        private void commit() {
            RequestItem row = getItem();
            if (row == null) return;
            try {
                int value = Integer.parseInt(field.getText().trim());
                if (value < 0) value = 0;
                if (value == row.getQuantity()) return;
                row.setQuantity(value);
                dirty = true;
            } catch (NumberFormatException e) {
                field.setText(String.valueOf(row.getQuantity()));
            }
        }

        @Override
        protected void updateItem(RequestItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                field.setText(String.valueOf(item.getQuantity()));
                setGraphic(field);
            }
        }
    }

    /** Cell hiển thị DatePicker sửa ngày nhận trực tiếp. */
    private class DeliveryDateCell extends TableCell<RequestItem, RequestItem> {
        private final DatePicker picker = new DatePicker();

        DeliveryDateCell() {
            picker.getStyleClass().add("edit-cell-field");
            picker.setPrefWidth(140);
            picker.valueProperty().addListener((obs, oldV, newV) -> {
                RequestItem row = getItem();
                if (row == null || newV == null) return;
                String iso = newV.toString();
                if (iso.equals(row.getDeliveryDate())) return;
                // Ngày nhận là field cấp yêu cầu -> đồng bộ cho mọi mặt hàng.
                for (RequestItem it : itemsTable.getItems()) it.setDeliveryDate(iso);
                dirty = true;
                itemsTable.refresh();
            });
        }

        @Override
        protected void updateItem(RequestItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                LocalDate value = parse(item.getDeliveryDate());
                picker.setValue(value);
                setGraphic(picker);
            }
        }

        private LocalDate parse(String text) {
            if (text == null || text.isBlank()) return null;
            try {
                return LocalDate.parse(text);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }

    /** Nạp dữ liệu yêu cầu theo mã. */
    public void loadRequest(String code) {
        this.current = service.getRequestDetail(code);
        render();
    }

    private void render() {
        if (current == null) return;

        titleLabel.setText("Chỉnh sửa Yêu cầu: " + current.getCode());
        codeLabel.setText(current.getCode());
        createdDateLabel.setText(current.getCreatedDate());
        itemCountLabel.setText(String.valueOf(current.getItemCount()));

        statusLabel.setText(StatusStyle.requestStatusLabel(current.getStatus()));
        statusLabel.setStyle(StatusStyle.badgeStyle(current.getStatus()));

        itemsTable.setItems(current.getItems());
        current.getItems().addListener((javafx.collections.ListChangeListener<RequestItem>)
                c -> itemCountLabel.setText(String.valueOf(current.getItems().size())));

        renderRejected(current.getRejectedItems());
        renderOrders(current.getOrders());
    }

    private void renderRejected(ObservableList<RejectedItem> items) {
        rejectedList.getChildren().clear();
        int size = items == null ? 0 : items.size();
        rejectedCountLabel.setText("(" + size + " mặt hàng)");

        boolean hasItems = size > 0;
        rejectedList.setVisible(hasItems);
        rejectedList.setManaged(hasItems);
        rejectedEmptyLabel.setVisible(!hasItems);
        rejectedEmptyLabel.setManaged(!hasItems);
        if (!hasItems) return;

        for (RejectedItem item : items) {
            rejectedList.getChildren().add(buildRejectedRow(item));
        }
    }

    private VBox buildRejectedRow(RejectedItem item) {
        Label codeChip = new Label(item.getCode());
        codeChip.getStyleClass().add("code-chip");

        Label name = new Label(item.getName());
        name.getStyleClass().add("rejected-name");

        HBox header = new HBox(10, name, codeChip);
        header.setAlignment(Pos.CENTER_LEFT);

        Label qty = new Label("Số lượng: " + item.getQuantity() + " " + item.getUnit());
        qty.getStyleClass().add("rejected-sub");

        boolean isOverseas = "overseas".equals(item.getRejectedBy());
        Label rejectedByBadge = new Label(
                isOverseas ? "Từ chối bởi Đặt hàng Quốc tế" : "Hủy bởi người dùng");
        rejectedByBadge.getStyleClass().add(
                isOverseas ? "rejected-badge-overseas" : "rejected-badge-user");

        Label dateLabel = new Label(item.getRejectedDate());
        dateLabel.getStyleClass().add("rejected-sub");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox rightBox = new VBox(4, rejectedByBadge, dateLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox topRow = new HBox(12, header, spacer, rightBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label reasonTitle = new Label("Lý do:");
        reasonTitle.getStyleClass().add("rejected-reason-title");
        Label reason = new Label(item.getReason());
        reason.getStyleClass().add("rejected-sub");
        reason.setWrapText(true);

        VBox row = new VBox(8, topRow, qty, reasonTitle, reason);
        row.getStyleClass().add("rejected-row");
        row.setPadding(new Insets(14, 16, 14, 16));
        return row;
    }

    private void renderOrders(ObservableList<RelatedOrder> orders) {
        ordersTable.setItems(orders);
        int size = orders == null ? 0 : orders.size();
        orderCountLabel.setText("(" + size + " đơn)");

        boolean hasOrders = size > 0;
        ordersTable.setVisible(hasOrders);
        ordersTable.setManaged(hasOrders);
        orderEmptyLabel.setVisible(!hasOrders);
        orderEmptyLabel.setManaged(!hasOrders);
    }

    private void onDeleteItem(RequestItem item) {
        if (current == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc muốn xóa mặt hàng " + item.getCode() + " không?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Xác nhận xóa mặt hàng");
        confirm.initOwner(getScene() != null ? getScene().getWindow() : null);
        confirm.showAndWait()
                .filter(b -> b == ButtonType.OK)
                .ifPresent(b -> {
                    current.getItems().remove(item);
                    dirty = true;
                    System.out.println("Nội dung chức năng: Xóa mặt hàng " + item.getCode());
                });
    }

    @FXML
    private void onAddItem() {
        if (current == null) return;
        javafx.stage.Window owner = getScene() != null ? getScene().getWindow() : null;
        SelectProductDialogUI.show(owner, current.getCode(), product ->
                EnterItemInfoDialogUI.show(owner, product, newItem -> {
                    current.getItems().add(newItem);
                    dirty = true;
                    System.out.println("Nội dung chức năng: Đã thêm mặt hàng "
                            + newItem.getCode());
                }));
    }

    @FXML
    private void onSave() {
        if (current == null) return;
        UpdateRequestDTO dto = new UpdateRequestDTO(current.getCode(),
                new ArrayList<>(current.getItems()));
        service.updateRequest(dto);
        dirty = false;
        Alert info = new Alert(Alert.AlertType.INFORMATION,
                "Đã lưu thay đổi yêu cầu " + current.getCode());
        info.setHeaderText(null);
        info.showAndWait();
        System.out.println("Nội dung chức năng: Lưu thay đổi yêu cầu "
                + current.getCode());
        if (onSaved != null) onSaved.accept(current.getCode());
    }

    @FXML
    private void onBack() {
        if (dirty) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Mọi thay đổi chưa lưu sẽ bị hủy. Bạn có chắc muốn quay lại?",
                    ButtonType.OK, ButtonType.CANCEL);
            confirm.setHeaderText("Thoát mà không lưu");
            confirm.initOwner(getScene() != null ? getScene().getWindow() : null);
            if (confirm.showAndWait().filter(b -> b == ButtonType.OK).isEmpty()) {
                return;
            }
        }
        System.out.println("Nội dung chức năng: Quay lại "
                + (current != null ? current.getCode() : ""));
        if (onBack != null) onBack.run();
    }
}
