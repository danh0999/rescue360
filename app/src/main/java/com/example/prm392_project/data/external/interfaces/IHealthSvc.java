package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.BaseResp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IHealthSvc {
    @GET("api/v1/ping")
    Call<BaseResp> healthCheck();
}
