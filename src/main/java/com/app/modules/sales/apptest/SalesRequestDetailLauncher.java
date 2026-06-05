package com.app.modules.sales.apptest;

import javafx.application.Application;

/**
 * Launcher cho {@link SalesRequestDetailApp} — test UC xem chi tiết yêu cầu nhập hàng.
 *
 * <p>Mở danh sách yêu cầu; bấm icon mắt để vào chi tiết (UC xem chi tiết).</p>
 */
public class SalesRequestDetailLauncher {
    public static void main(String[] args) {
        Application.launch(SalesRequestDetailApp.class, args);
    }
}
