package com.app.common.entity;

public class RequestDetail {
    long id;
    long merchandise_detail_id;
    int quantity;
    long request_id;

    public RequestDetail(long id, long merchandise_detail_id, int quantity, long request_id) {
        this.id = id;
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.request_id = request_id;
    }

    public RequestDetail(long merchandise_detail_id, int quantity, long request_id) {
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
        this.request_id = request_id;
    }
}
