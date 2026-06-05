package com.app.common.ui.components;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class IntegerEditCell<S> extends TableCell<S, S> {
    private final TextField field = new TextField();
    private final Function<S, Integer> getter;
    private final BiConsumer<S, Integer> setter;
    private final Runnable onChanged;

    public IntegerEditCell(Function<S, Integer> getter, BiConsumer<S, Integer> setter, Runnable onChanged) {
        this.getter = getter;
        this.setter = setter;
        this.onChanged = onChanged;
        field.getStyleClass().add("edit-cell-field");
        field.setPrefWidth(86);
        field.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if (!newFocus) {
                commitChange();
            }
        });
        field.setOnAction(event -> commitChange());
    }

    private void commitChange() {
        S row = getItem();
        if (row == null) {
            return;
        }
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0) {
                value = 0;
            }
            setter.accept(row, value);
            field.setText(String.valueOf(value));
            if (onChanged != null) {
                onChanged.run();
            }
            getTableView().refresh();
        } catch (NumberFormatException exception) {
            field.setText(String.valueOf(getter.apply(row)));
        }
    }

    @Override
    protected void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            return;
        }
        field.setText(String.valueOf(getter.apply(item)));
        setGraphic(field);
        setText(null);
    }
}
