package com.app.modules.sales.request.requestdetail.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.requestdetail.dto.RequestResponse;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.requestdetail.service.RequestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

/**
 * UI Class cho màn "Xem chi tiết Yêu cầu Nhập hàng".
 * Là 1 component nhúng vào trong MainLayoutUI (đã có sidebar + header).
 *
 * Caller có thể đăng ký {@link #setOnBack(Runnable)} cho nút ← trên header.
 *
 * Theo quy ước README: UI chỉ gọi service, không truy cập repository.
 */
public class RequestDetailUI extends ScrollPane {

    // --- Header ---
    @FXML private Button backButton;
    @FXML private Label titleLabel;

    // --- Info card ---
    @FXML private Label codeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label itemCountLabel;
    @FXML private Label createdByLabel;
    @FXML private Label assignedToLabel;

    // --- Items table ---
    @FXML private TableView<RequestItem> itemsTable;
    @FXML private TableColumn<RequestItem, String> codeColumn;
    @FXML private TableColumn<RequestItem, String> nameColumn;
    @FXML private TableColumn<RequestItem, Number> quantityColumn;
    @FXML private TableColumn<RequestItem, String> unitColumn;
    @FXML private TableColumn<RequestItem, String> deliveryDateColumn;
    @FXML private TableColumn<RequestItem, String> statusColumn;

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

    private Runnable backAction;
    private Consumer<String> onEdit;

    public RequestDetailUI() {
        this(new RequestService());
    }

    public RequestDetailUI(RequestService service) {
        this.service = service;
        FxmlUiHelper.loadSelf(this, "RequestDetailPage.fxml");
        setupItemsTable();
        setupOrdersTable();
    }

    public void setOnBack(Runnable callback) {
        this.backAction = callback;
    }

    public void setOnEdit(Consumer<String> callback) {
        this.onEdit = callback;
    }

    private void setupItemsTable() {
        codeColumn.setCellValueFactory(c -> c.getValue().codeProperty());
        nameColumn.setCellValueFactory(c -> c.getValue().nameProperty());
        quantityColumn.setCellValueFactory(c -> c.getValue().quantityProperty());
        unitColumn.setCellValueFactory(c -> c.getValue().unitProperty());
        deliveryDateColumn.setCellValueFactory(c -> c.getValue().deliveryDateProperty());

        statusColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        statusColumn.setCellFactory(
                StatusStyle.badgeCellFactory(StatusStyle::itemStatusLabel));
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
            private final Button viewBtn = iconButton(eyePath(), "#475569");
            private final HBox box = new HBox(viewBtn);

            {
                viewBtn.getStyleClass().add("icon-btn");
                box.setAlignment(javafx.geometry.Pos.CENTER);
                viewBtn.setOnAction(e -> {
                    RelatedOrder order = getTableView().getItems().get(getIndex());
                    System.out.println("Nội dung chức năng: Xem chi tiết đơn hàng "
                            + order.getCode());
                    OrderDetailDialogUI.show(
                            getScene() != null ? getScene().getWindow() : null,
                            order.getCode());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private static Button iconButton(String svg, String fill) {
        SVGPath icon = new SVGPath();
        icon.setContent(svg);
        icon.setStyle("-fx-fill: " + fill + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        Button btn = new Button();
        btn.setGraphic(icon);
        return btn;
    }

    private static String eyePath() {
        return "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5"
                + "C21.27 7.61 17 4.5 12 4.5zM12 17a5 5 0 1 1 0-10 5 5 0 0 1 0 10zm0-8a3 3 0 1 0 0 6 3 3 0 0 0 0-6z";
    }

    /**
     * Nạp dữ liệu yêu cầu theo mã (id số trong bảng ImportRequest).
     * @return true nếu tải thành công
     */
    public boolean loadRequest(String code) {
        try {
            this.current = service.getRequestDetail(code);
            render();
            return true;
        } catch (Exception ex) {
            this.current = null;
            showLoadError(code, ex);
            return false;
        }
    }

    private void showLoadError(String code, Exception ex) {
        String detail = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không tải được yêu cầu");
        alert.setHeaderText("Mã yêu cầu: " + code);
        alert.setContentText(detail + "\n\nKiểm tra kết nối Supabase trong PostgreSQLProvider "
                + "và đảm bảo bản ghi tồn tại trong bảng ImportRequest.");
        alert.showAndWait();
    }

    private void render() {
        if (current == null) return;

        titleLabel.setText("Chi tiết Yêu cầu: " + current.getCode());
        codeLabel.setText(current.getCode());
        createdDateLabel.setText(current.getCreatedDate());
        itemCountLabel.setText(String.valueOf(current.getItemCount()));
        createdByLabel.setText(current.getCreatedBy());
        String assigned = current.getAssignedTo();
        assignedToLabel.setText(assigned == null || assigned.isBlank()
                ? "Chưa phân công" : assigned);

        statusLabel.setText(StatusStyle.requestStatusLabel(current.getStatus()));
        statusLabel.setStyle(StatusStyle.badgeStyle(current.getStatus()));

        itemsTable.setItems(current.getItems());

        renderOrders(current.getOrders());
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

    @FXML
    private void onBack() {
        if (backAction != null) {
            backAction.run();
            return;
        }
        System.out.println("Nội dung chức năng: Quay lại danh sách yêu cầu "
                + (current != null ? current.getCode() : ""));
    }

    @FXML
    private void onEdit() {
        if (current == null) return;
        if (onEdit != null) {
            onEdit.accept(current.getCode());
            return;
        }
        System.out.println("Nội dung chức năng: Chỉnh sửa yêu cầu " + current.getCode());
    }

}
