package com.motion.laundryq_partner.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.utils.SharedPreference;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        int SPLASH_TIME_OUT = 3000;

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
}
