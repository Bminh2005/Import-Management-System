package com.app.common.entity;

public class ImportRequest {
    long id;
    long user_id;
    String status;
    String desired_date;

    public ImportRequest(long id, long user_id, String status, String desired_date) {
        this.id = id;
        this.user_id = user_id;
        this.status = status;
        this.desired_date = desired_date;
    }

    public ImportRequest(long user_id, String status, String desired_date) {
        this.user_id = user_id;
        this.status = status;
        this.desired_date = desired_date;
    }

    public ImportRequest(long id, long user_id, String desired_date) {
        this.id = id;
        this.user_id = user_id;
        this.status = "PENDING";
        this.desired_date = desired_date;
    }

    public ImportRequest(long user_id, String desired_date) {
        this.user_id = user_id;
        this.status = "PENDING";
        this.desired_date = desired_date;
    }
}
