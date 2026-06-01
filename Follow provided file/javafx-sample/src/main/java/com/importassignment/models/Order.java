package com.importassignment.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Order Model - Mô hình Đơn hàng
 * Supports both legacy OrdersList view and new OrderDetails view
 */
public class Order {
    // Legacy properties (for OrdersList, Dashboard)
    private final StringProperty orderId;
    private final StringProperty date;
    private final StringProperty status;
    private final StringProperty site;
    private final IntegerProperty totalItems;
    private final StringProperty priority;
    private final IntegerProperty completion;

    // New properties (for OrderDetails from ImportRequest)
    private final StringProperty importRequestCode;
    private final ObjectProperty<LocalDate> orderDate;
    private final ObservableList<OrderItem> items;
    private final DoubleProperty totalValue;

    // Legacy constructor (used by OrdersListController, DashboardController)
    public Order(String orderId, String date, String status, String site, int totalItems) {
        this.orderId = new SimpleStringProperty(orderId);
        this.date = new SimpleStringProperty(date);
        this.status = new SimpleStringProperty(status);
        this.site = new SimpleStringProperty(site);
        this.totalItems = new SimpleIntegerProperty(totalItems);
        this.priority = new SimpleStringProperty("Medium");
        this.completion = new SimpleIntegerProperty(0);

        // Initialize new properties
        this.importRequestCode = new SimpleStringProperty("");
        this.orderDate = new SimpleObjectProperty<>(LocalDate.now());
        this.items = FXCollections.observableArrayList();
        this.totalValue = new SimpleDoubleProperty(0.0);
    }

    // New constructor (used by ViewImportRequestDialog)
    public Order(String code, String importRequestCode, LocalDate orderDate, String status, String assignedSite) {
        this.orderId = new SimpleStringProperty(code);
        this.importRequestCode = new SimpleStringProperty(importRequestCode);
        this.orderDate = new SimpleObjectProperty<>(orderDate);
        this.status = new SimpleStringProperty(status);
        this.site = new SimpleStringProperty(assignedSite);
        this.items = FXCollections.observableArrayList();
        this.totalValue = new SimpleDoubleProperty(0.0);

        // Initialize legacy properties
        this.date = new SimpleStringProperty(orderDate.toString());
        this.totalItems = new SimpleIntegerProperty(0);
        this.priority = new SimpleStringProperty("Medium");
        this.completion = new SimpleIntegerProperty(0);
    }

    // Legacy getters
    public String getOrderId() { return orderId.get(); }
    public String getDate() { return date.get(); }
    public String getStatus() { return status.get(); }
    public String getSite() { return site.get(); }
    public int getTotalItems() { return totalItems.get(); }
    public String getPriority() { return priority.get(); }
    public int getCompletion() { return completion.get(); }

    // Legacy property getters
    public StringProperty orderIdProperty() { return orderId; }
    public StringProperty dateProperty() { return date; }
    public StringProperty statusProperty() { return status; }
    public StringProperty siteProperty() { return site; }
    public IntegerProperty totalItemsProperty() { return totalItems; }
    public StringProperty priorityProperty() { return priority; }
    public IntegerProperty completionProperty() { return completion; }

    // Legacy setters
    public void setOrderId(String orderId) { this.orderId.set(orderId); }
    public void setDate(String date) { this.date.set(date); }
    public void setStatus(String status) { this.status.set(status); }
    public void setSite(String site) { this.site.set(site); }
    public void setTotalItems(int totalItems) { this.totalItems.set(totalItems); }
    public void setPriority(String priority) { this.priority.set(priority); }
    public void setCompletion(int completion) { this.completion.set(completion); }

    // New getters (for OrderDetails functionality)
    public String getCode() { return orderId.get(); }
    public String getImportRequestCode() { return importRequestCode.get(); }
    public LocalDate getOrderDate() { return orderDate.get(); }
    public String getAssignedSite() { return site.get(); }
    public ObservableList<OrderItem> getItems() { return items; }
    public double getTotalValue() { return totalValue.get(); }

    // New property getters
    public StringProperty codeProperty() { return orderId; }
    public StringProperty importRequestCodeProperty() { return importRequestCode; }
    public ObjectProperty<LocalDate> orderDateProperty() { return orderDate; }
    public StringProperty assignedSiteProperty() { return site; }
    public DoubleProperty totalValueProperty() { return totalValue; }

    // New setters
    public void setCode(String value) { orderId.set(value); }
    public void setImportRequestCode(String value) { importRequestCode.set(value); }
    public void setOrderDate(LocalDate value) {
        orderDate.set(value);
        date.set(value.toString());
    }
    public void setAssignedSite(String value) { site.set(value); }
    public void setTotalValue(double value) { totalValue.set(value); }

    // Methods
    public void addItem(OrderItem item) {
        items.add(item);
        calculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        calculateTotal();
    }

    public int getItemCount() {
        return items.size();
    }

    private void calculateTotal() {
        double total = items.stream()
            .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
            .sum();
        setTotalValue(total);
        setTotalItems(items.size());
    }

    public boolean isPending() {
        return "pending".equals(status.get());
    }

    public boolean isProcessing() {
        return "processing".equals(status.get());
    }

    public boolean isCompleted() {
        return "completed".equals(status.get());
    }
}
