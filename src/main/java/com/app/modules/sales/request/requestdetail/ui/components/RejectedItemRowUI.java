package com.app.modules.sales.request.requestdetail.ui.components;

import com.app.modules.sales.request.entity.RejectedItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Dựng một dòng mặt hàng bị từ chối/hủy trên màn chi tiết yêu cầu. */
public final class RejectedItemRowUI {

    private RejectedItemRowUI() {
    }

    public static VBox build(RejectedItem item) {
        Label codeChip = new Label(item.getCode());
        codeChip.getStyleClass().add("code-chip");

        Label name = new Label(item.getName());
        name.getStyleClass().add("rejected-name");

        HBox header = new HBox(10, name, codeChip);
        header.setAlignment(Pos.CENTER_LEFT);

        Label qty = new Label("Số lượng: " + item.getQuantity() + " " + item.getUnit());
        qty.getStyleClass().add("rejected-sub");

        boolean isOverseas = "overseas".equals(item.getRejectedBy());
        Label rejectedByBadge = new Label(
                isOverseas ? "Từ chối bởi Đặt hàng Quốc tế" : "Hủy bởi người dùng");
        rejectedByBadge.getStyleClass().add(
                isOverseas ? "rejected-badge-overseas" : "rejected-badge-user");

        Label dateLabel = new Label(item.getRejectedDate());
        dateLabel.getStyleClass().add("rejected-sub");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox rightBox = new VBox(4, rejectedByBadge, dateLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        HBox topRow = new HBox(12, header, spacer, rightBox);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label reasonTitle = new Label("Lý do:");
        reasonTitle.getStyleClass().add("rejected-reason-title");
        Label reason = new Label(item.getReason());
        reason.getStyleClass().add("rejected-sub");
        reason.setWrapText(true);

        VBox row = new VBox(8, topRow, qty, reasonTitle, reason);
        row.getStyleClass().add("rejected-row");
        row.setPadding(new Insets(14, 16, 14, 16));
        return row;
    }
}
