package com.example.prm392_project.data.external.services;

import android.content.Context;

import com.example.prm392_project.data.external.api.ApiClient;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.interfaces.IRescueSvc;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.RescueInvoice;
import com.example.prm392_project.data.external.response.RescueInvoiceReq;
import com.example.prm392_project.data.external.response.RescueReqBody;
import com.example.prm392_project.data.external.response.RescueSummary;
import com.example.prm392_project.data.external.response.RescueUpdate;
import com.example.prm392_project.data.models.RescueReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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

    public void getUserRescueReq(ApiCallback<BaseResp<List<RescueReq>>> callback) {
        Call<BaseResp<List<RescueReq>>> call = rescueSvc.getUserRescueReq();
        call.enqueue(new Callback<BaseResp<List<RescueReq>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueReq>>> call, Response<BaseResp<List<RescueReq>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get user rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueReq>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getAdminRescueReq(ApiCallback<BaseResp<List<RescueReq>>> callback) {
        Call<BaseResp<List<RescueReq>>> call = rescueSvc.getAdminRescueReq();
        call.enqueue(new Callback<BaseResp<List<RescueReq>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueReq>>> call, Response<BaseResp<List<RescueReq>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get admin rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueReq>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getRescueReqById(String id, ApiCallback<BaseResp<RescueReq>> callback) {
        Call<BaseResp<RescueReq>> call = rescueSvc.getRescueReqById(id);
        call.enqueue(new Callback<BaseResp<RescueReq>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueReq>> call, Response<BaseResp<RescueReq>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get rescue request by ID failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueReq>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void updateRescueReq(String id, RescueUpdate request, ApiCallback<BaseResp<RescueReq>> callback) {
        Call<BaseResp<RescueReq>> call = rescueSvc.updateRescueReq(id, request);
        call.enqueue(new Callback<BaseResp<RescueReq>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueReq>> call, Response<BaseResp<RescueReq>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Update rescue request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueReq>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

//    @GET("api/v1/rescue-req/summary")
//    Call<BaseResp<RescueSummary>> getRescueReqSummary();
//
//    @GET("api/v1/rescue-invoice/user")
//    Call<BaseResp<List<RescueInvoice>>> getUserRescueInvoice();
//
//    @GET("api/v1/rescue-invoice/admin")
//    Call<BaseResp<List<RescueInvoice>>> getAdminRescueInvoice();
//
//    @POST("api/v1/rescue-invoice")
//    Call<BaseResp<RescueInvoice>> createRescueInvoice(@Body RescueInvoiceReq request);
//
//    @PUT("api/v1/rescue-invoice/{id}/paid")
//    Call<BaseResp<RescueInvoice>> updateRescueInvoicePaid(@Path("id") String id);

    public void getRescueReqSummary(ApiCallback<BaseResp<RescueSummary>> callback) {
        Call<BaseResp<RescueSummary>> call = rescueSvc.getRescueReqSummary();
        call.enqueue(new Callback<BaseResp<RescueSummary>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueSummary>> call, Response<BaseResp<RescueSummary>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get rescue request summary failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueSummary>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getUserRescueInvoice(ApiCallback<BaseResp<List<RescueInvoice>>> callback) {
        Call<BaseResp<List<RescueInvoice>>> call = rescueSvc.getUserRescueInvoice();
        call.enqueue(new Callback<BaseResp<List<RescueInvoice>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueInvoice>>> call, Response<BaseResp<List<RescueInvoice>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get user rescue invoice failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueInvoice>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getAdminRescueInvoice(ApiCallback<BaseResp<List<RescueInvoice>>> callback) {
        Call<BaseResp<List<RescueInvoice>>> call = rescueSvc.getAdminRescueInvoice();
        call.enqueue(new Callback<BaseResp<List<RescueInvoice>>>() {
            @Override
            public void onResponse(Call<BaseResp<List<RescueInvoice>>> call, Response<BaseResp<List<RescueInvoice>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Get admin rescue invoice failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<List<RescueInvoice>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void createRescueInvoice(RescueInvoiceReq request, ApiCallback<BaseResp<RescueInvoice>> callback) {
        Call<BaseResp<RescueInvoice>> call = rescueSvc.createRescueInvoice(request);
        call.enqueue(new Callback<BaseResp<RescueInvoice>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueInvoice>> call, Response<BaseResp<RescueInvoice>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Create rescue invoice failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueInvoice>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void updateRescueInvoicePaid(String id, ApiCallback<BaseResp<RescueInvoice>> callback) {
        Call<BaseResp<RescueInvoice>> call = rescueSvc.updateRescueInvoicePaid(id);
        call.enqueue(new Callback<BaseResp<RescueInvoice>>() {
            @Override
            public void onResponse(Call<BaseResp<RescueInvoice>> call, Response<BaseResp<RescueInvoice>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Update rescue invoice paid failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResp<RescueInvoice>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
