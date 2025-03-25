package com.example.prm392_project.data.external.response;

public class RescueSummary {
    private int totalUsers;
    private int totalRescueReqs;
    private int totalRescueReqsCompleted;
    private int totalStaffs;
    private double totalRevenue;

    public RescueSummary(int totalUsers, int totalRescueReqs, int totalRescueReqsCompleted, int totalStaffs, double totalRevenue) {
        this.totalUsers = totalUsers;
        this.totalRescueReqs = totalRescueReqs;
        this.totalRescueReqsCompleted = totalRescueReqsCompleted;
        this.totalStaffs = totalStaffs;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalRescueReqs() {
        return totalRescueReqs;
    }

    public void setTotalRescueReqs(int totalRescueReqs) {
        this.totalRescueReqs = totalRescueReqs;
    }

    public int getTotalRescueReqsCompleted() {
        return totalRescueReqsCompleted;
    }

    public void setTotalRescueReqsCompleted(int totalRescueReqsCompleted) {
        this.totalRescueReqsCompleted = totalRescueReqsCompleted;
    }

    public int getTotalStaffs() {
        return totalStaffs;
    }

    public void setTotalStaffs(int totalStaffs) {
        this.totalStaffs = totalStaffs;
    }


    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }


}
