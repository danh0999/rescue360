package com.example.prm392_project.data.external.response;

public class RescueInvoiceReq {
    private String rescueReqId;
    private double amount;
    private RescueInvoiceMetadata metadata;

    public RescueInvoiceReq(String rescueReqId, double amount, RescueInvoiceMetadata metadata) {
        this.rescueReqId = rescueReqId;
        this.amount = amount;
        this.metadata = metadata;
    }

    public String getRescueReqId() {
        return rescueReqId;
    }

    public void setRescueReqId(String rescueReqId) {
        this.rescueReqId = rescueReqId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public RescueInvoiceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RescueInvoiceMetadata metadata) {
        this.metadata = metadata;
    }
}
