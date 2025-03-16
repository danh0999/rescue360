package com.example.prm392_project.data.external.response;

import com.example.prm392_project.constants.AuthType;

public class LoginReq {
    private int authType;
    private String email;
    private String password;
    private String firebaseToken;

    public LoginReq(int authType, String firebaseToken) {
        this.authType = authType;
        this.firebaseToken = firebaseToken;
    }

    public LoginReq(int authType, String email, String password) {
        this.email = email;
        this.password = password;
        this.authType = authType;
    }

    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAuthType() {
        return authType;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }
}
