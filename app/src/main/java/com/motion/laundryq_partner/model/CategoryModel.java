package com.motion.laundryq_partner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {
    private String categoryID;
    private String categoryName;
    private Integer categoryPrice;
    private String categoryUnit;
    private String icon;
    private Integer quantity;
    private Integer status;

    public CategoryModel() {
    }

    public CategoryModel(String categoryID, String category_name) {
        this.categoryID = categoryID;
        this.categoryName = category_name;
    }

    public CategoryModel(String categoryID, String category_name, int categoryPrice, String categoryUnit) {
        this.categoryID = categoryID;
        this.categoryName = category_name;
        this.categoryPrice = categoryPrice;
        this.categoryUnit = categoryUnit;
    }

    public CategoryModel(String category_name, int categoryPrice, String categoryUnit) {
        this.categoryName = category_name;
        this.categoryPrice = categoryPrice;
        this.categoryUnit = categoryUnit;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String category_name) {
        this.categoryName = category_name;
    }

    public Integer getCategoryPrice() {
        return categoryPrice;
    }

    public void setCategoryPrice(Integer categoryPrice) {
        this.categoryPrice = categoryPrice;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryID);
        dest.writeString(this.categoryName);
        dest.writeValue(this.categoryPrice);
        dest.writeString(this.categoryUnit);
        dest.writeString(this.icon);
        dest.writeValue(this.quantity);
        dest.writeValue(this.status);
    }

    protected CategoryModel(Parcel in) {
        this.categoryID = in.readString();
        this.categoryName = in.readString();
        this.categoryPrice = (Integer) in.readValue(Integer.class.getClassLoader());
        this.categoryUnit = in.readString();
        this.icon = in.readString();
        this.quantity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CategoryModel> CREATOR = new Parcelable.Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel source) {
            return new CategoryModel(source);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
