package com.app.modules.sales.request.createrequest.ui.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class CreateImportItemModel {
    private final StringProperty itemCode;     // mã hàng
    private final StringProperty itemName;     // tên hàng
    private final IntegerProperty quantity;    // số lượng
    private final StringProperty unit;         // đơn vị
    private final ObjectProperty<LocalDate> expectedDate; // ngày nhận mong muốn
    private final BooleanProperty selected;

    public CreateImportItemModel(String itemCode, String itemName, int quantity, String unit, LocalDate expectedDate) {
        this.itemCode = new SimpleStringProperty(itemCode);
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.expectedDate = new SimpleObjectProperty<>(expectedDate);
        this.selected = new SimpleBooleanProperty(false);
    }

    // --- itemCode ---
    public String getItemCode() { return itemCode.get(); }
    public void setItemCode(String value) { itemCode.set(value); }
    public StringProperty itemCodeProperty() { return itemCode; }

    // --- itemName ---
    public String getItemName() { return itemName.get(); }
    public void setItemName(String value) { itemName.set(value); }
    public StringProperty itemNameProperty() { return itemName; }

    // --- quantity ---
    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int value) { quantity.set(value); }
    public IntegerProperty quantityProperty() { return quantity; }

    // --- unit ---
    public String getUnit() { return unit.get(); }
    public void setUnit(String value) { unit.set(value); }
    public StringProperty unitProperty() { return unit; }

    // --- expectedDate ---
    public LocalDate getExpectedDate() { return expectedDate.get(); }
    public void setExpectedDate(LocalDate value) { expectedDate.set(value); }
    public ObjectProperty<LocalDate> expectedDateProperty() { return expectedDate; }

    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean value) { selected.set(value); }
    public BooleanProperty selectedProperty() { return selected; }
}
