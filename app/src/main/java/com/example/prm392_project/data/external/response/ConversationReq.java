package com.example.prm392_project.data.external.response;

import com.example.prm392_project.constants.ApiConst;

public class ConversationReq {
    private String name;
    private String client;

    public ConversationReq(String name) {
        this.name = name;
        this.client = ApiConst.CHAT_CLIENT;
    }

    public String getName() {
        return name;
    }

    public String getClient() {
        return client;
    }
}
