package com.app.modules.procurement.order.model;

public class SiteAllocationEntry {
    private long siteId;
    private String siteName;
    private long merchandiseDetailId;
    private String merchandiseName;
    private String unit;
    private long quantity;
    private double price;
    private long availableQuantity;

    public SiteAllocationEntry() {
    }

    public SiteAllocationEntry(long siteId, String siteName,
                               long merchandiseDetailId, String merchandiseName,
                               String unit, long quantity, double price,
                               long availableQuantity) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.merchandiseDetailId = merchandiseDetailId;
        this.merchandiseName = merchandiseName;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
        this.availableQuantity = availableQuantity;
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

    public long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
