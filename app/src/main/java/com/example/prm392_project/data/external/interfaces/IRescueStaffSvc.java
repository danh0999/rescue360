package com.example.prm392_project.data.external.interfaces;

import com.example.prm392_project.data.external.response.AssignReq;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.models.RescueAssign;
import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.RescueStaff;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface IRescueStaffSvc {
    @GET("api/v1/rescue-staff/admin")
    Call<BaseResp<List<RescueStaff>>> getRescueStaff();

    @GET("api/v1/rescue-staff/assigns/{rescueReqId}")
    Call<BaseResp<List<RescueAssign>>> getRescueStaffAssigns(String rescueReqId);

    @GET("api/v1/rescue-staff/assigns/staff")
    Call<BaseResp<List<RescueAssign>>> getRescueStaffAssigns();

    @POST("api/v1/rescue-staff/assigns")
    Call<BaseResp<RescueAssign>> assignRescueStaff(AssignReq rescueAssign);
}
