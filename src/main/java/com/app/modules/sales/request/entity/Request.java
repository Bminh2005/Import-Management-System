package com.app.modules.sales.request.entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Yêu cầu nhập hàng (đại diện cho 1 record trong bảng request).
 * Dùng làm entity ở tầng repository và truyền lên service/UI qua DTO.
 */
public class Request {

    private String code;
    private String createdDate;
    private String status;
    private String createdBy;
    private String assignedTo;

    private final ObservableList<RequestItem> items = FXCollections.observableArrayList();
    private final ObservableList<RejectedItem> rejectedItems = FXCollections.observableArrayList();
    private final ObservableList<RelatedOrder> orders = FXCollections.observableArrayList();

    public Request() {
    }

    public Request(String code, String createdDate, String status,
                   String createdBy, String assignedTo) {
        this.code = code;
        this.createdDate = createdDate;
        this.status = status;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
    }

    public int getItemCount() {
        return items.size();
    }

    // ----- Getters / Setters -----
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public ObservableList<RequestItem> getItems() { return items; }
    public ObservableList<RejectedItem> getRejectedItems() { return rejectedItems; }
    public ObservableList<RelatedOrder> getOrders() { return orders; }
}
