package com.app.Ioms.domain;
package com.app.Ioms.domain;

public class Allocation {
    private final String orderId;
    private final String sku;
    private final String siteCode;
    private final int quantity;

    public Allocation(String orderId, String sku, String siteCode, int quantity) {
        this.orderId = orderId;
        this.sku = sku;
        this.siteCode = siteCode;
        this.quantity = quantity;
    }

    public String getOrderId() { return orderId; }
    public String getSku() { return sku; }
    public String getSiteCode() { return siteCode; }
    public int getQuantity() { return quantity; }
}
public class Allocation {
    private final String orderId;
    private final String sku;
    private final String siteCode;
    private final int quantity;

    public Allocation(String orderId, String sku, String siteCode, int quantity) {
        this.orderId = orderId;
        this.sku = sku;
        this.siteCode = siteCode;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSku() {
        return sku;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public int getQuantity() {
        return quantity;
    }
}
