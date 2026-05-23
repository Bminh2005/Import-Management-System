package com.app.Ioms;

import javafx.application.Application;

/**
 * Launcher cho {@link ProcurementInventoryApp}.
 *
 * Tách lớp này ra khỏi Application để né lỗi class loader
 * khi chạy không qua module-path (Maven + IDE đa số dùng kiểu này).
 */
public class ProcurementInventoryLauncher {
    public static void main(String[] args) {
        Application.launch(ProcurementInventoryApp.class, args);
    }
}
