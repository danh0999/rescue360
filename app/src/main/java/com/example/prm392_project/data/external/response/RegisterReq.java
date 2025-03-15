package com.example.prm392_project.data.external.response;

//{
//        "email": "string",
//        "password": "string",
//        "fullName": "string",
//        "phone": "string",
//        "avatar": "string"
//        }

public class RegisterReq {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String avatar;

    public RegisterReq(String email, String password, String fullName, String phone, String avatar) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }
}
