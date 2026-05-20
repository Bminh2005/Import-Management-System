package com.app.Ioms;

import javafx.application.Application;

/**
 * Launcher cho {@link SalesRequestApp}.
 *
 * Tách lớp này ra khỏi Application để né lỗi class loader
 * khi chạy không qua module-path (Maven + IDE đa số dùng kiểu này).
 */
public class SalesRequestLauncher {
    public static void main(String[] args) {
        Application.launch(SalesRequestApp.class, args);
    }
}
