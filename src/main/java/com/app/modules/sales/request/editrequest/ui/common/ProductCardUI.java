package com.app.modules.sales.request.editrequest.ui.common;

import com.app.modules.sales.request.entity.RequestItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Thành phần giao diện hiển thị thông tin thẻ sản phẩm trong popup chọn sản phẩm.
 */
public class ProductCardUI extends HBox {

    public ProductCardUI(RequestItem product, Runnable onPicked) {
        super(12); // Spacing giữa các thành phần con là 12px
        
        Label name = new Label(product.getName());
        name.getStyleClass().add("product-name");

        Label meta = new Label("Mã: " + product.getCode() + "  -  ĐVT: " + product.getUnit());
        meta.getStyleClass().add("product-meta");

        VBox info = new VBox(4, name, meta);
        VBox.setVgrow(info, Priority.ALWAYS);

        Button pick = new Button("Chọn");
        pick.getStyleClass().add("pick-button");
        pick.setOnAction(e -> {
            if (onPicked != null) {
                onPicked.run();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(info, spacer, pick);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(14, 18, 14, 18));
        this.getStyleClass().add("product-card");
    }
}
