package com.app.modules.warehouse.inbound.ui;

import com.app.common.ui.components.IntegerEditCell;
import com.app.common.ui.components.TextEditCell;
import com.app.modules.warehouse.inbound.dto.InboundOrderItemResponse;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class InboundOrderProcessTableController {
    private final InboundOrderProcessUI view;
    private final Runnable onChanged;

    public InboundOrderProcessTableController(InboundOrderProcessUI view, Runnable onChanged) {
        this.view = view;
        this.onChanged = onChanged;
    }

    public void setupItemsTable() {
        view.getItemCodeColumn().setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProductCode()));
        view.getItemCodeColumn().setCellFactory(column -> new CodeCell());
        view.getItemNameColumn().setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProductName()));
        view.getOrderedQuantityColumn().setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getOrderedQuantity()).asObject());

        view.getActualQuantityColumn().setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue()));
        view.getActualQuantityColumn().setCellFactory(column -> new IntegerEditCell<>(
                InboundOrderItemResponse::getActualQuantity,
                InboundOrderItemResponse::setActualQuantity,
                onChanged
        ));

        view.getDifferenceColumn().setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue()));
        view.getDifferenceColumn().setCellFactory(column -> new DifferenceCell());

        view.getReasonColumn().setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue()));
        view.getReasonColumn().setCellFactory(column -> new TextEditCell<>(
                item -> item.getDiscrepancyReason() == null ? "" : item.getDiscrepancyReason(),
                InboundOrderItemResponse::setDiscrepancyReason,
                InboundOrderItemResponse::hasMismatch
        ));
    }

    private static class CodeCell extends TableCell<InboundOrderItemResponse, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            getStyleClass().remove("code-cell");
            if (empty || item == null) {
                setText(null);
                return;
            }
            setText(item);
            getStyleClass().add("code-cell");
        }
    }

    private static class DifferenceCell extends TableCell<InboundOrderItemResponse, InboundOrderItemResponse> {
        private final Label chip = new Label();

        @Override
        protected void updateItem(InboundOrderItemResponse item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                return;
            }
            int difference = item.getActualQuantity() - item.getOrderedQuantity();
            chip.getStyleClass().setAll("difference-chip", cssClass(difference));
            chip.setText(label(difference));
            setAlignment(Pos.CENTER_LEFT);
            setGraphic(chip);
            setText(null);
        }

        private String label(int difference) {
            if (difference == 0) {
                return "Khớp";
            }
            if (difference > 0) {
                return "+" + difference;
            }
            return String.valueOf(difference);
        }

        private String cssClass(int difference) {
            if (difference == 0) {
                return "difference-ok";
            }
            if (difference > 0) {
                return "difference-extra";
            }
            return "difference-missing";
        }
    }
}
