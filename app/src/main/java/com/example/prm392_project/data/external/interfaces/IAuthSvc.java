package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.LoginReq;
import com.example.prm392_project.data.external.response.LoginResp;
import com.example.prm392_project.data.external.response.RegisterReq;
import com.example.prm392_project.data.external.response.UpdateProfileReq;
import com.example.prm392_project.data.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IAuthSvc {
    @POST("api/v1/auth/login")
    Call<BaseResp<LoginResp>> login(@Body LoginReq request);

    @POST("api/v1/auth/login")
    Call<BaseResp<LoginResp>> loginFirebase(@Body LoginReq request);

    @POST("api/v1/auth/register")
    Call<BaseResp<User>> register(@Body RegisterReq request);

    @GET("api/v1/users/profile")
    Call<BaseResp<User>> getProfile();

    @PUT("api/v1/users/profile")
    Call<BaseResp> updateProfile(@Body UpdateProfileReq user);

}
