package com.example.prm392_project.data.external.response;

import com.example.prm392_project.constants.RescueStatus;
import com.example.prm392_project.data.models.RescueReqMetadata;

public class RescueReqBody {
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private String address;
    private String contact;
    private RescueReqMetadata metadata;
    private int status;

    public RescueReqBody(String title, String description, double latitude, double longitude, String address, String contact, RescueReqMetadata metadata) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.contact = contact;
        this.metadata = metadata;
    }

    public RescueReqBody(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public RescueReqMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RescueReqMetadata metadata) {
        this.metadata = metadata;
    }
}
