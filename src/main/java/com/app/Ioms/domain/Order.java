package com.app.Ioms.domain;
package com.app.Ioms.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final String id;
    private String customerName;
    private OrderStatus status;
    private String canceledBySite;
    private final LocalDateTime createdAt;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(String id, String customerName, OrderStatus status, String canceledBySite, LocalDateTime createdAt, List<OrderItem> items) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.customerName = customerName;
        this.status = status;
        this.canceledBySite = canceledBySite;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        if (items != null) this.items.addAll(items);
    }

    public String getId() { return id; }
    public String getCustomerName() { return customerName; }
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
    public String getCanceledBySite() { return canceledBySite; }
    public void setCanceledBySite(String canceledBySite) { this.canceledBySite = canceledBySite; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return items; }
}
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final String id;
    private String customerName;
    private OrderStatus status;
    private String canceledBySite; // mã site hủy (nếu có)
    private final LocalDateTime createdAt;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(String id, String customerName, OrderStatus status, String canceledBySite, LocalDateTime createdAt, List<OrderItem> items) {
        this.id = id == null || id.isBlank() ? UUID.randomUUID().toString() : id;
        this.customerName = customerName;
        this.status = status;
        this.canceledBySite = canceledBySite;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        if (items != null) this.items.addAll(items);
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getCanceledBySite() {
        return canceledBySite;
    }

    public void setCanceledBySite(String canceledBySite) {
        this.canceledBySite = canceledBySite;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
