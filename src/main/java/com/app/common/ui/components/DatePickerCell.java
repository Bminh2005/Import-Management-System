package com.app.common.ui.components;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Một TableCell dùng chung hiển thị DatePicker để sửa trực tiếp ngày nhận dạng chuỗi ISO.
 * Hoàn toàn generic, tuân thủ SOLID (SRP, OCP) và tránh phụ thuộc Model cụ thể.
 *
 * @param <S> Kiểu dữ liệu của dòng trong TableView.
 */
public class DatePickerCell<S> extends TableCell<S, S> {
    private final DatePicker picker = new DatePicker();
    private final Function<S, String> getter;
    private final BiConsumer<S, String> setter;
    private final boolean editable;

    public DatePickerCell(Function<S, String> getter, BiConsumer<S, String> setter, boolean editable) {
        this.getter = getter;
        this.setter = setter;
        this.editable = editable;
        picker.getStyleClass().add("edit-cell-field");
        picker.setPrefWidth(140);
        picker.valueProperty().addListener((obs, oldV, newV) -> {
            S row = getItem();
            if (row == null || newV == null) return;
            String iso = newV.toString();
            String oldVal = getter.apply(row);
            if (iso.equals(oldVal)) return;
            setter.accept(row, iso);
        });
    }

    @Override
    protected void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            LocalDate value = parse(getter.apply(item));
            picker.setValue(value);
            picker.setDisable(!editable);
            setGraphic(picker);
        }
    }

    private LocalDate parse(String text) {
        if (text == null || text.isBlank()) return null;
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
