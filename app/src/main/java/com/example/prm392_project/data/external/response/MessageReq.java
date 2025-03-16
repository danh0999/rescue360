package com.example.prm392_project.data.external.response;

import com.example.prm392_project.data.models.MessageMetadata;

public class MessageReq {
    private String content;
    private String conversationId;
    private MessageMetadata metadata;

    public MessageReq(String content, String conversationId) {
        this.content = content;
        this.conversationId = conversationId;
    }

    public MessageReq(String content, String conversationId, MessageMetadata metadata) {
        this.content = content;
        this.conversationId = conversationId;
        this.metadata = metadata;
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

    public MessageMetadata getMetadata() {
        return metadata;
    }
}
