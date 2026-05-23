package com.app.modules.sales.request.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Một mặt hàng trong yêu cầu nhập hàng.
 * Dùng property để bind trực tiếp vào TableView của tầng ui.
 */
public class RequestItem {

    private final StringProperty code;
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final StringProperty unit;
    private final StringProperty deliveryDate;
    private final StringProperty status;

    public RequestItem(String code, String name, int quantity, String unit,
                       String deliveryDate, String status) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.deliveryDate = new SimpleStringProperty(deliveryDate);
        this.status = new SimpleStringProperty(status);
    }

    // ----- Getters -----
    public String getCode() { return code.get(); }
    public String getName() { return name.get(); }
    public int getQuantity() { return quantity.get(); }
    public String getUnit() { return unit.get(); }
    public String getDeliveryDate() { return deliveryDate.get(); }
    public String getStatus() { return status.get(); }

    // ----- Setters -----
    public void setCode(String value) { code.set(value); }
    public void setName(String value) { name.set(value); }
    public void setQuantity(int value) { quantity.set(value); }
    public void setUnit(String value) { unit.set(value); }
    public void setDeliveryDate(String value) { deliveryDate.set(value); }
    public void setStatus(String value) { status.set(value); }

    // ----- Property accessors (cho TableView) -----
    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty unitProperty() { return unit; }
    public StringProperty deliveryDateProperty() { return deliveryDate; }
    public StringProperty statusProperty() { return status; }
}
