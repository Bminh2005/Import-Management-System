package com.app.modules.sales.request.requestdetail.ui;

import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.entity.RelatedOrder;
import com.app.modules.sales.request.entity.RequestItem;
import com.app.modules.sales.request.requestdetail.ui.common.RequestDetailUiConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

/**
 * Cấu hình bảng mặt hàng và bảng đơn hàng liên quan cho {@link RequestDetailUI}.
 */
public class RequestDetailTableController {

    private final RequestDetailUI view;

    public RequestDetailTableController(RequestDetailUI view) {
        this.view = view;
    }

    public void setupTables(Consumer<String> onViewOrder) {
        setupItemsTable();
        setupOrdersTable(onViewOrder);
    }

    private void setupItemsTable() {
        TableColumn<RequestItem, String> codeColumn = view.getCodeColumn();
        TableColumn<RequestItem, String> nameColumn = view.getNameColumn();
        TableColumn<RequestItem, Number> quantityColumn = view.getQuantityColumn();
        TableColumn<RequestItem, String> unitColumn = view.getUnitColumn();
        TableColumn<RequestItem, String> deliveryDateColumn = view.getDeliveryDateColumn();
        TableColumn<RequestItem, String> statusColumn = view.getStatusColumn();

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

    private void setupOrdersTable(Consumer<String> onViewOrder) {
        TableColumn<RelatedOrder, String> orderCodeColumn = view.getOrderCodeColumn();
        TableColumn<RelatedOrder, String> orderDateColumn = view.getOrderDateColumn();
        TableColumn<RelatedOrder, String> orderSiteColumn = view.getOrderSiteColumn();
        TableColumn<RelatedOrder, Number> orderItemCountColumn = view.getOrderItemCountColumn();
        TableColumn<RelatedOrder, String> orderStatusColumn = view.getOrderStatusColumn();
        TableColumn<RelatedOrder, Void> orderActionsColumn = view.getOrderActionsColumn();

        orderCodeColumn.setCellValueFactory(c -> c.getValue().codeProperty());
        orderDateColumn.setCellValueFactory(c -> c.getValue().orderDateProperty());
        orderSiteColumn.setCellValueFactory(c -> c.getValue().siteProperty());
        orderItemCountColumn.setCellValueFactory(c -> c.getValue().itemCountProperty());

        orderStatusColumn.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        orderStatusColumn.setCellFactory(
                StatusStyle.badgeCellFactory(StatusStyle::requestStatusLabel));

        orderActionsColumn.setCellFactory(col -> new TableCell<RelatedOrder, Void>() {
            private final Button viewBtn = iconButton(RequestDetailUiConstants.eyePath());
            private final HBox box = new HBox(viewBtn);

            {
                viewBtn.getStyleClass().add("icon-btn");
                box.setAlignment(javafx.geometry.Pos.CENTER);
                viewBtn.setOnAction(e -> {
                    RelatedOrder order = getTableView().getItems().get(getIndex());
                    if (onViewOrder != null) {
                        onViewOrder.accept(order.getCode());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private static Button iconButton(String svg) {
        SVGPath icon = new SVGPath();
        icon.setContent(svg);
        icon.setStyle("-fx-fill: " + RequestDetailUiConstants.ACTION_ICON_COLOR + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        Button btn = new Button();
        btn.setGraphic(icon);
        return btn;
    }
}
