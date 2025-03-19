package com.example.prm392_project.data.models;

public class RescueStaff {
    private String id;
    private String userId;
    private String position;
    private String description;
    public boolean isAvailable;

    public User user;

    public RescueStaff(String id, String userId, String position, String description, boolean isAvailable, User user) {
        this.id = id;
        this.userId = userId;
        this.position = position;
        this.description = description;
        this.isAvailable = isAvailable;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public User getUser() {
        return user;
    }

}
