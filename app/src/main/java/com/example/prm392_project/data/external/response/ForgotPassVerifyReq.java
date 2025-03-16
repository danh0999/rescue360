package com.example.prm392_project.data.external.response;

public class ForgotPassVerifyReq {
    private String email;
    private String otp;
    private String newPassword;

    public ForgotPassVerifyReq(String email, String otp, String newPassword) {
        this.email = email;
        this.otp = otp;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return otp;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
