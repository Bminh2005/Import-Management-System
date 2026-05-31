package com.app.modules.warehouse.inbound.dto;

public class InboundOrderResponse {
    private long orderId;
    private long siteId;
    private String orderCode;
    private String receivedDate;
    private String supplier;
    private String status;
    private String statusCode;
    private int expectedQuantity;
    private int actualQuantity;
    private String note;

    public InboundOrderResponse() {
    }

    public InboundOrderResponse(String orderCode, String receivedDate, String supplier, String status,
                                int expectedQuantity, int actualQuantity, String note) {
        this.orderCode = orderCode;
        this.receivedDate = receivedDate;
        this.supplier = supplier;
        this.status = status;
        this.statusCode = status;
        this.expectedQuantity = expectedQuantity;
        this.actualQuantity = actualQuantity;
        this.note = note;
    }

    public InboundOrderResponse(long orderId, long siteId, String orderCode, String receivedDate, String supplier,
                                String status, String statusCode, int expectedQuantity, int actualQuantity,
                                String note) {
        this.orderId = orderId;
        this.siteId = siteId;
        this.orderCode = orderCode;
        this.receivedDate = receivedDate;
        this.supplier = supplier;
        this.status = status;
        this.statusCode = statusCode;
        this.expectedQuantity = expectedQuantity;
        this.actualQuantity = actualQuantity;
        this.note = note;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public int getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(int expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public int getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(int actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
