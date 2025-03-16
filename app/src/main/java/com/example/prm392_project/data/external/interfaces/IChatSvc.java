package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.MessageReq;
import com.example.prm392_project.data.external.response.MessagesResp;
import com.example.prm392_project.data.models.Conversation;
import com.example.prm392_project.data.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IChatSvc {
    @GET("api/v1/chat/conversation/rescue")
    Call<BaseResp<Conversation>> getUserRescueConversation();

    @POST("api/v1/chat/message")
    Call<BaseResp<Message>> createRescueReq(@Body MessageReq request);

    @GET("api/v1/chat/conversation/messages")
    Call<BaseResp<MessagesResp>> getMessagesByConversationId(
            @Query("page") int page,
            @Query("pageSize") int pageSize,
            @Query("conversationId") String conversationId
    );

    @GET("api/v1/chat/conversation/admin")
    Call<BaseResp<List<Conversation>>> getAdminRescueConversation();
}
