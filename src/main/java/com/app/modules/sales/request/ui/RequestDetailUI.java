package com.app.modules.sales.request.ui;

import com.app.common.ui.components.AppSidebarUI;
import com.app.common.ui.components.AppTopBarUI;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.dto.RequestResponse;
import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.service.RequestService;
import com.app.modules.warehouse.inventory.ui.InventoryListUI;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * UI Class cho màn hình "Xem chi tiết Yêu cầu Nhập hàng".
 * Layout: BorderPane (sidebar bên trái + topbar bên trên + content ở giữa).
 *
 * Theo quy ước README: chỉ gọi service, không truy cập trực tiếp repository
 * và không chứa business logic.
 */
public class RequestDetailUI extends BorderPane {

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

    private final RequestService service = new RequestService();
    private RequestResponse current;

    public RequestDetailUI() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "RequestDetailPage.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        attachSidebarAndTopBar();
        setupItemsTable();
        setupOrdersTable();
    }

    /** Gắn sidebar + topbar vào layout chính. */
    private void attachSidebarAndTopBar() {
        AppSidebarUI sidebar = new AppSidebarUI();
        sidebar.setActive(AppSidebarUI.KEY_REQUEST);
        sidebar.setBrandSubtitle("Bộ phận Bán hàng");
        sidebar.setRole("Bộ phận Bán hàng");
        sidebar.setOnInventory(() -> navigateTo(new InventoryListUI(),
                "Hệ thống Quản lý Nhập khẩu - Mặt hàng Tồn kho"));
        setLeft(sidebar);

        AppTopBarUI topbar = new AppTopBarUI();
        topbar.setUserName("Nguyễn Văn A");
        topbar.setRole("Bộ phận Bán hàng");
        setTop(topbar);
    }

    private void navigateTo(javafx.scene.Parent root, String title) {
        Scene scene = getScene();
        if (scene == null) return;
        scene.setRoot(root);
        ((Stage) scene.getWindow()).setTitle(title);
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
            private final Button viewBtn = new Button("Xem");
            private final Button cancelBtn = new Button("Hủy");
            private final HBox box = new HBox(8, viewBtn, cancelBtn);

            {
                viewBtn.getStyleClass().add("link-button");
                cancelBtn.getStyleClass().add("link-button-danger");
                viewBtn.setOnAction(e -> {
                    RelatedOrder order = getTableView().getItems().get(getIndex());
                    System.out.println("Nội dung chức năng: Xem chi tiết đơn hàng "
                            + order.getCode());
                });
                cancelBtn.setOnAction(e -> {
                    RelatedOrder order = getTableView().getItems().get(getIndex());
                    System.out.println("Nội dung chức năng: Hủy đơn hàng "
                            + order.getCode());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
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

        codeLabel.setText(current.getCode());
        createdDateLabel.setText(current.getCreatedDate());
        itemCountLabel.setText(String.valueOf(current.getItemCount()));
        createdByLabel.setText(current.getCreatedBy());
        assignedToLabel.setText(current.getAssignedTo());

        statusLabel.setText(StatusStyle.requestStatusLabel(current.getStatus()));
        statusLabel.setStyle(StatusStyle.badgeStyle(current.getStatus()));

        itemsTable.setItems(current.getItems());

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
    private void onEdit() {
        if (current == null) return;
        System.out.println("Nội dung chức năng: Chuyển sang màn hình Chỉnh sửa yêu cầu "
                + current.getCode());
        EditRequestUI editUI = new EditRequestUI();
        editUI.loadRequest(current.getCode());
        navigateTo(editUI, "Hệ thống Quản lý Nhập khẩu - Chỉnh sửa Yêu cầu");
    }

    @FXML
    private void onClose() {
        System.out.println("Nội dung chức năng: Đóng màn hình chi tiết yêu cầu "
                + (current != null ? current.getCode() : ""));
        Stage stage = (Stage) itemsTable.getScene().getWindow();
        stage.close();
    }
}
