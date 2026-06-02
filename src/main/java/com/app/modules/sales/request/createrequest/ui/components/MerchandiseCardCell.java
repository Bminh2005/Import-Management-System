package com.app.modules.sales.request.createrequest.ui.components;

import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MerchandiseCardCell extends ListCell<CreateImportItemModel> {
    private final HBox root = new HBox();
    private final Label lblName = new Label();
    private final Label lblDetail = new Label();
    private final CheckBox checkBox = new CheckBox();

    public MerchandiseCardCell() {
        root.getStyleClass().add("merchandise-card");
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(12, 16, 12, 16));

        // Gom nhóm văn bản tiêu đề sản phẩm và mã hàng
        VBox textContainer = new VBox(4);
        lblName.getStyleClass().add("card-item-name");
        lblDetail.getStyleClass().add("card-item-detail");
        textContainer.getChildren().addAll(lblName, lblDetail);

        // Thiết lập cấu hình linh kiện hộp kiểm
        checkBox.getStyleClass().add("card-checkbox");
        checkBox.setMouseTransparent(true); // Tránh xung đột sự kiện click riêng lẻ của ô Checkbox

        HBox.setHgrow(textContainer, javafx.scene.layout.Priority.ALWAYS);
        root.getChildren().addAll(textContainer, checkBox);

        setGraphic(null);
        setStyle("-fx-background-color: transparent; -fx-padding: 0 0 12 0;");
    }

    @Override
    protected void updateItem(CreateImportItemModel item, boolean empty) {
        super.updateItem(item, empty);
        root.setOnMouseClicked(null);
        checkBox.selectedProperty().unbind();
        if (empty || item == null) {
            setGraphic(null);
//            root.setOnMouseClicked(null);
        } else {
            lblName.setText(item.getItemName());
            lblDetail.setText("Mã: " + item.getItemCode() + " - ĐVT: " + item.getUnit());
            checkBox.setDisable(false); // Đảm bảo không bị mờ
            checkBox.setMouseTransparent(true);

            checkBox.selectedProperty().bind(item.selectedProperty());
            // Đồng bộ trạng thái hai chiều giữa Model dữ liệu và nút bấm Checkbox trên UI
//            checkBox.selectedProperty().unbindBidirectional(item.selectedProperty());
//            checkBox.selectedProperty().bindBidirectional(item.selectedProperty());
//            checkBox.setSelected(item.isSelected());
            // Thiết lập hiệu ứng nhấp chọn trực quan trên toàn bộ vùng thẻ
            root.setOnMouseClicked(event -> item.setSelected(!item.isSelected()));

            setGraphic(root);
        }
    }
}