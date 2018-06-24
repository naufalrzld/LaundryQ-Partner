package com.motion.laundryq_partner.model;

public class TimeOperationModel {
    private String day;
    private String timeOpen;
    private String timeClose;

    public TimeOperationModel(String day, String timeOpen, String timeClose) {
        this.day = day;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public String getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(String timeClose) {
        this.timeClose = timeClose;
    }
}
