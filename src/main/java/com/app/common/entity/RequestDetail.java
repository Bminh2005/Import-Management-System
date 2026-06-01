package com.app.common.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class RequestDetail {
    long id;
    long merchandise_detail_id;
    int quantity;
    long request_id;
    LocalDate desired_date;

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

    public RequestDetail(long merchandise_detail_id, int quantity) {
        this.merchandise_detail_id = merchandise_detail_id;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getMerchandise_detail_id() {
        return merchandise_detail_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getRequest_id() {
        return request_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMerchandise_detail_id(long merchandise_detail_id) {
        this.merchandise_detail_id = merchandise_detail_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setRequest_id(long request_id) {
        this.request_id = request_id;
    }

    public LocalDate getDesired_date() {
        return desired_date;
    }

    public void setDesired_date(LocalDate desired_date) {
        this.desired_date = desired_date;
    }
}
