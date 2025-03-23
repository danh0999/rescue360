package com.example.prm392_project.data.external.response;

public class StaffUpdate {
    private String position;
    private String description;
    private boolean isAvailable;

    public StaffUpdate(String position, String description, boolean isAvailable) {
        this.position = position;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public StaffUpdate(boolean isAvailable) {
        this.isAvailable = isAvailable;
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
}
