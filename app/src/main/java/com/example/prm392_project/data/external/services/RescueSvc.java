package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IRescueSvc;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.models.RescueReq;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RescueSvc {
    private IRescueSvc rescueSvc;
    private Context context;

    public RescueSvc(Context ctx) {
        rescueSvc = ApiClient.getClient(ctx).create(IRescueSvc.class);
        context = ctx;
    }

    public void createRescueReq(RescueReqBody request, ApiCallback<BaseResp<RescueReq>> callback) {
        Call<BaseResp<RescueReq>> call = rescueSvc.createRescueReq(request);
        call.enqueue(new Callback<BaseResp<RescueReq>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueReq>> call, Response<BaseResp<RescueReq>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Create rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueReq>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getUserRescueReq(ApiCallback<BaseResp<RescueReq>> callback) {
        Call<BaseResp<RescueReq>> call = rescueSvc.getUserRescueReq();
        call.enqueue(new Callback<BaseResp<RescueReq>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueReq>> call, Response<BaseResp<RescueReq>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get user rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueReq>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getAdminRescueReq(ApiCallback<BaseResp<RescueReq>> callback) {
        Call<BaseResp<RescueReq>> call = rescueSvc.getAdminRescueReq();
        call.enqueue(new Callback<BaseResp<RescueReq>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueReq>> call, Response<BaseResp<RescueReq>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get admin rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueReq>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
