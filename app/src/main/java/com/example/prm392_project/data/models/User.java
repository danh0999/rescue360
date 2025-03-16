package com.example.prm392_project.data.models;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String avatar;
    private boolean isAdmin;

    public User(String id, String username, String fullName, String email, String phone, String avatar, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
