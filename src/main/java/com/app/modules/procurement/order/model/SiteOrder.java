package com.app.modules.procurement.order.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SiteOrder {
    private long id;
    private LocalDateTime createdAt;
    private long siteId;
    private String siteName;
    private LocalDate expectedDeliveryDate;
    private long userId;
    private String ordererName;
    private long requestId;
    private OrderStatus status;
    private DeliveryType delivery;
    private final List<SiteOrderItem> items = new ArrayList<>();

    public SiteOrder() {
    }

    public SiteOrder(long siteId, long requestId, long userId,
                     LocalDate expectedDeliveryDate, OrderStatus status,
                     DeliveryType delivery) {
        this.siteId = siteId;
        this.requestId = requestId;
        this.userId = userId;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = status;
        this.delivery = delivery;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOrdererName() {
        return ordererName;
    }

    public void setOrdererName(String ordererName) {
        this.ordererName = ordererName;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DeliveryType getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryType delivery) {
        this.delivery = delivery;
    }

    public List<SiteOrderItem> getItems() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }
}
