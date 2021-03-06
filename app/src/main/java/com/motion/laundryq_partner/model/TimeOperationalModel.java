package com.motion.laundryq_partner.model;

public class TimeOperationalModel {
    private String day;
    private int dayNum;
    private String timeOpen;
    private String timeClose;
    private boolean selected;

    public TimeOperationalModel() {
    }

    public TimeOperationalModel(int dayNum, String timeOpen, String timeClose) {
        this.dayNum = dayNum;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
    }

    public TimeOperationalModel(String day, int dayNum, String timeOpen, String timeClose) {
        this.day = day;
        this.dayNum = dayNum;
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
