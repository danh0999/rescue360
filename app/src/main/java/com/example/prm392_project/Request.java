package com.example.prm392_project;

public class Request {
    private String title;
    private boolean isApproved;

    public Request(String title, boolean isApproved) {
        this.title = title;
        this.isApproved = isApproved;
    }

    public String getTitle() {
        return title;
    }

    public boolean isApproved() {
        return isApproved;
    }
}
