package com.app.modules.sales.request.requestlist.ui.common;

/** Hằng số giao diện dùng chung cho màn danh sách yêu cầu. */
public final class RequestListUiConstants {

    public static final String ACTION_ICON_COLOR = "#475569";

    private RequestListUiConstants() {
    }

    public static String eyePath() {
        return "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5"
                + "C21.27 7.61 17 4.5 12 4.5zM12 17a5 5 0 1 1 0-10 5 5 0 0 1 0 10zm0-8a3 3 0 1 0 0 6 3 3 0 0 0 0-6z";
    }

    public static String editPath() {
        return "M4 4h6v2H6v10h10v-4h2v6H4V4zm12.5-1.5l1.5 1.5-9 9-2.5.5.5-2.5 9-9 1.5 1.5 2.12-2.12z";
    }
}
