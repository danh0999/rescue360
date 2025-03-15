package com.example.prm392_project.data.models;

public class Message {
    private String content;
    private String conversationId;
    private String createdBy;

    private MessageMetadata metadata;
    private User createdUser;

    public Message(String content, String conversationId, String createdBy, MessageMetadata metadata, User createdUser) {
        this.content = content;
        this.conversationId = conversationId;
        this.createdBy = createdBy;
        this.metadata = metadata;
        this.createdUser = createdUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public MessageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(MessageMetadata metadata) {
        this.metadata = metadata;
    }

    public User getCreatedUser() {
        return createdUser;
    }
}

