package com.app.modules.warehouse.inbound.ui;

public final class WarehouseInboundStatus {
    private WarehouseInboundStatus() {
    }

    public static String label(String statusCode) {
        if ("PROCESSING".equals(statusCode)) {
            return "Đang xử lý";
        }
        if ("IMPORTED".equals(statusCode)) {
            return "Đã nhập kho";
        }
        if ("MISMATCH".equals(statusCode)) {
            return "Có sai lệch";
        }
        return "Chờ xử lý";
    }

    public static String cssClass(String statusCode) {
        if ("PROCESSING".equals(statusCode)) {
            return "status-processing";
        }
        if ("IMPORTED".equals(statusCode)) {
            return "status-imported";
        }
        if ("MISMATCH".equals(statusCode)) {
            return "status-mismatch";
        }
        return "status-pending";
    }
}
