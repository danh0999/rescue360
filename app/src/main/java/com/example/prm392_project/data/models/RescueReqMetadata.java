package com.example.prm392_project.data.models;

public class RescueReqMetadata {
    private String accidentType;
    private String vehicleType;
    private String vehicleBrand;
    private String vehicleInfo;

    public RescueReqMetadata(String accidentType, String vehicleType, String vehicleBrand, String vehicleInfo) {
        this.accidentType = accidentType;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleInfo = vehicleInfo;
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
}
