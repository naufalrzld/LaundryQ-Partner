package com.motion.laundryq_partner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SharedPreference {
    private static final String PREF_NAME = "Session";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_IS_LAUNDRY_REGISTERED = "isLaundryRegistered";
    private static Gson gson = new Gson();

    private SharedPreferences sharedPreferences;

    private Context mContext;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        this.mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLaundryRegistered(boolean status) {
        editor.putBoolean(KEY_IS_LAUNDRY_REGISTERED, status);
        editor.commit();
    }

    public boolean isLaundryRegistered(){
        return sharedPreferences.getBoolean(KEY_IS_LAUNDRY_REGISTERED, false);
    }

    public void storeData(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public boolean checkIfDataExists(String key) {
        return sharedPreferences.contains(key);
    }

    public <T> T getObjectData(String key, Class<T> object) {
        String json = sharedPreferences.getString(key, null);
        return gson.fromJson(json, object);
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
