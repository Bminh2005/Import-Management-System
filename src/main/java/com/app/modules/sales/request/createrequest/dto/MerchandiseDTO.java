package com.app.modules.sales.request.createrequest.dto;

public class MerchandiseDTO {
    long id;
    String merchandise_name;
    String unit;
    double reference_price;

    public MerchandiseDTO(long id, String merchandise_name, String unit, double reference_price) {
        this.id = id;
        this.merchandise_name = merchandise_name;
        this.unit = unit;
        this.reference_price = reference_price;
    }
    public MerchandiseDTO(String merchandise_name, String unit, double reference_price) {
        this.merchandise_name = merchandise_name;
        this.unit = unit;
    }

    public long getId() {
        return id;
    }

    public String getMerchandise_name() {
        return merchandise_name;
    }

    public String getUnit() {
        return unit;
    }

    public double getReference_price() {
        return reference_price;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMerchandise_name(String merchandise_name) {
        this.merchandise_name = merchandise_name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setReference_price(double reference_price) {
        this.reference_price = reference_price;
    }
}
