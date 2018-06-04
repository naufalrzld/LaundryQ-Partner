package com.motion.laundryq_partner;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kofigyan.stateprogressbar.StateProgressBar;
import com.motion.laundryq_partner.adapter.ViewPagerAdapter;
import com.motion.laundryq_partner.fragment.FinishRegisLaundryFragment;
import com.motion.laundryq_partner.fragment.LocationLaundryFragment;
import com.motion.laundryq_partner.fragment.ProfileLaundryFragment;
import com.motion.laundryq_partner.fragment.ServiceLaundryFragment;
import com.motion.laundryq_partner.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterLaundryActivity extends AppCompatActivity {
    @BindView(R.id.step)
    StateProgressBar step;
    @BindView(R.id.tv_step_title)
    TextView tvStepTitle;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.btn_next)
    Button btnNext;

    private ViewPagerAdapter viewPagerAdapter;
    private int viewPagerPosition, currentState;

    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_laundry);
        ButterKnife.bind(this);

        sharedPreference = new SharedPreference(this);

        setupViewPager(viewPager);

        viewPagerPosition = viewPager.getCurrentItem();

        setStepTitle((String) viewPagerAdapter.getPageTitle(viewPagerPosition));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    btnNext.setText("Selesai");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPagerPosition = viewPager.getCurrentItem();
                currentState = step.getCurrentStateNumber();
                if (viewPagerPosition != 3) {
                    nextViewPager(viewPagerPosition, currentState);
                } else {
                    step.setAllStatesCompleted(true);
                    sharedPreference.setLaundryRegistered(true);
                    startActivity(new Intent(RegisterLaundryActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new LocationLaundryFragment(), "Lokasi Laundry");
        viewPagerAdapter.addFragment(new ServiceLaundryFragment(), "Layanan Laundry");
        viewPagerAdapter.addFragment(new ProfileLaundryFragment(), "Profile Laundry");
        viewPagerAdapter.addFragment(new FinishRegisLaundryFragment(), "Selesai");

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setStepTitle(String title) {
        tvStepTitle.setText(title);
    }

    private void nextViewPager(int position, int state) {
        switch (state) {
            case 1:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                break;
            case 2:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                break;
            case 3:
                step.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                break;
        }
        setStepTitle((String) viewPagerAdapter.getPageTitle(position + 1));
        viewPager.setCurrentItem(position + 1);
    }
}
