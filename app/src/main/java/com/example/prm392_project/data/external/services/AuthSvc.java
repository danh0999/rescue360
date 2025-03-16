package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.constants.AuthType;
import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IAuthSvc;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.LoginReq;
import com.example.prm392_project.data.external.response.LoginResp;
import com.example.prm392_project.data.external.response.RegisterReq;
import com.example.prm392_project.data.models.User;
import com.example.prm392_project.utils.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthSvc {
    private IAuthSvc authSvc;
    private Context context;

    public AuthSvc(Context ctx) {
        authSvc = ApiClient.getClient(ctx).create(IAuthSvc.class);
        context = ctx;
    }

    public void login(String email, String password, ApiCallback<BaseResp<LoginResp>> callback) {
        LoginReq request = new LoginReq(AuthType.Email.getValue(), email, password);
        Call<BaseResp<LoginResp>> call = authSvc.login(request);
        call.enqueue(new Callback<BaseResp<LoginResp>>() {
            @Override
            public void onResponse(Call<BaseResp<LoginResp>> call, Response<BaseResp<LoginResp>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenManager tokenManager = new TokenManager(context);
                    tokenManager.saveToken(response.body().getData().getAccessToken());

                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<LoginResp>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void loginFirebase(String token, ApiCallback<BaseResp<LoginResp>> callback) {
        LoginReq request = new LoginReq(AuthType.Firebase.getValue(), token);
        Call<BaseResp<LoginResp>> call = authSvc.loginFirebase(request);
        call.enqueue(new Callback<BaseResp<LoginResp>>() {
            @Override
            public void onResponse(Call<BaseResp<LoginResp>> call, Response<BaseResp<LoginResp>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenManager tokenManager = new TokenManager(context);
                    tokenManager.saveToken(response.body().getData().getAccessToken());

                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<LoginResp>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void register(String email, String password, String fullName, String phone, String avatar, ApiCallback<BaseResp<User>> callback) {
        RegisterReq request = new RegisterReq(email, password, fullName, phone, avatar);
        Call<BaseResp<User>> call = authSvc.register(request);
        call.enqueue(new Callback<BaseResp<User>>() {
            @Override
            public void onResponse(Call<BaseResp<User>> call, Response<BaseResp<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Register failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<User>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getProfile(ApiCallback<BaseResp<User>> callback) {
        Call<BaseResp<User>> call = authSvc.getProfile();
        call.enqueue(new Callback<BaseResp<User>>() {
            @Override
            public void onResponse(Call<BaseResp<User>> call, Response<BaseResp<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get profile failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<User>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
