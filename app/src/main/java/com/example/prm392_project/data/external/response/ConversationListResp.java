package com.example.prm392_project.data.external.response;

import com.example.prm392_project.data.models.Conversation;

import java.util.List;

public class ConversationListResp {
    private int total;
    private List<Conversation> conversations;

    public ConversationListResp(int total, List<Conversation> conversations) {
        this.total = total;
        this.conversations = conversations;
    }

    public int getTotal() {
        return total;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

}
