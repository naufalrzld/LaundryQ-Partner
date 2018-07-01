package com.motion.laundryq_partner.model;

public class LaundryModel {
    private String laundryID;
    private String laundryName;
    private String urlPhoto;
    private String phoneNumber;
    private String idLine;
    private String owner;
    private Boolean active;
    private Boolean open;
    private LaundryLocationModel location;

    public LaundryModel() {
    }

    public LaundryModel(String laundryName, String phoneNumber, String idLine, String owner, Boolean active, Boolean open) {
        this.laundryName = laundryName;
        this.phoneNumber = phoneNumber;
        this.idLine = idLine;
        this.owner = owner;
        this.active = active;
        this.open = open;
    }

    public String getLaundryID() {
        return laundryID;
    }

    public void setLaundryID(String laundryID) {
        this.laundryID = laundryID;
    }

    public String getLaundryName() {
        return laundryName;
    }

    public void setLaundryName(String laundryName) {
        this.laundryName = laundryName;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public LaundryLocationModel getLocation() {
        return location;
    }

    public void setLocation(LaundryLocationModel location) {
        this.location = location;
    }
}
