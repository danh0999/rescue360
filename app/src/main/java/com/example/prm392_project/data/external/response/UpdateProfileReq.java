package com.example.prm392_project.data.external.response;

public class UpdateProfileReq {
    private String fullName;
    private String phone;
    private String avatar;

    public UpdateProfileReq(String fullName, String phone, String avatar) {
        this.fullName = fullName;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
