package com.example.prm392_project.data.models;
public class RescueReq {
    private String id;
    private String createdBy;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String address;
    private String contact;
    private RescueReqMetadata metadata;
    private int status;
    private User createdUser;
    private String createdAt;

    public RescueReq(String id, String createdBy, String title, String description, double latitude, double longitude, String address, String contact, RescueReqMetadata metadata, int status, User createdUser, String createdAt) {
        this.id = id;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.contact = contact;
        this.metadata = metadata;
        this.status = status;
        this.createdUser = createdUser;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public RescueReqMetadata getMetadata() {
        return metadata;
    }

    public int getStatus() {
        return status;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
