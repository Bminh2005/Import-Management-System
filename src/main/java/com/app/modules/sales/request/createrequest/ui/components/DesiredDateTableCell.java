package com.app.modules.sales.request.createrequest.ui.components;

import com.app.modules.sales.request.createrequest.ui.model.CreateImportItemModel;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import java.time.LocalDate;

public class DesiredDateTableCell extends TableCell<CreateImportItemModel, LocalDate> {
    private final DatePicker datePicker = new DatePicker();

    public DesiredDateTableCell() {
        datePicker.setPromptText("dd/mm/yyyy");
        datePicker.getStyleClass().add("table-date-picker");

        // Đồng bộ dữ liệu khi người dùng chọn ngày mới
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (getTableView() != null && getIndex() < getTableView().getItems().size()) {
                CreateImportItemModel model = getTableView().getItems().get(getIndex());
                model.setExpectedDate(newDate);
            }
        });
    }

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            CreateImportItemModel currentModel = getTableView().getItems().get(getIndex());
            datePicker.setValue(currentModel.getExpectedDate());
            setGraphic(datePicker);
        }
    }
}