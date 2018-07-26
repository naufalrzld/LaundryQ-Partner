package com.motion.laundryq_partner.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderModel implements Parcelable {
    private String orderID;
    private String laundryID;
    private String userID;
    private String addressPick;
    private String addressDetailPick;
    private String addressDeliv;
    private String addressDetailDeliv;
    private double latPick;
    private double lngPick;
    private double latDeliv;
    private double lngDeliv;
    private int total;
    private String datePickup;
    private String timePickup;
    private String dateDelivery;
    private String timeDelivery;
    private String dateOrder;
    private int status;
    private String laundryID_status;
    private List<CategoryModel> categories;

    public OrderModel() {
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getLaundryID() {
        return laundryID;
    }

    public void setLaundryID(String laundryID) {
        this.laundryID = laundryID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddressPick() {
        return addressPick;
    }

    public void setAddressPick(String addressPick) {
        this.addressPick = addressPick;
    }

    public String getAddressDetailPick() {
        return addressDetailPick;
    }

    public void setAddressDetailPick(String addressDetailPick) {
        this.addressDetailPick = addressDetailPick;
    }

    public String getAddressDeliv() {
        return addressDeliv;
    }

    public void setAddressDeliv(String addressDeliv) {
        this.addressDeliv = addressDeliv;
    }

    public String getAddressDetailDeliv() {
        return addressDetailDeliv;
    }

    public void setAddressDetailDeliv(String addressDetailDeliv) {
        this.addressDetailDeliv = addressDetailDeliv;
    }

    public double getLatPick() {
        return latPick;
    }

    public void setLatPick(double latPick) {
        this.latPick = latPick;
    }

    public double getLngPick() {
        return lngPick;
    }

    public void setLngPick(double lngPick) {
        this.lngPick = lngPick;
    }

    public double getLatDeliv() {
        return latDeliv;
    }

    public void setLatDeliv(double latDeliv) {
        this.latDeliv = latDeliv;
    }

    public double getLngDeliv() {
        return lngDeliv;
    }

    public void setLngDeliv(double lngDeliv) {
        this.lngDeliv = lngDeliv;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDatePickup() {
        return datePickup;
    }

    public void setDatePickup(String datePickup) {
        this.datePickup = datePickup;
    }

    public String getTimePickup() {
        return timePickup;
    }

    public void setTimePickup(String timePickup) {
        this.timePickup = timePickup;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public String getTimeDelivery() {
        return timeDelivery;
    }

    public void setTimeDelivery(String timeDelivery) {
        this.timeDelivery = timeDelivery;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLaundryID_status() {
        return laundryID_status;
    }

    public void setLaundryID_status(String laundryID_status) {
        this.laundryID_status = laundryID_status;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderID);
        dest.writeString(this.laundryID);
        dest.writeString(this.userID);
        dest.writeString(this.addressPick);
        dest.writeString(this.addressDetailPick);
        dest.writeString(this.addressDeliv);
        dest.writeString(this.addressDetailDeliv);
        dest.writeDouble(this.latPick);
        dest.writeDouble(this.lngPick);
        dest.writeDouble(this.latDeliv);
        dest.writeDouble(this.lngDeliv);
        dest.writeInt(this.total);
        dest.writeString(this.datePickup);
        dest.writeString(this.timePickup);
        dest.writeString(this.dateDelivery);
        dest.writeString(this.timeDelivery);
        dest.writeString(this.dateOrder);
        dest.writeInt(this.status);
        dest.writeString(this.laundryID_status);
        dest.writeList(this.categories);
    }

    protected OrderModel(Parcel in) {
        this.orderID = in.readString();
        this.laundryID = in.readString();
        this.userID = in.readString();
        this.addressPick = in.readString();
        this.addressDetailPick = in.readString();
        this.addressDeliv = in.readString();
        this.addressDetailDeliv = in.readString();
        this.latPick = in.readDouble();
        this.lngPick = in.readDouble();
        this.latDeliv = in.readDouble();
        this.lngDeliv = in.readDouble();
        this.total = in.readInt();
        this.datePickup = in.readString();
        this.timePickup = in.readString();
        this.dateDelivery = in.readString();
        this.timeDelivery = in.readString();
        this.dateOrder = in.readString();
        this.status = in.readInt();
        this.laundryID_status = in.readString();
        this.categories = new ArrayList<CategoryModel>();
        in.readList(this.categories, CategoryModel.class.getClassLoader());
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel source) {
            return new OrderModel(source);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };
}
