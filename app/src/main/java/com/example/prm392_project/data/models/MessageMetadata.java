package com.example.prm392_project.data.models;

import java.util.List;

public class MessageMetadata {
    private List<String> images;

    public MessageMetadata(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}