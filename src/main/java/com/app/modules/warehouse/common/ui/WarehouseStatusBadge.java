package com.app.modules.warehouse.common.ui;

import javafx.scene.control.Label;

public class WarehouseStatusBadge extends Label {
    private String statusCode;

    public WarehouseStatusBadge() {
        this("PENDING");
    }

    public WarehouseStatusBadge(String statusCode) {
        setStatusCode(statusCode);
    }

    public String getStatusCode() {
        return statusCode;
    }

    public final void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        apply(this, statusCode);
    }

    public static void apply(Label target, String statusCode) {
        target.setText(WarehouseInboundStatus.label(statusCode));
        target.getStyleClass().setAll("status-chip", WarehouseInboundStatus.cssClass(statusCode));
    }
}
