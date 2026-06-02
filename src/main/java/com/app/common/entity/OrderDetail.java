package com.app.common.entity;

public class OrderDetail {
    long id;
    long order_id;
    long merchandise_detail_id;
    int quantity;
    String refused_reason;
    String status;

    public OrderDetail(long id, long order_id, long merchandise_detail_id, int quantity, String refused_reason, String status) {
        this.id = id;
        this.order_id = order_id;
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.refused_reason = refused_reason;
        this.status = status;
    }
    public OrderDetail(long order_id, long merchandise_detail_id, int quantity, String refused_reason, String status) {
        this.order_id = order_id;
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.refused_reason = refused_reason;
        this.status = status;
    }
    public OrderDetail(long order_id, long merchandise_detail_id, int quantity) {
        this.order_id = order_id;
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.status = "PENDING";
        this.refused_reason = "";
    }
}
