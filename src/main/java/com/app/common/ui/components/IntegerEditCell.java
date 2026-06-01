package com.app.common.ui.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Một TableCell dùng chung để chỉnh sửa trực tiếp giá trị số nguyên trong bảng.
 * Hoàn toàn generic, tuân thủ SOLID (SRP, OCP) và tránh sự phụ thuộc vào Model cụ thể.
 *
 * @param <S> Kiểu dữ liệu của dòng trong TableView.
 */
public class IntegerEditCell<S> extends TableCell<S, S> {
    private final TextField field = new TextField();
    private final Function<S, Integer> getter;
    private final BiConsumer<S, Integer> setter;
    private final boolean editable;

    public IntegerEditCell(Function<S, Integer> getter, BiConsumer<S, Integer> setter, boolean editable) {
        this.getter = getter;
        this.setter = setter;
        this.editable = editable;
        field.getStyleClass().add("edit-cell-field");
        field.setPrefWidth(80);
        field.focusedProperty().addListener((obs, oldF, newF) -> {
            if (!newF) commitChange();
        });
        field.setOnAction(e -> commitChange());
    }

    private void commitChange() {
        S row = getItem();
        if (row == null) return;
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0) value = 0;
            Integer oldVal = getter.apply(row);
            if (oldVal != null && oldVal == value) return;
            setter.accept(row, value);
        } catch (NumberFormatException e) {
            if (getItem() != null) {
                field.setText(String.valueOf(getter.apply(row)));
            }
        }
    }

    @Override
    protected void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            field.setText(String.valueOf(getter.apply(item)));
            field.setDisable(!editable);
            setGraphic(field);
        }
    }
}
