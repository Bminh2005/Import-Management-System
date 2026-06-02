package com.app.common.entity;

public class Merchandise {
    long id;
    String merchandise_name;
    String subscription;

    public Merchandise(long id, String merchandise_name, String subscription) {
        this.id = id;
        this.merchandise_name = merchandise_name;
        this.subscription = subscription;
    }

    public Merchandise(String merchandise_name, String subscription) {
        this.merchandise_name = merchandise_name;
        this.subscription = subscription;
    }
}
