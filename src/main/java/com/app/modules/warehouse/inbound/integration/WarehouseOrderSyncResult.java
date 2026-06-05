package com.app.modules.warehouse.inbound.integration;

public record WarehouseOrderSyncResult(boolean success, boolean skipped, int statusCode, String message) {
    public static WarehouseOrderSyncResult success(int statusCode, String message) {
        return new WarehouseOrderSyncResult(true, false, statusCode, message);
    }

    public static WarehouseOrderSyncResult failure(int statusCode, String message) {
        return new WarehouseOrderSyncResult(false, false, statusCode, message);
    }

    public static WarehouseOrderSyncResult skipped(String message) {
        return new WarehouseOrderSyncResult(false, true, 0, message);
    }
}
