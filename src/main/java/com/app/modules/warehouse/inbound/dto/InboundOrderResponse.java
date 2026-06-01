package com.app.modules.warehouse.inbound.dto;

public class InboundOrderResponse {
    private long orderId;
    private long siteId;
    private String orderCode;
    private String requestCode;
    private String receivedDate;
    private String expectedDate;
    private String supplier;
    private String status;
    private String statusCode;
    private int itemCount;
    private int expectedQuantity;
    private int actualQuantity;
    private String note;

    public InboundOrderResponse() {
    }

    public InboundOrderResponse(String orderCode, String receivedDate, String supplier, String status,
                                int expectedQuantity, int actualQuantity, String note) {
        this.orderCode = orderCode;
        this.requestCode = "";
        this.receivedDate = receivedDate;
        this.expectedDate = receivedDate;
        this.supplier = supplier;
        this.status = status;
        this.statusCode = status;
        this.itemCount = 0;
        this.expectedQuantity = expectedQuantity;
        this.actualQuantity = actualQuantity;
        this.note = note;
    }

    public InboundOrderResponse(long orderId, long siteId, String orderCode, String receivedDate, String supplier,
                                String status, String statusCode, int expectedQuantity, int actualQuantity,
                                String note) {
        this(orderId, siteId, orderCode, "", receivedDate, receivedDate, supplier, status, statusCode,
                0, expectedQuantity, actualQuantity, note);
    }

    public InboundOrderResponse(long orderId, long siteId, String orderCode, String requestCode,
                                String receivedDate, String expectedDate, String supplier,
                                String status, String statusCode, int itemCount,
                                int expectedQuantity, int actualQuantity, String note) {
        this.orderId = orderId;
        this.siteId = siteId;
        this.orderCode = orderCode;
        this.requestCode = requestCode;
        this.receivedDate = receivedDate;
        this.expectedDate = expectedDate;
        this.supplier = supplier;
        this.status = status;
        this.statusCode = statusCode;
        this.itemCount = itemCount;
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

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
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

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
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
