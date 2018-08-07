package com.motion.laundryq_partner.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OrderModel implements Parcelable {
    private String orderID;
    private String laundryID;
    private String userID;
    private int laundryCost;
    private int adminCost;
    private int total;
    private String datePickup;
    private String timePickup;
    private String dateDelivery;
    private String timeDelivery;
    private String dateOrder;
    private int status;
    private String laundryID_status;
    private AddressModel addressPick;
    private AddressModel addressDelivery;
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

    public int getLaundryCost() {
        return laundryCost;
    }

    public void setLaundryCost(int laundryCost) {
        this.laundryCost = laundryCost;
    }

    public int getAdminCost() {
        return adminCost;
    }

    public void setAdminCost(int adminCost) {
        this.adminCost = adminCost;
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

    public AddressModel getAddressPick() {
        return addressPick;
    }

    public void setAddressPick(AddressModel addressPick) {
        this.addressPick = addressPick;
    }

    public AddressModel getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(AddressModel addressDelivery) {
        this.addressDelivery = addressDelivery;
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
        dest.writeInt(this.laundryCost);
        dest.writeInt(this.adminCost);
        dest.writeInt(this.total);
        dest.writeString(this.datePickup);
        dest.writeString(this.timePickup);
        dest.writeString(this.dateDelivery);
        dest.writeString(this.timeDelivery);
        dest.writeString(this.dateOrder);
        dest.writeInt(this.status);
        dest.writeString(this.laundryID_status);
        dest.writeParcelable(this.addressPick, flags);
        dest.writeParcelable(this.addressDelivery, flags);
        dest.writeTypedList(this.categories);
    }

    protected OrderModel(Parcel in) {
        this.orderID = in.readString();
        this.laundryID = in.readString();
        this.userID = in.readString();
        this.laundryCost = in.readInt();
        this.adminCost = in.readInt();
        this.total = in.readInt();
        this.datePickup = in.readString();
        this.timePickup = in.readString();
        this.dateDelivery = in.readString();
        this.timeDelivery = in.readString();
        this.dateOrder = in.readString();
        this.status = in.readInt();
        this.laundryID_status = in.readString();
        this.addressPick = in.readParcelable(AddressModel.class.getClassLoader());
        this.addressDelivery = in.readParcelable(AddressModel.class.getClassLoader());
        this.categories = in.createTypedArrayList(CategoryModel.CREATOR);
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
