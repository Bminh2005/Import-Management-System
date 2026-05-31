package com.app.modules.sales.request.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.dto.RequestResponse;
import com.app.modules.sales.request.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.service.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
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
import javafx.scene.shape.SVGPath;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * UI Class cho màn "Chỉnh sửa Yêu cầu Nhập hàng".
 * Cho phép sửa số lượng / ngày nhận của từng mặt hàng ngay trên bảng,
 * thêm hoặc xóa mặt hàng, lưu thay đổi. Chỉ gồm 2 mục: thông tin yêu cầu
 * và danh sách mặt hàng.
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
