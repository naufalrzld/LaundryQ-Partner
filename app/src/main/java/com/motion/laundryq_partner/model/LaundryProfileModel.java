package com.motion.laundryq_partner.model;

public class LaundryProfileModel {
    private String laundryID;
    private String urlPhoto;
    private String laundryName;
    private String phoneNumber;
    private String idLine;
    private String owner;
    private boolean active;

    public LaundryProfileModel() {
    }

    public LaundryProfileModel(String laundryName, String phoneNumber, String idLine, String owner, boolean active) {
        this.laundryName = laundryName;
        this.phoneNumber = phoneNumber;
        this.idLine = idLine;
        this.owner = owner;
        this.active = active;
    }

    public String getLaundryID() {
        return laundryID;
    }

    public void setLaundryID(String laundryID) {
        this.laundryID = laundryID;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
