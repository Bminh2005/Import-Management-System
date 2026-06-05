package com.app.modules.sales.request.requestlist.controller;

import com.app.common.util.StatusStyle;
import com.app.modules.sales.request.requestlist.dto.RequestListRow;
import com.app.modules.sales.request.requestlist.ui.RequestListUI;
import com.app.modules.sales.request.requestlist.ui.common.RequestListUiConstants;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

import java.util.function.Consumer;

/**
 * Cấu hình bảng và cột hành động cho {@link RequestListUI}.
 */
public class RequestListTableController {

    private final RequestListUI view;

    public RequestListTableController(RequestListUI view) {
        this.view = view;
    }

    public void setupTable(Consumer<String> onViewDetail, Consumer<String> onEditRequest) {
        TableColumn<RequestListRow, String> colId = view.getColId();
        TableColumn<RequestListRow, String> colDate = view.getColDate();
        TableColumn<RequestListRow, Integer> colQuantity = view.getColQuantity();
        TableColumn<RequestListRow, String> colStatus = view.getColStatus();
        TableColumn<RequestListRow, Void> colActions = view.getColActions();

        colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCreatedDate()));
        colQuantity.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getItemCount()));
        colQuantity.setCellFactory(col -> new TableCell<RequestListRow, Integer>() {
            private final Label badge = new Label();

            {
                badge.getStyleClass().add("quantity-badge");
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Integer count, boolean empty) {
                super.updateItem(count, empty);
                if (empty || count == null) {
                    setGraphic(null);
                } else {
                    badge.setText(String.valueOf(count));
                    setGraphic(badge);
                }
            }
        });
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));
        colStatus.setCellFactory(StatusStyle.badgeCellFactory(StatusStyle::requestStatusLabel));

        colActions.getStyleClass().add("actions-column-header");
        colActions.setStyle("-fx-alignment: CENTER;");
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button viewBtn = iconButton(RequestListUiConstants.eyePath());
            private final Button editBtn = iconButton(RequestListUiConstants.editPath());
            private final HBox box = new HBox(6);
            private RequestListRow row;

            {
                getStyleClass().add("actions-cell");
                viewBtn.getStyleClass().add("icon-btn");
                editBtn.getStyleClass().add("icon-btn");
                box.setAlignment(Pos.CENTER);
                Tooltip.install(viewBtn, new Tooltip("Xem chi tiết yêu cầu"));
                Tooltip.install(editBtn, new Tooltip("Chỉnh sửa yêu cầu"));
                viewBtn.setOnAction(e -> {
                    e.consume();
                    if (row != null && onViewDetail != null) {
                        onViewDetail.accept(row.getCode());
                    }
                });
                editBtn.setOnAction(e -> {
                    e.consume();
                    if (row != null && onEditRequest != null) {
                        onEditRequest.accept(row.getCode());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    row = null;
                    setGraphic(null);
                    return;
                }
                row = getTableRow() != null ? getTableRow().getItem() : null;
                if (row == null && getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                    row = getTableView().getItems().get(getIndex());
                }
                box.getChildren().clear();
                if (row != null) {
                    box.getChildren().add(viewBtn);
                    if (!"completed".equals(row.getStatus())) {
                        box.getChildren().add(editBtn);
                    }
                }
                setGraphic(box);
                setAlignment(Pos.CENTER);
            }
        });

        view.getTableRequests().setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                RequestListRow row = view.getTableRequests().getSelectionModel().getSelectedItem();
                if (row != null && onViewDetail != null) {
                    onViewDetail.accept(row.getCode());
                }
            }
        });
    }

    private static Button iconButton(String svg) {
        SVGPath icon = new SVGPath();
        icon.setContent(svg);
        icon.setStyle("-fx-fill: " + RequestListUiConstants.ACTION_ICON_COLOR + ";");
        icon.setScaleX(0.85);
        icon.setScaleY(0.85);
        Button btn = new Button();
        btn.setGraphic(icon);
        return btn;
    }
}
