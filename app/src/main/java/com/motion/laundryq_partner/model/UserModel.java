package com.motion.laundryq_partner.model;

public class UserModel {
    private String userID;
    private String nama;
    private String noTlp;
    private String email;
    private String password;
    private boolean hasRegisteredLaundry;

    public UserModel() {
    }

    public UserModel(String nama, String noTlp, String email, boolean hasRegisteredLaundry) {
        this.nama = nama;
        this.noTlp = noTlp;
        this.email = email;
        this.hasRegisteredLaundry = hasRegisteredLaundry;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoTlp() {
        return noTlp;
    }

    public void setNoTlp(String noTlp) {
        this.noTlp = noTlp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasRegisteredLaundry() {
        return hasRegisteredLaundry;
    }

    public void setHasRegisteredLaundry(boolean hasRegisteredLaundry) {
        this.hasRegisteredLaundry = hasRegisteredLaundry;
    }
}
