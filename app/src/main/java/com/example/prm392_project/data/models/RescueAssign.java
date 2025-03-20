package com.example.prm392_project.data.models;

public class RescueAssign {
    private String id;
    private String rescueReqId;
    private String staffId;
    private RescueReq rescueReq;
    private RescueStaff staff;

    public RescueAssign(String id, String rescueReqId, String staffId, RescueReq rescueReq, RescueStaff staff) {
        this.id = id;
        this.rescueReqId = rescueReqId;
        this.staffId = staffId;
        this.rescueReq = rescueReq;
        this.staff = staff;
    }

    public String getId() {
        return id;
    }

    public String getRescueReqId() {
        return rescueReqId;
    }

    public String getStaffId() {
        return staffId;
    }

    public RescueReq getRescueReq() {
        return rescueReq;
    }

    public RescueStaff getStaff() {
        return staff;
    }
}
