package com.app.common.entity;

public class User {
    long id;
    String username;
    String password;
    String email;
    String phone;
    String address;
    String role;

    public User(long id, String username, String password, String email, String phone, String address, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public User(String username, String role, String password, long id) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.id = id;
    }

    public User(String role, String password, long id) {
        this.role = role;
        this.password = password;
        this.id = id;
    }
}
