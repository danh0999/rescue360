package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.external.response.RescueUpdate;
import com.example.prm392_project.data.models.RescueReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRescueSvc {
    @POST("api/v1/rescue-req")
    Call<BaseResp<RescueReq>> createRescueReq(@Body RescueReqBody request);

    @GET("api/v1/rescue-req/user")
    Call<BaseResp<List<RescueReq>>> getUserRescueReq();

    @GET("api/v1/rescue-req/admin")
    Call<BaseResp<List<RescueReq>>> getAdminRescueReq();

    @GET("api/v1/rescue-req/{id}")
    Call<BaseResp<RescueReq>> getRescueReqById(@Path("id") String id);

    @PUT("api/v1/rescue-req/{id}")
    Call<BaseResp<RescueReq>> updateRescueReq(@Path("id") String id, @Body RescueUpdate request);
}
