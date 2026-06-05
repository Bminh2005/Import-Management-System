package com.app.common.ui.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TextEditCell<S> extends TableCell<S, S> {
    private final TextField field = new TextField();
    private final Function<S, String> getter;
    private final BiConsumer<S, String> setter;
    private final Predicate<S> editablePredicate;

    public TextEditCell(Function<S, String> getter, BiConsumer<S, String> setter, Predicate<S> editablePredicate) {
        this.getter = getter;
        this.setter = setter;
        this.editablePredicate = editablePredicate;
        field.getStyleClass().add("edit-cell-field");
        field.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) {
                commitChange();
            }
        });
        field.setOnAction(event -> commitChange());
    }

    private void commitChange() {
        S row = getItem();
        if (row != null && isRowEditable(row)) {
            setter.accept(row, field.getText() == null ? "" : field.getText().trim());
        }
    }

    @Override
    protected void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
            return;
        }
        boolean editable = isRowEditable(item);
        field.setText(editable ? getter.apply(item) : "");
        field.setPromptText(editable ? "Nhập lý do sai lệch..." : "Không có sai lệch");
        field.setDisable(!editable);
        setGraphic(field);
        setText(null);
    }

    private boolean isRowEditable(S row) {
        return editablePredicate == null || editablePredicate.test(row);
    }
}
