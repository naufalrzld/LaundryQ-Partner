package com.motion.laundryq_partner.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.fragment.main.ListOrderFragment;
import com.motion.laundryq_partner.fragment.main.ProfileFragment;
import com.motion.laundryq_partner.fragment.main.WashingFragment;
import com.motion.laundryq_partner.model.UserModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottomNavView)
    BottomNavigationView bottomNavView;

    private FirebaseAuth auth;

    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();

        sharedPreference = new SharedPreference(this);

        if (sharedPreference.checkIfDataExists("profile")) {
            UserModel userModel = sharedPreference.getObjectData("profile", UserModel.class);
        }

        bottomNavView.setOnNavigationItemSelectedListener(mOnNavItemSelectedListener);

        loadFragment(new ListOrderFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
            switch (item.getItemId()) {
                case R.id.nav_list_order:
                    if (!(currentFragment instanceof ListOrderFragment)) {
                        fragment = new ListOrderFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    break;
                case R.id.nav_cucian:
                    if (!(currentFragment instanceof WashingFragment)) {
                        fragment = new WashingFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    break;
                case R.id.nav_profile:
                    if (!(currentFragment instanceof ProfileFragment)) {
                        fragment = new ProfileFragment();
                        loadFragment(fragment);
                        return true;
                    }
                    break;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
