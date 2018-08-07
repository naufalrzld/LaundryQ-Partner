package com.motion.laundryq_partner.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.utils.SharedPreference;

public class SplashscreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000;
    private static final int PERMISSIONS_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        if (!checkPermission()) {
            showPermission();
        } else {
            runActivity();
        }
    }

    public void runActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreference sharedPreference = new SharedPreference(SplashscreenActivity.this);
                boolean isLogin = sharedPreference.isLoggedIn();
                boolean isLundryRegistered = sharedPreference.isLaundryRegistered();
                Intent intent;

                if (isLogin) {
                    if (isLundryRegistered) {
                        intent = new Intent(SplashscreenActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashscreenActivity.this, RegisterLaundryActivity.class);
                    }
                } else {
                    intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void showPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(SplashscreenActivity.this,
                permissions,
                PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                runActivity();
                break;
            default:
                break;
        }
    }
}
