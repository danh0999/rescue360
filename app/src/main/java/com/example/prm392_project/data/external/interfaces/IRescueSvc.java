package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.models.RescueReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IRescueSvc {
    @POST("api/v1/rescue-req")
    Call<BaseResp<RescueReq>> createRescueReq(@Body RescueReqBody request);

    @GET("api/v1/rescue-req/user")
    Call<BaseResp<RescueReq>> getUserRescueReq();

    @GET("api/v1/rescue-req/admin")
    Call<BaseResp<RescueReq>> getAdminRescueReq();
}
