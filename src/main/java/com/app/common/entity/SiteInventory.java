package com.app.common.entity;

public class SiteInventory {
    long id;
    long site_id;
    long merchandise_detail_id;
    int quantity;
    double price;

    public SiteInventory(long id, long site_id, long merchandise_detail_id, int quantity, double price) {
        this.id = id;
        this.site_id = site_id;
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.price = price;
    }

    public SiteInventory(long site_id, long merchandise_detail_id, int quantity, double price) {
        this.site_id = site_id;
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.price = price;
    }
}
