package com.app.common.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.Function;

/**
 * Util dùng chung cho mọi module: ánh xạ mã trạng thái sang nhãn tiếng Việt
 * + style badge, kèm tiện ích định dạng tiền tệ VND.
 *
 * Đặt ở common/util vì không phụ thuộc vào bất kỳ feature nào.
 */
public final class StatusStyle {

    private static final Locale VN = new Locale("vi", "VN");

    private StatusStyle() {
    }

    /** Nhãn tiếng Việt cho trạng thái yêu cầu / đơn hàng. */
    public static String requestStatusLabel(String status) {
        if (status == null) return "";
        switch (status) {
            case "draft":      return "Bản nháp";
            case "pending":    return "Chờ xử lý";
            case "processing": return "Đang xử lý";
            case "completed":  return "Hoàn thành";
            case "cancelled":  return "Đã hủy";
            default:           return status;
        }
    }

    /** Nhãn tiếng Việt cho trạng thái của từng mặt hàng trong yêu cầu. */
    public static String itemStatusLabel(String status) {
        if (status == null) return "";
        switch (status) {
            case "pending":  return "Chờ duyệt";
            case "approved": return "Đã duyệt";
            case "rejected": return "Từ chối";
            default:         return status;
        }
    }

    /** Style nền cho badge trạng thái (áp inline qua Node#setStyle). */
    public static String badgeStyle(String status) {
        String base = "-fx-padding: 4 12; -fx-background-radius: 12; "
                + "-fx-font-size: 12px; -fx-font-weight: bold;";
        if (status == null) return base;
        switch (status) {
            case "pending":
                return base + "-fx-background-color: #FEF3C7; -fx-text-fill: #92400E;";
            case "processing":
                return base + "-fx-background-color: #DBEAFE; -fx-text-fill: #1E40AF;";
            case "approved":
            case "completed":
                return base + "-fx-background-color: #D1FAE5; -fx-text-fill: #065F46;";
            case "rejected":
            case "cancelled":
                return base + "-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B;";
            case "draft":
            default:
                return base + "-fx-background-color: #F3F4F6; -fx-text-fill: #1F2937;";
        }
    }

    /** Định dạng số tiền theo chuẩn VND, ví dụ 75000000 -> "75.000.000 ₫". */
    public static String formatCurrency(long value) {
        return NumberFormat.getCurrencyInstance(VN).format(value);
    }

    /**
     * Cell factory dùng lại cho mọi cột "Trạng thái": hiển thị badge có màu.
     * Truyền vào hàm chuyển mã trạng thái sang nhãn (request hoặc item).
     */
    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>>
            badgeCellFactory(Function<String, String> labeler) {
        return column -> new TableCell<S, String>() {
            private final Label badge = new Label();

            {
                setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    badge.setText(labeler.apply(status));
                    badge.setStyle(badgeStyle(status));
                    setGraphic(badge);
                }
            }
        };
    }
}
