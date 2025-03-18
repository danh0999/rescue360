package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IRescueStaffSvc;
import com.example.prm392_project.data.external.response.AssignReq;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.models.RescueAssign;
import com.example.prm392_project.data.models.RescueStaff;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RescueStaffSvc {
    private IRescueStaffSvc rescueStaffSvc;
    private Context context;

    public RescueStaffSvc(Context ctx) {
        rescueStaffSvc = ApiClient.getClient(ctx).create(IRescueStaffSvc.class);
        context = ctx;
    }

    public void getRescueStaff(ApiCallback<BaseResp<List<RescueStaff>>> callback) {
        Call<BaseResp<List<RescueStaff>>> call = rescueStaffSvc.getRescueStaff();
        call.enqueue(new Callback<BaseResp<List<RescueStaff>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueStaff>>> call, Response<BaseResp<List<RescueStaff>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get rescue staff failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueStaff>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getRescueStaffAssigns(String rescueReqId, ApiCallback<BaseResp<List<RescueAssign>>> callback) {
        Call<BaseResp<List<RescueAssign>>> call = rescueStaffSvc.getRescueStaffAssigns(rescueReqId);
        call.enqueue(new Callback<BaseResp<List<RescueAssign>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueAssign>>> call, Response<BaseResp<List<RescueAssign>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get rescue staff assigns failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueAssign>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }


    public void getRescueStaffAssigns(ApiCallback<BaseResp<List<RescueAssign>>> callback) {
        Call<BaseResp<List<RescueAssign>>> call = rescueStaffSvc.getRescueStaffAssigns();
        call.enqueue(new Callback<BaseResp<List<RescueAssign>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueAssign>>> call, Response<BaseResp<List<RescueAssign>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get rescue staff assigns failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueAssign>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void assignRescueStaff(AssignReq rescueAssign, ApiCallback<BaseResp<RescueAssign>> callback) {
        Call<BaseResp<RescueAssign>> call = rescueStaffSvc.assignRescueStaff(rescueAssign);
        call.enqueue(new Callback<BaseResp<RescueAssign>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueAssign>> call, Response<BaseResp<RescueAssign>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Assign rescue staff failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueAssign>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }



}
