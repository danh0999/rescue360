package com.example.prm392_project.data.external.response;

public class AssignReq {
    private String rescueReqId;
    private String staffId;

    public AssignReq(String rescueReqId, String staffId) {
        this.rescueReqId = rescueReqId;
        this.staffId = staffId;
    }

    public String getRescueReqId() {
        return rescueReqId;
    }

    public String getStaffId() {
        return staffId;
    }
}
