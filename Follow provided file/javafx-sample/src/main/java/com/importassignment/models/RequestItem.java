package com.importassignment.models;

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Model class for Request Item (Mặt hàng trong yêu cầu nhập)
 */
public class RequestItem {
    private final IntegerProperty id;
    private final ObjectProperty<Merchandise> merchandise;
    private final IntegerProperty quantity;
    private final ObjectProperty<LocalDate> deliveryDate;

    public RequestItem(int id) {
        this.id = new SimpleIntegerProperty(id);
        this.merchandise = new SimpleObjectProperty<>();
        this.quantity = new SimpleIntegerProperty(0);
        this.deliveryDate = new SimpleObjectProperty<>();
    }

    public RequestItem(int id, Merchandise merchandise, int quantity, LocalDate deliveryDate) {
        this.id = new SimpleIntegerProperty(id);
        this.merchandise = new SimpleObjectProperty<>(merchandise);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.deliveryDate = new SimpleObjectProperty<>(deliveryDate);
    }

    // Getters
    public int getId() { return id.get(); }
    public Merchandise getMerchandise() { return merchandise.get(); }
    public int getQuantity() { return quantity.get(); }
    public LocalDate getDeliveryDate() { return deliveryDate.get(); }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public ObjectProperty<Merchandise> merchandiseProperty() { return merchandise; }
    public IntegerProperty quantityProperty() { return quantity; }
    public ObjectProperty<LocalDate> deliveryDateProperty() { return deliveryDate; }

    // Setters
    public void setMerchandise(Merchandise merchandise) { this.merchandise.set(merchandise); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }
    public void setDeliveryDate(LocalDate date) { this.deliveryDate.set(date); }

    // Helper methods
    public boolean isValid() {
        return merchandise.get() != null &&
               quantity.get() > 0 &&
               deliveryDate.get() != null;
    }

    public String getUnit() {
        return merchandise.get() != null ? merchandise.get().getUnit() : "";
    }

    public String getMerchandiseCode() {
        return merchandise.get() != null ? merchandise.get().getCode() : "";
    }

    public String getMerchandiseName() {
        return merchandise.get() != null ? merchandise.get().getName() : "";
    }
}
