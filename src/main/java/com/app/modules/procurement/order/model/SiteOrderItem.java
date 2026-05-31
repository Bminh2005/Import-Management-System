package com.app.modules.procurement.order.model;

public class SiteOrderItem {
    private long id;
    private long orderId;
    private long merchandiseDetailId;
    private String merchandiseName;
    private String unit;
    private long quantity;
    private double price;
    private String refusedReason;
    private OrderStatus status;

    public SiteOrderItem() {
    }

    public SiteOrderItem(long orderId, long merchandiseDetailId, String merchandiseName,
                         String unit, long quantity, double price,
                         String refusedReason, OrderStatus status) {
        this.orderId = orderId;
        this.merchandiseDetailId = merchandiseDetailId;
        this.merchandiseName = merchandiseName;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
        this.refusedReason = refusedReason;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getMerchandiseDetailId() {
        return merchandiseDetailId;
    }

    public void setMerchandiseDetailId(long merchandiseDetailId) {
        this.merchandiseDetailId = merchandiseDetailId;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRefusedReason() {
        return refusedReason;
    }

    public void setRefusedReason(String refusedReason) {
        this.refusedReason = refusedReason;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
