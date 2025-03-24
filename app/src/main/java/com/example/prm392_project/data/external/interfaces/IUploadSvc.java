package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.UploadResp;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IUploadSvc {
    @Multipart
    @POST("api/v1/upload")
    Call<BaseResp<UploadResp>> uploadImage(@Part MultipartBody.Part file);
}
