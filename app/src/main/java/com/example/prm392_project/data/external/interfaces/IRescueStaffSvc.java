package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.AssignReq;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.StaffUpdate;
import com.example.prm392_project.data.models.RescueAssign;
import com.example.prm392_project.data.models.RescueStaff;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRescueStaffSvc {
    @GET("api/v1/rescue-staff/admin")
    Call<BaseResp<List<RescueStaff>>> getRescueStaff();

    @GET("api/v1/rescue-staff/assigns/{rescueReqId}")
    Call<BaseResp<List<RescueAssign>>> getRescueAssigns(@Path("rescueReqId") String rescueReqId);

    @GET("api/v1/rescue-staff/assigns/staff")
    Call<BaseResp<List<RescueAssign>>> getRescueStaffAssigns();

    @POST("api/v1/rescue-staff/assign")
    Call<BaseResp<RescueAssign>> assignRescueStaff(@Body AssignReq rescueAssign);

    @PUT("api/v1/rescue-staff/{id}")
    Call<BaseResp> updateRescueStaff(@Path("id") String id, @Body StaffUpdate rescueStaff);

    @POST("api/v1/rescue-staff/complete/{rescueReqId}")
    Call<BaseResp> completeRescueStaff(@Path("rescueReqId") String rescueReqId);
}
