package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IUploadSvc;
import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.UploadResp;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadSvc {
    private IUploadSvc uploadSvc;
    private Context context;

    public UploadSvc(Context ctx) {
        uploadSvc = ApiClient.getClient(ctx).create(IUploadSvc.class);
        context = ctx;
    }

    public void uploadImage(MultipartBody.Part file, ApiCallback<BaseResp<UploadResp>> callback) {
        Call<BaseResp<UploadResp>> call = uploadSvc.uploadImage(file);
        call.enqueue(new Callback<BaseResp<UploadResp>>() {
            @Override
            public void onResponse(Call<BaseResp<UploadResp>> call, Response<BaseResp<UploadResp>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Upload image failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<UploadResp>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

}
