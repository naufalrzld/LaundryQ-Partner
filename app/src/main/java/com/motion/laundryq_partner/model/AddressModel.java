package com.motion.laundryq_partner.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModel implements Parcelable {
    private String alamat;
    private String alamatDetail;
    private double latitude;
    private double longitude;

    public AddressModel() {
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getAlamatDetail() {
        return alamatDetail;
    }

    public void setAlamatDetail(String alamatDetail) {
        this.alamatDetail = alamatDetail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.alamat);
        dest.writeString(this.alamatDetail);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    protected AddressModel(Parcel in) {
        this.alamat = in.readString();
        this.alamatDetail = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<AddressModel> CREATOR = new Parcelable.Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel source) {
            return new AddressModel(source);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };
}
