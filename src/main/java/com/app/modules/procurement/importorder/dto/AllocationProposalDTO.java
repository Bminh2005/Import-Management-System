package com.app.modules.procurement.importorder.dto;

public class AllocationProposalDTO {
    private long siteId;
    private String siteName;
    private long merchandiseDetailId;
    private String merchandiseName;
    private String unit;
    private int allocatedQuantity;
    private String deliveryType; // SHIP or AIR
    private String expectedDeliveryDate;
    private double price;

    public AllocationProposalDTO(long siteId, String siteName, long merchandiseDetailId, String merchandiseName,
                                 String unit, int allocatedQuantity, String deliveryType, String expectedDeliveryDate, double price) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.merchandiseDetailId = merchandiseDetailId;
        this.merchandiseName = merchandiseName;
        this.unit = unit;
        this.allocatedQuantity = allocatedQuantity;
        this.deliveryType = deliveryType;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.price = price;
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

    public int getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public void setAllocatedQuantity(int allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AllocationProposalDTO{" +
                "siteId=" + siteId +
                ", siteName='" + siteName + '\'' +
                ", merchandiseDetailId=" + merchandiseDetailId +
                ", merchandiseName='" + merchandiseName + '\'' +
                ", unit='" + unit + '\'' +
                ", allocatedQuantity=" + allocatedQuantity +
                ", deliveryType='" + deliveryType + '\'' +
                ", expectedDeliveryDate='" + expectedDeliveryDate + '\'' +
                ", price=" + price +
                '}';
    }
}
