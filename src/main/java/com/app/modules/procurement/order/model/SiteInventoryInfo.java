package com.app.modules.procurement.order.model;

public class SiteInventoryInfo {
    private long siteId;
    private String siteName;
    private String siteAddress;
    private long deliveryByShip;
    private long deliveryByAir;
    private long availableQuantity;
    private double price;
    private int score;

    public SiteInventoryInfo() {
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

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public long getDeliveryByShip() {
        return deliveryByShip;
    }

    public void setDeliveryByShip(long deliveryByShip) {
        this.deliveryByShip = deliveryByShip;
    }

    public long getDeliveryByAir() {
        return deliveryByAir;
    }

    public void setDeliveryByAir(long deliveryByAir) {
        this.deliveryByAir = deliveryByAir;
    }

    public long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
