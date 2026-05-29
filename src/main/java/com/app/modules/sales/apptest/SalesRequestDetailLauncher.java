package com.app.modules.sales.apptest;

import javafx.application.Application;

/**
 * Launcher cho {@link SalesRequestDetailApp}.
 *
 * Tách lớp này ra khỏi Application để né lỗi class loader
 * khi chạy không qua module-path (Maven + IDE đa số dùng kiểu này).
 */
public class SalesRequestDetailLauncher {
    public static void main(String[] args) {
        Application.launch(SalesRequestDetailApp.class, args);
    }
}
