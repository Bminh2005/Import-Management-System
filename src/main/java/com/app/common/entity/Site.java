package com.app.common.entity;

public class Site {
    long id;
    String site_name;
    String site_url;
    String site_address;
    double site_distance;
    String more_info;
    long delivery_by_ship;
    long delivery_by_air;

    public Site(long id, String site_name, String site_url, String site_address, double site_distance, String more_info, long delivery_by_ship, long delivery_by_air) {
        this.id = id;
        this.site_name = site_name;
        this.site_url = site_url;
        this.site_address = site_address;
        this.site_distance = site_distance;
        this.more_info = more_info;
        this.delivery_by_ship = delivery_by_ship;
        this.delivery_by_air = delivery_by_air;
    }

}
