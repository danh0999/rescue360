package com.example.prm392_project.data.external.response;

import com.example.prm392_project.data.models.RescueReq;
import com.example.prm392_project.data.models.User;

public class RescueInvoice {
    private String id;
    private String rescueReqId;
    private String createdBy;
    private double amount;
    private boolean isPaid;
    private RescueInvoiceMetadata metadata;
    private String createdAt;
    private String updatedAt;

    private RescueReq rescueReq;
    private User createdByUser;

    public RescueInvoice(String id, String rescueReqId, String createdBy, double amount, boolean isPaid, RescueInvoiceMetadata metadata, String createdAt, String updatedAt, RescueReq rescueReq, User createdByUser) {
        this.id = id;
        this.rescueReqId = rescueReqId;
        this.createdBy = createdBy;
        this.amount = amount;
        this.isPaid = isPaid;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rescueReq = rescueReq;
        this.createdByUser = createdByUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRescueReqId() {
        return rescueReqId;
    }

    public void setRescueReqId(String rescueReqId) {
        this.rescueReqId = rescueReqId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public RescueInvoiceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(RescueInvoiceMetadata metadata) {
        this.metadata = metadata;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RescueReq getRescueReq() {
        return rescueReq;
    }

    public void setRescueReq(RescueReq rescueReq) {
        this.rescueReq = rescueReq;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }
}
