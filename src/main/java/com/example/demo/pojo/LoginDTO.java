package com.example.demo.pojo;
import lombok.Getter;

@Getter

public class LoginDTO {

        private String email;
        private String password;

        public LoginDTO(String email, String password) {
            this.email = email;
            this.password = password;
        }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
