package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IChatSvc;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.MessageReq;
import com.example.prm392_project.data.external.response.MessagesResp;
import com.example.prm392_project.data.models.Conversation;
import com.example.prm392_project.data.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatSvc {
    IChatSvc chatSvc;
    private Context context;

    public ChatSvc(Context ctx) {
        chatSvc = ApiClient.getClient(ctx).create(IChatSvc.class);
        context = ctx;
    }

    public void getUserRescueConversation(ApiCallback<BaseResp<Conversation>> callback) {
        Call<BaseResp<Conversation>> call = chatSvc.getUserRescueConversation();
        call.enqueue(new Callback<BaseResp<Conversation>>() {
            @Override
            public void onResponse(Call<BaseResp<Conversation>> call, Response<BaseResp<Conversation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get user rescue conversation failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<Conversation>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void createMessage(MessageReq request, ApiCallback<BaseResp<Message>> callback) {
        Call<BaseResp<Message>> call = chatSvc.createRescueReq(request);
        call.enqueue(new Callback<BaseResp<Message>>() {
            @Override
            public void onResponse(Call<BaseResp<Message>> call, Response<BaseResp<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Create rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<Message>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getMessagesByConversationId(int page, int pageSize, String conversationId, ApiCallback<BaseResp<MessagesResp>> callback) {
        Call<BaseResp<MessagesResp>> call = chatSvc.getMessagesByConversationId(page, pageSize, conversationId);
        call.enqueue(new Callback<BaseResp<MessagesResp>>() {
            @Override
            public void onResponse(Call<BaseResp<MessagesResp>> call, Response<BaseResp<MessagesResp>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get messages by conversation ID failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<MessagesResp>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getAdminRescueConversation(ApiCallback<BaseResp<List<Conversation>>> callback) {
        Call<BaseResp<List<Conversation>>> call = chatSvc.getAdminRescueConversation();
        call.enqueue(new Callback<BaseResp<List<Conversation>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<Conversation>>> call, Response<BaseResp<List<Conversation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get admin rescue conversation failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<Conversation>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
