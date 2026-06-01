package com.importassignment.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model class for Import Request (Yêu cầu Nhập hàng)
 */
public class ImportRequest {
    private final IntegerProperty id;
    private final StringProperty code;
    private final StringProperty createdDate;
    private final IntegerProperty itemCount;
    private final StringProperty status;
    private final ObservableList<RequestItem> items;

    public ImportRequest(int id, String code, String createdDate, int itemCount, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.code = new SimpleStringProperty(code);
        this.createdDate = new SimpleStringProperty(createdDate);
        this.itemCount = new SimpleIntegerProperty(itemCount);
        this.status = new SimpleStringProperty(status);
        this.items = FXCollections.observableArrayList();
    }

    // Getters
    public int getId() { return id.get(); }
    public String getCode() { return code.get(); }
    public String getCreatedDate() { return createdDate.get(); }
    public int getItemCount() { return itemCount.get(); }
    public String getStatus() { return status.get(); }
    public ObservableList<RequestItem> getItems() { return items; }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty codeProperty() { return code; }
    public StringProperty createdDateProperty() { return createdDate; }
    public IntegerProperty itemCountProperty() { return itemCount; }
    public StringProperty statusProperty() { return status; }

    // Setters
    public void setStatus(String status) { this.status.set(status); }
    public void setItemCount(int count) { this.itemCount.set(count); }

    // Helper methods
    public void addItem(RequestItem item) {
        items.add(item);
        updateItemCount();
    }

    public void removeItem(RequestItem item) {
        items.remove(item);
        updateItemCount();
    }

    private void updateItemCount() {
        setItemCount(items.size());
    }

    public boolean isDraft() {
        return "draft".equals(status.get());
    }

    public boolean isPending() {
        return "pending".equals(status.get());
    }
}
