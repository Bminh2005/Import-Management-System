package com.app.modules.warehouse.common.ui;

import javafx.scene.control.TableCell;

public class WarehouseStatusCell<S> extends TableCell<S, String> {
    private final WarehouseStatusBadge badge = new WarehouseStatusBadge();

    @Override
    protected void updateItem(String statusCode, boolean empty) {
        super.updateItem(statusCode, empty);
        if (empty || statusCode == null) {
            setGraphic(null);
            setText(null);
            return;
        }
        badge.setStatusCode(statusCode);
        setGraphic(badge);
        setText(null);
    }
}
