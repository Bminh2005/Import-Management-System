package com.app.common.entity;

public class MerchandiseDetail {
    long id;
    long merchandise_id;
    String unit;
    double reference_price;

    public MerchandiseDetail(long id, long merchandise_id, String unit, double reference_price) {
        this.id = id;
        this.merchandise_id = merchandise_id;
        this.unit = unit;
        this.reference_price = reference_price;
    }

    public MerchandiseDetail(long merchandise_id, String unit, double reference_price) {
        this.merchandise_id = merchandise_id;
        this.unit = unit;
        this.reference_price = reference_price;
    }


}
