package com.app.auth.login.dto;

public class SignupDTO {
    private String username;
    private String password;
    private String role;
    private String fullname;
    public SignupDTO(String username, String password, String role, String fullname) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullname = fullname;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public String getFullname() {return fullname;}
    public void setRole(String role) {
        this.role = role;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFullname(String fullname) {this.fullname = fullname;}
}
