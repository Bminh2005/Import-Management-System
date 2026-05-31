package com.app.common.ui.components;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

public class ToastNotification {

    public static void show(Window ownerWindow, String message, boolean isSuccess) {
        // Khởi tạo Popup
        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        // Tạo Label chứa nội dung và thiết lập CSS trực tiếp
        Label label = new Label(message);

        // Đặt màu sắc tùy thuộc vào trạng thái thành công hay thất bại
        String backgroundColor = isSuccess ? "#dcfce7" : "#fee2e2";
        String textColor = isSuccess ? "#166534" : "#991b1b";

        label.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-text-fill: " + textColor + "; " +
                        "-fx-padding: 12px 24px; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );

        // Thêm hiệu ứng đổ bóng
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(2.0);
        dropShadow.setColor(Color.color(0, 0, 0, 0.15));
        label.setEffect(dropShadow);

        popup.getContent().add(label);

        // Xử lý vị trí hiển thị (Căn giữa ở phía trên cùng của cửa sổ)
        popup.setOnShown(e -> {
            double windowX = ownerWindow.getX();
            double windowWidth = ownerWindow.getWidth();
            double popupWidth = popup.getWidth();

            popup.setX(windowX + (windowWidth / 2) - (popupWidth / 2));
            popup.setY(ownerWindow.getY() + 50); // Cách mép trên 50px
        });

        // Tạo hiệu ứng Fade In (Xuất hiện mờ dần)
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), label);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // Thời gian hiển thị thông báo (3 giây)
        PauseTransition delay = new PauseTransition(Duration.seconds(3));

        // Tạo hiệu ứng Fade Out (Biến mất mờ dần)
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), label);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> popup.hide());

        // Nối các chuỗi hiệu ứng lại với nhau: Fade In -> Delay -> Fade Out
        fadeIn.setOnFinished(e -> delay.play());
        delay.setOnFinished(e -> fadeOut.play());

        // Bắt đầu hiển thị
        popup.show(ownerWindow);
        fadeIn.play();
    }

}
