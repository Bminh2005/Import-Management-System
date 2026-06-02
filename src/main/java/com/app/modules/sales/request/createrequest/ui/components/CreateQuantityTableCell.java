package com.app.modules.sales.request.createrequest.ui.components;
import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

public class CreateQuantityTableCell extends TableCell<CreateImportItemModel, Integer> {
    private final TextField txtQuantity = new TextField();

    public CreateQuantityTableCell() {
        txtQuantity.getStyleClass().add("table-text-field");

        // Ràng buộc chỉ cho phép nhập số (Regex)
        txtQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txtQuantity.setText(newVal.replaceAll("[^\\d]", ""));
                return;
            }

            // Đồng bộ dữ liệu vào Model khi người dùng gõ
            if (getTableView() != null && getIndex() < getTableView().getItems().size()) {
                CreateImportItemModel model = getTableView().getItems().get(getIndex());
                try {
                    model.setQuantity(newVal.isEmpty() ? 0 : Integer.parseInt(newVal));
                } catch (NumberFormatException e) {
                    model.setQuantity(0);
                }
            }
        });
    }

    @Override
    protected void updateItem(Integer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            CreateImportItemModel currentModel = getTableView().getItems().get(getIndex());
            txtQuantity.setText(String.valueOf(currentModel.getQuantity()));
            setGraphic(txtQuantity);
        }
    }
}