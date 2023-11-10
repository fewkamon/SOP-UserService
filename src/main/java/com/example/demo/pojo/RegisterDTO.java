package com.example.demo.pojo;

import java.util.Date;

public class RegisterDTO {
    private String email;
    private String password;
    private boolean has_shop;
    private UserInfo info;

    // Default Constructor
    public RegisterDTO() {
    }

    // Parameterized Constructor
    public RegisterDTO(String email, String password, boolean has_shop, UserInfo info) {
        this.email = email;
        this.password = password;
        this.has_shop = has_shop;
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isHas_shop() {
        return has_shop;
    }

    public UserInfo getInfo() {
        return info;
    }
}
