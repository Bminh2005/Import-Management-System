package com.app.modules.sales.request.requestdetail.ui;

import com.app.common.util.FxmlUiHelper;
import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.requestdetail.dto.RequestResponse;
import com.app.modules.sales.request.entity.RejectedItem;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.requestdetail.ui.components.RejectedItemRowUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.util.function.Consumer;

/**
 * View cho màn "Xem chi tiết Yêu cầu Nhập hàng".
 * Logic điều phối nằm ở {@link com.app.modules.sales.request.requestdetail.controller.RequestDetailController}.
 */
public class RequestDetailUI extends ScrollPane {

    @FXML private Button backButton;
    @FXML private Label titleLabel;

    @FXML private Label codeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label statusLabel;
    @FXML private Label itemCountLabel;
    @FXML private Label createdByLabel;
    @FXML private Label assignedToLabel;

    @FXML private TableView<RequestItem> itemsTable;
    @FXML private TableColumn<RequestItem, String> codeColumn;
    @FXML private TableColumn<RequestItem, String> nameColumn;
    @FXML private TableColumn<RequestItem, Number> quantityColumn;
    @FXML private TableColumn<RequestItem, String> unitColumn;
    @FXML private TableColumn<RequestItem, String> deliveryDateColumn;
    @FXML private TableColumn<RequestItem, String> statusColumn;

    @FXML private VBox rejectedList;
    @FXML private Label rejectedCountLabel;
    @FXML private Label rejectedEmptyLabel;

    @FXML private TableView<RelatedOrder> ordersTable;
    @FXML private TableColumn<RelatedOrder, String> orderCodeColumn;
    @FXML private TableColumn<RelatedOrder, String> orderDateColumn;
    @FXML private TableColumn<RelatedOrder, String> orderSiteColumn;
    @FXML private TableColumn<RelatedOrder, Number> orderItemCountColumn;
    @FXML private TableColumn<RelatedOrder, String> orderStatusColumn;
    @FXML private TableColumn<RelatedOrder, Void> orderActionsColumn;
    @FXML private Label orderCountLabel;
    @FXML private Label orderEmptyLabel;

    private String currentRequestCode = "";
    private Runnable onBack;
    private Consumer<String> onEdit;

    public RequestDetailUI() {
        FxmlUiHelper.loadSelf(this, "RequestDetailPage.fxml");
    }

    public Window getSceneWindow() {
        return getScene() != null ? getScene().getWindow() : null;
    }

    public void setOnBack(Runnable callback) {
        this.onBack = callback;
    }

    public void setOnEdit(Consumer<String> callback) {
        this.onEdit = callback;
    }

    public String getCurrentRequestCode() {
        return currentRequestCode;
    }

    public void display(RequestResponse current) {
        if (current == null) return;

        currentRequestCode = current.getCode();
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
        renderRejected(current.getRejectedItems());
        renderOrders(current.getOrders());
    }

    public void clearCurrentRequestCode() {
        currentRequestCode = "";
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
            rejectedList.getChildren().add(RejectedItemRowUI.build(item));
        }
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
        if (onBack != null) {
            onBack.run();
        }
    }

    @FXML
    private void onEdit() {
        if (onEdit != null && !currentRequestCode.isBlank()) {
            onEdit.accept(currentRequestCode);
        }
    }

    public TableColumn<RequestItem, String> getCodeColumn() { return codeColumn; }
    public TableColumn<RequestItem, String> getNameColumn() { return nameColumn; }
    public TableColumn<RequestItem, Number> getQuantityColumn() { return quantityColumn; }
    public TableColumn<RequestItem, String> getUnitColumn() { return unitColumn; }
    public TableColumn<RequestItem, String> getDeliveryDateColumn() { return deliveryDateColumn; }
    public TableColumn<RequestItem, String> getStatusColumn() { return statusColumn; }

    public TableColumn<RelatedOrder, String> getOrderCodeColumn() { return orderCodeColumn; }
    public TableColumn<RelatedOrder, String> getOrderDateColumn() { return orderDateColumn; }
    public TableColumn<RelatedOrder, String> getOrderSiteColumn() { return orderSiteColumn; }
    public TableColumn<RelatedOrder, Number> getOrderItemCountColumn() { return orderItemCountColumn; }
    public TableColumn<RelatedOrder, String> getOrderStatusColumn() { return orderStatusColumn; }
    public TableColumn<RelatedOrder, Void> getOrderActionsColumn() { return orderActionsColumn; }
}
