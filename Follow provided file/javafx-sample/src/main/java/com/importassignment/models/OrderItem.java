package com.importassignment.models;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * OrderItem model - represents a merchandise item in an order
 */
public class OrderItem {
    private final ObjectProperty<Merchandise> merchandise;
    private final IntegerProperty quantity;
    private final DoubleProperty unitPrice;
    private final ObjectProperty<LocalDate> deliveryDate;
    private final StringProperty status;

    public OrderItem(Merchandise merchandise, int quantity, double unitPrice, LocalDate deliveryDate) {
        this.merchandise = new SimpleObjectProperty<>(merchandise);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitPrice = new SimpleDoubleProperty(unitPrice);
        this.deliveryDate = new SimpleObjectProperty<>(deliveryDate);
        this.status = new SimpleStringProperty("pending");
    }

    // Property getters
    public ObjectProperty<Merchandise> merchandiseProperty() { return merchandise; }
    public IntegerProperty quantityProperty() { return quantity; }
    public DoubleProperty unitPriceProperty() { return unitPrice; }
    public ObjectProperty<LocalDate> deliveryDateProperty() { return deliveryDate; }
    public StringProperty statusProperty() { return status; }

    // Standard getters
    public Merchandise getMerchandise() { return merchandise.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getUnitPrice() { return unitPrice.get(); }
    public LocalDate getDeliveryDate() { return deliveryDate.get(); }
    public String getStatus() { return status.get(); }

    // Setters
    public void setMerchandise(Merchandise value) { merchandise.set(value); }
    public void setQuantity(int value) { quantity.set(value); }
    public void setUnitPrice(double value) { unitPrice.set(value); }
    public void setDeliveryDate(LocalDate value) { deliveryDate.set(value); }
    public void setStatus(String value) { status.set(value); }

    // Computed values
    public double getTotalPrice() {
        return quantity.get() * unitPrice.get();
    }

    public String getMerchandiseCode() {
        return merchandise.get() != null ? merchandise.get().getCode() : "";
    }

    public String getMerchandiseName() {
        return merchandise.get() != null ? merchandise.get().getName() : "";
    }

    public String getUnit() {
        return merchandise.get() != null ? merchandise.get().getUnit() : "";
    }
}
