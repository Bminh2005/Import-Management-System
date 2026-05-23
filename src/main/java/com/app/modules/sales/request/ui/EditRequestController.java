package com.app.modules.sales.request.ui;

import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.dto.RequestResponse;
import com.app.modules.sales.request.dto.UpdateRequestDTO;
import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.service.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller cho màn hình "Chỉnh sửa Yêu cầu Nhập hàng".
 * Cho phép sửa số lượng / ngày nhận của từng mặt hàng ngay trên bảng,
 * thêm hoặc xóa mặt hàng, hủy yêu cầu, lưu thay đổi.
 *
 * Theo quy ước README: chỉ gọi service.
 */
public class EditRequestController implements Initializable {

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
    @FXML private TableColumn<RequestItem, Integer> quantityColumn;
    @FXML private TableColumn<RequestItem, String> unitColumn;
    @FXML private TableColumn<RequestItem, String> deliveryDateColumn;
    @FXML private TableColumn<RequestItem, String> statusColumn;
    @FXML private TableColumn<RequestItem, Void> actionsColumn;

    // --- Rejected panel ---
    @FXML private VBox rejectedList;
    @FXML private Label rejectedCountLabel;
    @FXML private Label rejectedEmptyLabel;

    private final RequestService service = new RequestService();
    private RequestResponse current;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemsTable.setEditable(true);
        setupItemsTable();
    }

    private void setupItemsTable() {
        codeColumn.setCellValueFactory(c -> c.getValue().codeProperty());
        nameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        unitColumn.setCellValueFactory(c -> c.getValue().unitProperty());

        // Cột Số lượng: sửa trực tiếp
        quantityColumn.setCellValueFactory(c ->
                c.getValue().quantityProperty().asObject());
        quantityColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(e -> {
            int value = e.getNewValue() == null ? 0 : e.getNewValue();
            RequestItem row = e.getRowValue();
            row.setQuantity(value);
            if (current != null) {
                service.updateItemQuantity(current.getCode(), row.getCode(), value);
            }
            System.out.println("Nội dung chức năng: Cập nhật số lượng "
                    + row.getCode() + " = " + value);
        });

        // Cột Ngày nhận: sửa trực tiếp
        deliveryDateColumn.setCellValueFactory(c -> c.getValue().deliveryDateProperty());
        deliveryDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        deliveryDateColumn.setOnEditCommit(e -> {
            RequestItem row = e.getRowValue();
            row.setDeliveryDate(e.getNewValue());
            if (current != null) {
                service.updateItemDeliveryDate(current.getCode(), row.getCode(),
                        e.getNewValue());
            }
            System.out.println("Nội dung chức năng: Cập nhật ngày nhận "
                    + row.getCode() + " = " + e.getNewValue());
        });

        statusColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        statusColumn.setCellFactory(
                StatusStyle.badgeCellFactory(StatusStyle::itemStatusLabel));

        // Cột Thao tác: nút Xóa
        actionsColumn.setCellFactory(col -> new TableCell<RequestItem, Void>() {
            private final Button deleteBtn = new Button("Xóa");

            {
                deleteBtn.getStyleClass().add("link-button-danger");
                deleteBtn.setOnAction(e -> {
                    RequestItem item = getTableView().getItems().get(getIndex());
                    onDeleteItem(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
    }

    /** Nạp dữ liệu yêu cầu theo mã (gọi service). */
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

        renderRejected(current.getRejectedItems());
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

        Label qty = new Label("Số lượng: " + item.getQuantity() + " " + item.getUnit());
        qty.getStyleClass().add("rejected-sub");

        Label rejectedByBadge = new Label(
                "overseas".equals(item.getRejectedBy())
                        ? "Từ chối bởi Đặt hàng Quốc tế"
                        : "Hủy bởi người dùng");
        rejectedByBadge.setStyle("-fx-padding: 3 10; -fx-background-radius: 10; "
                + "-fx-font-size: 11px; -fx-font-weight: bold; "
                + "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E;");

        Label dateLabel = new Label(item.getRejectedDate());
        dateLabel.getStyleClass().add("rejected-sub");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(12, header, spacer, rejectedByBadge);

        Label reasonTitle = new Label("Lý do:");
        reasonTitle.getStyleClass().add("rejected-reason-title");
        Label reason = new Label(item.getReason());
        reason.getStyleClass().add("rejected-sub");
        reason.setWrapText(true);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox subRow = new HBox(12, qty, spacer2, dateLabel);

        VBox row = new VBox(8, topRow, subRow, reasonTitle, reason);
        row.getStyleClass().add("rejected-row");
        row.setPadding(new Insets(14, 16, 14, 16));
        return row;
    }

    private void onDeleteItem(RequestItem item) {
        if (current == null) return;
        service.removeItem(current.getCode(), item.getCode());
        itemCountLabel.setText(String.valueOf(current.getItems().size()));
        System.out.println("Nội dung chức năng: Xóa mặt hàng " + item.getCode());
    }

    @FXML
    private void onAddItem() {
        if (current == null) return;
        // Tạm thời thêm 1 mặt hàng giả lập; popup chọn mặt hàng làm sau.
        int next = current.getItems().size() + 1;
        RequestItem newItem = new RequestItem(
                String.format("MH-NEW-%02d", next),
                "Mặt hàng mới #" + next, 1, "cái",
                java.time.LocalDate.now().toString(), "pending");
        service.addItem(current.getCode(), newItem);
        itemCountLabel.setText(String.valueOf(current.getItems().size()));
        System.out.println("Nội dung chức năng: Đã thêm mặt hàng " + newItem.getCode());
    }

    @FXML
    private void onSave() {
        if (current == null) return;
        UpdateRequestDTO dto = new UpdateRequestDTO(current.getCode(),
                new ArrayList<>(current.getItems()));
        service.updateRequest(dto);
        System.out.println("Nội dung chức năng: Lưu thay đổi yêu cầu "
                + current.getCode());
    }

    @FXML
    private void onBack() {
        if (current == null) return;
        System.out.println("Nội dung chức năng: Quay lại màn hình chi tiết yêu cầu "
                + current.getCode());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "RequestDetailPage.fxml"));
            Parent root = loader.load();
            RequestDetailController detailController = loader.getController();
            detailController.loadRequest(current.getCode());

            Scene scene = itemsTable.getScene();
            scene.setRoot(root);
            ((Stage) scene.getWindow()).setTitle(
                    "Hệ thống Quản lý Nhập khẩu - Chi tiết Yêu cầu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelRequest() {
        if (current == null) return;
        // Popup hủy sẽ làm sau; tạm thời gọi thẳng service với lý do mặc định.
        service.cancelRequest(current.getCode(), "Hủy thử nghiệm từ UI");
        System.out.println("Nội dung chức năng: Đã hủy yêu cầu " + current.getCode());
    }
}
