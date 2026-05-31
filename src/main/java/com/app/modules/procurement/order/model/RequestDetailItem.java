package com.app.modules.procurement.order.model;

public class RequestDetailItem {
    private long requestDetailId;
    private long merchandiseDetailId;
    private String merchandiseName;
    private String unit;
    private long requiredQuantity;
    private double referencePrice;

    public RequestDetailItem() {
    }

    public RequestDetailItem(long requestDetailId, long merchandiseDetailId,
                             String merchandiseName, String unit,
                             long requiredQuantity, double referencePrice) {
        this.requestDetailId = requestDetailId;
        this.merchandiseDetailId = merchandiseDetailId;
        this.merchandiseName = merchandiseName;
        this.unit = unit;
        this.requiredQuantity = requiredQuantity;
        this.referencePrice = referencePrice;
    }

    public long getRequestDetailId() {
        return requestDetailId;
    }

    public void setRequestDetailId(long requestDetailId) {
        this.requestDetailId = requestDetailId;
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

    public long getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(long requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public double getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(double referencePrice) {
        this.referencePrice = referencePrice;
    }
}
