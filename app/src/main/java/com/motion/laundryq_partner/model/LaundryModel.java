package com.motion.laundryq_partner.model;

public class LaundryModel {
    private String laundryName;
    private String phoneNumber;
    private String idLine;
    private LaundryLocationModel laundryLocation;
    private LaundryServicesModel laundryServices;

    public LaundryModel() {
    }

    public LaundryModel(String laundryName, String phoneNumber, String idLine) {
        this.laundryName = laundryName;
        this.phoneNumber = phoneNumber;
        this.idLine = idLine;
    }

    public String getLaundryName() {
        return laundryName;
    }

    public void setLaundryName(String laundryName) {
        this.laundryName = laundryName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIdLine() {
        return idLine;
    }

    public void setIdLine(String idLine) {
        this.idLine = idLine;
    }

    public LaundryLocationModel getLaundryLocation() {
        return laundryLocation;
    }

    public void setLaundryLocation(LaundryLocationModel laundryLocation) {
        this.laundryLocation = laundryLocation;
    }

    public LaundryServicesModel getLaundryServices() {
        return laundryServices;
    }

    public void setLaundryServices(LaundryServicesModel laundryServices) {
        this.laundryServices = laundryServices;
    }
}
