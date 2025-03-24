package com.example.prm392_project.data.models;

import java.util.List;

public class RescueReqMetadata {
    private String accidentType;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleInfo;
    private List<String> imageList;

    public RescueReqMetadata(String accidentType, String vehicleType, String vehicleBrand, String vehicleInfo) {
        this.accidentType = accidentType;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleInfo = vehicleInfo;
    }

    public RescueReqMetadata(String accidentType, String vehicleType, String vehicleBrand, String vehicleInfo, List<String> imageList) {
        this.accidentType = accidentType;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleInfo = vehicleInfo;
        this.imageList = imageList;
    }

    public String getAccidentType() {
        return accidentType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
