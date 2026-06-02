package com.app.modules.sales.apptest;

import javafx.application.Application;

/**
 * Launcher cho {@link salesApp}.
 *
 * Tách lớp này ra khỏi Application để né lỗi
 * "JavaFX runtime components are missing" khi IntelliJ chạy
 * bằng -classpath (không phải --module-path).
 *
 * Cách chạy: right-click file này → Run 'salesAppLauncher.main()'.
 */
public class salesAppLauncher {
    public static void main(String[] args) {
        Application.launch(salesApp.class, args);
    }
}
