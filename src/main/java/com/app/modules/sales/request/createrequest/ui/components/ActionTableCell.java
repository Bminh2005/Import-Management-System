package com.app.modules.sales.request.createrequest.ui.components;

import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.shape.SVGPath;

public class ActionTableCell extends TableCell<CreateImportItemModel, Void> {
    private final Button btnDelete = new Button();

    public ActionTableCell() {
        btnDelete.getStyleClass().add("btn-delete-row");

        // Vẽ icon thùng rác bằng mã SVG phẳng
        SVGPath trashIcon = new SVGPath();
        trashIcon.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        trashIcon.setFill(javafx.scene.paint.Color.web("#ef4444")); // Màu đỏ rực của UI hiện đại
        btnDelete.setGraphic(trashIcon);

        // Xử lý sự kiện click để xóa hàng
        btnDelete.setOnAction(event -> {
            // 1. Kiểm tra xem ô này đã được gắn vào một Hàng (Row) cụ thể chưa
            if (getTableRow() != null) {
                // 2. Lấy trực tiếp đối tượng Model nằm ở hàng đó (An toàn tuyệt đối)
                CreateImportItemModel model = (CreateImportItemModel) getTableRow().getItem();

                // 3. Tiến hành xóa khỏi TableView nếu đối tượng tồn tại
                if (model != null && getTableView() != null) {
                    getTableView().getItems().remove(model);
                }
            }
        });
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(btnDelete);
            setStyle("-fx-alignment: CENTER;"); // Căn nút nằm chính giữa ô
        }
    }
}