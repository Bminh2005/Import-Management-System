package com.app.modules.warehouse.inbound.dto;

public class InboundOrderItemResponse {
    private long orderDetailId;
    private long orderId;
    private long merchandiseDetailId;
    private String productCode;
    private String productName;
    private int orderedQuantity;
    private int actualQuantity;

    public InboundOrderItemResponse() {
    }

    public InboundOrderItemResponse(long orderDetailId, long orderId, long merchandiseDetailId,
                                    String productCode, String productName,
                                    int orderedQuantity, int actualQuantity) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.merchandiseDetailId = merchandiseDetailId;
        this.productCode = productCode;
        this.productName = productName;
        this.orderedQuantity = orderedQuantity;
        this.actualQuantity = actualQuantity;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
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

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public boolean hasMismatch() {
        return orderedQuantity != actualQuantity;
    }
}
