package com.app.modules.sales.request.editrequest.ui;

import com.app.common.ui.components.DatePickerCell;
import com.app.common.ui.components.IntegerEditCell;
import com.app.common.ui.components.SvgButton;
import com.app.modules.sales.request.editrequest.ui.common.RequestUiConstants;
import com.app.modules.sales.request.entity.RequestItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

/**
 * Controller phụ quản lý cấu hình các cột và ô chỉnh sửa (Cell Factories)
 * cho bảng mặt hàng trong màn hình EditRequestUI (SRP).
 */
public class EditRequestTableController {

    private final EditRequestUI view;
    private final Consumer<RequestItem> onDeleteItem;

    public EditRequestTableController(EditRequestUI view, Consumer<RequestItem> onDeleteItem) {
        this.view = view;
        this.onDeleteItem = onDeleteItem;
    }

    public void setupTables(boolean editable) {
        setupItemsTable(editable);
    }

    private void setupItemsTable(boolean editable) {
        view.getCodeColumn().setCellValueFactory(c -> c.getValue().codeProperty());
        view.getNameColumn().setCellValueFactory(c -> c.getValue().nameProperty());
        view.getNameColumn().setCellFactory(col -> new TableCell<RequestItem, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setCursor(null);
                    setOnMouseClicked(null);
                } else {
                    setText(item);
                    setCursor(javafx.scene.Cursor.HAND);
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {
                            javafx.scene.control.Tooltip tooltip = new javafx.scene.control.Tooltip(item);
                            tooltip.setStyle("-fx-font-size: 14px; -fx-background-color: #1e293b; -fx-text-fill: #f8fafc; -fx-padding: 8px; -fx-background-radius: 4px; -fx-wrap-text: true; -fx-max-width: 400;");
                            tooltip.setAutoHide(true);
                            tooltip.show(getScene().getWindow(), event.getScreenX(), event.getScreenY() + 10);
                        }
                    });
                }
            }
        });
        view.getUnitColumn().setCellValueFactory(c -> c.getValue().unitProperty());

        view.getQuantityColumn().setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()));
        view.getQuantityColumn().setCellFactory(col -> new IntegerEditCell<>(
                RequestItem::getQuantity,
                (item, val) -> {
                    item.setQuantity(val);
                    view.setDirty(true);
                },
                editable
        ));

        view.getDeliveryDateColumn().setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()));
        view.getDeliveryDateColumn().setCellFactory(col -> new DatePickerCell<>(
                RequestItem::getDeliveryDate,
                (item, val) -> {
                    item.setDeliveryDate(val);
                    view.setDirty(true);
                    view.getItemsTable().refresh();
                },
                editable
        ));

        view.getActionsColumn().setCellFactory(col -> new TableCell<RequestItem, Void>() {
            private final Button deleteBtn = new SvgButton(RequestUiConstants.TRASH_ICON_SVG, "#DC2626");

            {
                deleteBtn.getStyleClass().add("icon-delete");
                deleteBtn.setOnAction(e -> {
                    RequestItem item = getTableView().getItems().get(getIndex());
                    if (onDeleteItem != null) {
                        onDeleteItem.accept(item);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || !editable) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(deleteBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        
        view.getItemsTable().refresh();
    }
}
