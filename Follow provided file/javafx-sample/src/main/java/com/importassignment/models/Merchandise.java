package com.importassignment.models;

import javafx.beans.property.*;

/**
 * Model class for Merchandise (Mặt hàng)
 */
public class Merchandise {
    private final IntegerProperty id;
    private final StringProperty code;
    private final StringProperty name;
    private final StringProperty unit;
    private final StringProperty category;
    private final StringProperty status;
    private final StringProperty price;
    private final StringProperty supplier;
    private final StringProperty description;

    public Merchandise(int id, String code, String name, String unit, String category,
                      String status, String price, String supplier, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.category = new SimpleStringProperty(category);
        this.status = new SimpleStringProperty(status);
        this.price = new SimpleStringProperty(price);
        this.supplier = new SimpleStringProperty(supplier);
        this.description = new SimpleStringProperty(description);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getCode() { return code.get(); }
    public String getName() { return name.get(); }
    public String getUnit() { return unit.get(); }
    public String getCategory() { return category.get(); }
    public String getStatus() { return status.get(); }
    public String getPrice() { return price.get(); }
    public String getSupplier() { return supplier.get(); }
    public String getDescription() { return description.get(); }

    // Property getters for TableView binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public StringProperty unitProperty() { return unit; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty statusProperty() { return status; }
    public StringProperty priceProperty() { return price; }
    public StringProperty supplierProperty() { return supplier; }
    public StringProperty descriptionProperty() { return description; }

    // Setters
    public void setCode(String code) { this.code.set(code); }
    public void setName(String name) { this.name.set(name); }
    public void setUnit(String unit) { this.unit.set(unit); }
    public void setCategory(String category) { this.category.set(category); }
    public void setStatus(String status) { this.status.set(status); }
    public void setPrice(String price) { this.price.set(price); }
    public void setSupplier(String supplier) { this.supplier.set(supplier); }
    public void setDescription(String description) { this.description.set(description); }

    public boolean isActive() {
        return "active".equals(status.get());
    }
}
