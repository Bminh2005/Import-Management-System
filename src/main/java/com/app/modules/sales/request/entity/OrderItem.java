package com.app.modules.sales.request.entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Một dòng mặt hàng trong popup "Chi tiết Đơn hàng" (đặt hàng quốc tế).
 * Chỉ hiển thị thông tin định danh + số lượng — không có đơn giá / thành tiền.
 */
public class OrderItem {

    private final StringProperty code;
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final StringProperty unit;

    public OrderItem(String code, String name, int quantity, String unit) {
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
    }

    public String getCode() { return code.get(); }
    public String getName() { return name.get(); }
    public int getQuantity() { return quantity.get(); }
    public String getUnit() { return unit.get(); }

    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty quantityProperty() { return quantity; }
    public StringProperty unitProperty() { return unit; }
}
