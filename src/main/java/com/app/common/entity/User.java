package com.app.common.entity;

public class User {
    long id;
    String username;
    String password;
    String role;
    String fullname;


    public User(long id, String username, String password, String role, String fullname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String role, String username,String password, String fullname) {
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
