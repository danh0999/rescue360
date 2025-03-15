package com.example.prm392_project.data.models;

public class Conversation {
    private String id;
    private String name;
    private String client;
    private String createdBy;
    private String lastMessageId;

    private User createdUser;
    private Message lastMessage;

    private String updatedAt;

    public Conversation(String id, String name, String client, String createdBy, String lastMessageId, User createdUser, Message lastMessage, String updatedAt) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.createdBy = createdBy;
        this.lastMessageId = lastMessageId;
        this.createdUser = createdUser;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getClient() {
        return client;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
