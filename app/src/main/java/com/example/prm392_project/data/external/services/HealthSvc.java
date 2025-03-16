package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IHealthSvc;
import com.example.prm392_project.data.external.response.BaseResp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthSvc {
    private IHealthSvc healthSvc;
    private Context context;

    public HealthSvc(Context ctx) {
        healthSvc = ApiClient.getClient(ctx).create(IHealthSvc.class);
        context = ctx;
    }

    public void healthCheck(ApiCallback<BaseResp> callback) {
        Call<BaseResp> call = healthSvc.healthCheck();
        call.enqueue(new Callback<BaseResp>() {
            @Override
            public void onResponse(Call<BaseResp> call, Response<BaseResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Health check failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
