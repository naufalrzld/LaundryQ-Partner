package com.motion.laundryq_partner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
    @BindView(R.id.img_search)
    ImageView imgSearch;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.btn_next)
    Button btnNext;

    private static final String TAG = RegisterLaundryActivity.class.getSimpleName();
    public static final int MAP_REQUEST_CODE = 125;

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
                if (position != 0) {
                    imgSearch.setVisibility(View.GONE);
                }

                if (position == 3) {
                    btnNext.setText("Selesai");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(RegisterLaundryActivity.this);
                    startActivityForResult(intent, MAP_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = viewPagerAdapter.getItem(viewPagerPosition);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
