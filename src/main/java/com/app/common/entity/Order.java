package com.app.common.entity;

public class Order {
    long id;
    long site_id;
    String expected_delivery_date;
    long user_id;
    long request_id;
    String status;
    String delivery; //SHIP OR AIR

    public Order(long id, long site_id, String expected_delivery_date, long user_id, long request_id, String status, String delivery) {
        this.id = id;
        this.site_id = site_id;
        this.expected_delivery_date = expected_delivery_date;
        this.user_id = user_id;
        this.request_id = request_id;
        this.status = status;
        this.delivery = delivery;
    }

    public Order(String delivery, long request_id, long user_id, String expected_delivery_date, long site_id, long id) {
        this.delivery = delivery;
        this.request_id = request_id;
        this.user_id = user_id;
        this.status = "PENDING";
        this.expected_delivery_date = expected_delivery_date;
        this.site_id = site_id;
        this.id = id;
    }

    public Order(long site_id, String expected_delivery_date, long user_id, long request_id, String delivery) {
        this.site_id = site_id;
        this.expected_delivery_date = expected_delivery_date;
        this.user_id = user_id;
        this.status = "PENDING";
        this.request_id = request_id;
        this.delivery = delivery;
    }

    public Order(long site_id, String expected_delivery_date, long user_id, long request_id, String status, String delivery) {
        this.site_id = site_id;
        this.expected_delivery_date = expected_delivery_date;
        this.user_id = user_id;
        this.request_id = request_id;
        this.status = status;
        this.delivery = delivery;
    }
}
