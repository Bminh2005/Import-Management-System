package com.app.modules.sales.request.createrequest.ui.model;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

import java.time.LocalDate;

public class CreateImportItemModel {
    private long merchandiseDetailId;
    private final StringProperty itemCode;     // mã hàng
    private final StringProperty itemName;     // tên hàng
    private final IntegerProperty quantity;    // số lượng
    private final StringProperty unit;         // đơn vị
    private final ObjectProperty<LocalDate> expectedDate; // ngày nhận mong muốn
    private final BooleanProperty selected;
    private final DoubleProperty referencePrice;

    public CreateImportItemModel(long merchandiseDetailId,String itemCode, String itemName, int quantity, String unit, double referencePrice,LocalDate expectedDate) {
        this.itemCode = new SimpleStringProperty(itemCode);
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.expectedDate = new SimpleObjectProperty<>(expectedDate);
        this.referencePrice = new SimpleDoubleProperty(referencePrice != 0.0? referencePrice:0.0);
        this.selected = new SimpleBooleanProperty(false);
        this.merchandiseDetailId = merchandiseDetailId;
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
    // --- referencePrice ---
    public DoubleProperty referencePriceProperty() { return referencePrice; }
    public double getReferencePrice() { return referencePrice.get(); }
    public void setReferencePrice(double value) { referencePrice.set(value); }
    // --- expectedDate ---
    public LocalDate getExpectedDate() { return expectedDate.get(); }
    public void setExpectedDate(LocalDate value) { expectedDate.set(value); }
    public ObjectProperty<LocalDate> expectedDateProperty() { return expectedDate; }

    public boolean isSelected() { return selected.get(); }
    public void setSelected(boolean value) { selected.set(value); }
    public BooleanProperty selectedProperty() { return selected; }

    public long getMerchandiseDetailId() { return merchandiseDetailId; }
    public void setMerchandiseDetailId(long id){
        this.merchandiseDetailId = id;
    }
}
