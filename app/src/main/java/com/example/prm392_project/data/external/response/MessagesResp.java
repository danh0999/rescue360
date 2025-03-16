package com.example.prm392_project.data.external.response;

import com.example.prm392_project.data.models.Message;

import java.util.List;

public class MessagesResp {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
