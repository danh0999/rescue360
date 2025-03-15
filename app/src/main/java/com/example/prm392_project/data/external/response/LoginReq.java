package com.example.prm392_project.data.external.response;

import com.example.prm392_project.constants.AuthType;

public class LoginReq {
    private AuthType authType;
    private String username;
    private String password;

    public LoginReq(AuthType authType, String username, String password) {
        this.username = username;
        this.password = password;
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
