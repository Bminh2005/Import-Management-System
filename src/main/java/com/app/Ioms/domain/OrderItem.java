package com.app.Ioms.domain;

public class OrderItem {
    private final String sku;
    private final String name;
    private final int quantity;

    public OrderItem(String sku, String name, int quantity) {
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }
package com.app.Ioms.domain;

public class OrderItem {
    private final String sku;
    private final String name;
    private final int quantity;

    public OrderItem(String sku, String name, int quantity) {
        this.sku = sku;
        this.name = name;
        this.quantity = quantity;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
}
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
