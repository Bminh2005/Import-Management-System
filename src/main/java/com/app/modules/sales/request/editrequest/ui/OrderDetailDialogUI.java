package com.app.modules.sales.request.editrequest.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.entity.OrderItem;
import com.app.modules.sales.request.requestdetail.dto.OrderDetailResponse;
import com.app.modules.sales.request.requestdetail.service.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Popup "Chi tiết Đơn hàng" — mở khi nhấn nút Xem ở bảng "Đơn hàng liên quan".
 * Hiển thị thông tin tóm tắt đơn + danh sách mặt hàng (không có giá).
 *
 * Mở qua tiện ích {@link #show(Window, String)}.
 */
public class OrderDetailDialogUI extends VBox {

    @FXML private Label codeLabel;
    @FXML private Label dateLabel;
    @FXML private Label statusLabel;
    @FXML private Label requestCodeLabel;
    @FXML private Label siteLabel;

    @FXML private TableView<OrderItem> itemsTable;
    @FXML private TableColumn<OrderItem, String> codeColumn;
    @FXML private TableColumn<OrderItem, String> nameColumn;
    @FXML private TableColumn<OrderItem, Number> quantityColumn;
    @FXML private TableColumn<OrderItem, String> unitColumn;

    private final RequestService service;
    private Stage stage;

    public OrderDetailDialogUI() {
        this(new RequestService());
    }

    public OrderDetailDialogUI(RequestService service) {
        this.service = service;
        FxmlUiHelper.loadSelf(this, "OrderDetailDialogPage.fxml");
        setupTable();
    }

    /** Tiện ích: mở popup modal trên owner và nạp dữ liệu theo mã đơn. */
    public static void show(Window owner, String orderCode) {
        OrderDetailDialogUI dialog = new OrderDetailDialogUI();
        dialog.loadOrder(orderCode);

        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Chi tiết Đơn hàng");
        Scene scene = new Scene(dialog);
        scene.setFill(null);
        stage.setScene(scene);
        dialog.stage = stage;
        stage.showAndWait();
    }

    private void setupTable() {
        codeColumn.setCellValueFactory(c -> c.getValue().codeProperty());
        nameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        quantityColumn.setCellValueFactory(c -> c.getValue().quantityProperty());
        unitColumn.setCellValueFactory(c -> c.getValue().unitProperty());
    }

    private void loadOrder(String orderCode) {
        OrderDetailResponse detail = service.getOrderDetail(orderCode);
        codeLabel.setText(detail.getCode());
        dateLabel.setText(detail.getOrderDate());
        requestCodeLabel.setText(detail.getRequestCode());
        siteLabel.setText(detail.getSite());

        statusLabel.setText(StatusStyle.requestStatusLabel(detail.getStatus()));
        statusLabel.setStyle(StatusStyle.badgeStyle(detail.getStatus()));

        // Cột Mã hàng đôi khi không kích hoạt cellValueFactory ngay khi
        // dữ liệu set sớm — re-bind chỉ để chắc.
        codeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));

        itemsTable.setItems(detail.getItems());
    }

    @FXML
    private void onClose() {
        if (stage != null) {
            stage.close();
        }
    }
}
