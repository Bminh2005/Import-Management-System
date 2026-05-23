package com.app.Ioms;

import javafx.application.Application;

/**
 * Launcher cho {@link SalesEditRequestApp}.
 *
 * Tách lớp này ra khỏi Application để né lỗi class loader
 * khi chạy không qua module-path (Maven + IDE đa số dùng kiểu này).
 */
public class SalesEditRequestLauncher {
    public static void main(String[] args) {
        Application.launch(SalesEditRequestApp.class, args);
    }
}
