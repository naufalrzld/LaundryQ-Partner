package com.motion.laundryq_partner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.ViewPagerAdapter;
import com.motion.laundryq_partner.fragment.register_laundry.FinishRegisLaundryFragment;
import com.motion.laundryq_partner.fragment.register_laundry.LocationLaundryFragment;
import com.motion.laundryq_partner.fragment.register_laundry.ProfileLaundryFragment;
import com.motion.laundryq_partner.fragment.register_laundry.ServiceLaundryFragment;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.LaundryLocationModel;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.model.LaundryServicesModel;
import com.motion.laundryq_partner.model.LaundryServicesModelFBS;
import com.motion.laundryq_partner.model.TimeOperationModel;
import com.motion.laundryq_partner.model.UserModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_HAS_REGISTERED_LAUNDRY;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_LOCATION;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_SERVICES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_TIME_CLOSE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_TIME_OPEN;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_USERS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_USER_PARTNER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_SERVICES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_PROFILE;

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

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private LaundryModel laundryModel;
    private LaundryServicesModel laundryServicesModel;
    private UserModel userModel;

    private ProgressDialog registerLoading;

    private String userID, laundryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_laundry);
        ButterKnife.bind(this);

        registerLoading = new ProgressDialog(this);
        registerLoading.setMessage("Saving . . .");
        registerLoading.setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        sharedPreference = new SharedPreference(this);

        userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
        userID = userModel.getUserID();

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
                    Fragment fragment = viewPagerAdapter.getItem(viewPagerPosition);
                    if (fragment instanceof LocationLaundryFragment) {
                        LocationLaundryFragment locationLaundryFragment = (LocationLaundryFragment) fragment;
                        if (locationLaundryFragment.isInputValid()) {
                            String address = locationLaundryFragment.getAddress();
                            String addressDetail = locationLaundryFragment.getAddressDetail();
                            double latitude = locationLaundryFragment.getLatitude();
                            double longitude = locationLaundryFragment.getLongitude();

                            LaundryLocationModel laundryLocationModel = new LaundryLocationModel(address, addressDetail, latitude, longitude);
                            laundryModel.setLocation(laundryLocationModel);
                            saveLocation(laundryID, laundryModel);
                        }
                    } else if (fragment instanceof ServiceLaundryFragment) {
                        ServiceLaundryFragment serviceLaundryFragment = (ServiceLaundryFragment) fragment;
                        if (serviceLaundryFragment.isInputValid()) {
                            List<TimeOperationModel> listTime = serviceLaundryFragment.getTimeListSelected();
                            List<CategoryModel> listCategory = serviceLaundryFragment.getCategoryListSelected();
                            boolean deliveryOrder = serviceLaundryFragment.deliveryOrder();
                            laundryServicesModel = new LaundryServicesModel(listTime, listCategory, deliveryOrder);
                            saveLaundryServices(laundryID, laundryServicesModel);
                        } else {
                            Toast.makeText(RegisterLaundryActivity.this, "Mohon lengkapi data - data yang diminta!", Toast.LENGTH_SHORT).show();
                        }
                    } else if (fragment instanceof ProfileLaundryFragment) {
                        ProfileLaundryFragment profileLaundryFragment = (ProfileLaundryFragment) fragment;
                        if (profileLaundryFragment.isInputValid()) {
                            String laundryName = profileLaundryFragment.getLaundryName();
                            laundryID = laundryName.replaceAll(" ", "_").toLowerCase();
                            String noTlp = profileLaundryFragment.getNoTlp();
                            String idLine = profileLaundryFragment.getIdLine();
                            Uri photo = profileLaundryFragment.getImageUri();

                            laundryModel = new LaundryModel(laundryName, noTlp, idLine, userID, false, false);
                            saveLaundryProfile(laundryID, laundryModel, photo);
                        }
                    }
                } else {
                    step.setAllStatesCompleted(true);
                    sharedPreference.setLaundryRegistered(true);

                    updateDataProfile(userModel);

                    startActivity(new Intent(RegisterLaundryActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ProfileLaundryFragment(), "Profile Laundry");
        viewPagerAdapter.addFragment(new LocationLaundryFragment(), "Lokasi Laundry");
        viewPagerAdapter.addFragment(new ServiceLaundryFragment(), "Layanan Laundry");
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
        viewPagerPosition = viewPager.getCurrentItem();
    }

    private void saveLaundryProfile(final String laundryID, final LaundryModel laundryModel, Uri imageUri) {
        registerLoading.show();
        registerLoading.setMessage("Uploading Photo . . .");
        databaseReference = firebaseDatabase.getReference(KEY_FDB_LAUNDRY);

        laundryModel.setLaundryID(laundryID);

        final StorageReference ref = storageReference.child("images/laundry/" + laundryID);
        UploadTask uploadTask = ref.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("error", "ERROR");
                    return null;
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    registerLoading.setMessage("Saving . . .");
                    String url = task.getResult().toString();
                    laundryModel.setUrlPhoto(url);
                    databaseReference.child(laundryID).setValue(laundryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            registerLoading.dismiss();
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterLaundryActivity.this, "Data tidak tersimpan", Toast.LENGTH_SHORT).show();
                            } else {
                                //sharedPreference.storeData(KEY_LAUNDRY_PROFILE, laundryProfileModel);
                                nextViewPager(viewPagerPosition, currentState);
                            }
                        }
                    });
                } else {
                    registerLoading.dismiss();
                    Toast.makeText(RegisterLaundryActivity.this, "Upload photo failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveLocation(String laundryID, final LaundryModel laundryModel) {
        registerLoading.show();
        databaseReference = firebaseDatabase.getReference(KEY_FDB_LAUNDRY);
        databaseReference.child(laundryID).child(KEY_FDB_LAUNDRY_LOCATION).setValue(laundryModel.
                getLocation())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        registerLoading.dismiss();
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterLaundryActivity.this, "Data tidak tersimpan", Toast.LENGTH_SHORT).show();
                        } else {
                            sharedPreference.storeData(KEY_LAUNDRY_PROFILE, laundryModel);
                            nextViewPager(viewPagerPosition, currentState);
                        }
                    }
                });
    }

    private void saveLaundryServices(String laundryID, final LaundryServicesModel laundryServicesModel) {
        registerLoading.show();
        databaseReference = firebaseDatabase.getReference(KEY_FDB_LAUNDRY_SERVICES);

        List<TimeOperationModel> timeOperational = laundryServicesModel.getTimeOperationalList();
        List<CategoryModel> categoryList = laundryServicesModel.getCagoryList();

        Map<String, String> timesMap = new HashMap<>();
        Map<String, Map<String, String>> dayTimeMap = new HashMap<>();
        for (TimeOperationModel tom : timeOperational) {
            timesMap.put(KEY_FDB_TIME_OPEN, tom.getTimeOpen());
            timesMap.put(KEY_FDB_TIME_CLOSE, tom.getTimeClose());
            dayTimeMap.put(tom.getDay(), timesMap);
        }

        Map<String, CategoryModel> categoriesMap = new HashMap<>();
        for (CategoryModel cm : categoryList) {
            CategoryModel cat = new CategoryModel(cm.getCategoryPrice(), cm.getCategoryUnit());
            categoriesMap.put(cm.getCategoryID(), cat);
        }

        LaundryServicesModelFBS laundryServicesModelFBS = new LaundryServicesModelFBS(
                laundryServicesModel.isDeliveryOrder(),
                categoriesMap,
                dayTimeMap);

        databaseReference.child(laundryID).setValue(laundryServicesModelFBS)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        registerLoading.dismiss();
                        if (task.isSuccessful()) {
                            sharedPreference.storeData(KEY_LAUNDRY_SERVICES, laundryServicesModel);
                            nextViewPager(viewPagerPosition, currentState);
                        } else {
                            Toast.makeText(RegisterLaundryActivity.this, "Data tidak tersimpan", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateDataProfile(UserModel userModel) {
        databaseReference = firebaseDatabase.getReference(KEY_FDB_USERS).child(KEY_FDB_USER_PARTNER);

        userModel.setLaundry(laundryID);
        userModel.setHasRegisteredLaundry(true);

        sharedPreference.storeData(KEY_PROFILE, userModel);

        databaseReference.child(userID).child(KEY_FDB_HAS_REGISTERED_LAUNDRY).setValue(true);
        databaseReference.child(userID).child(KEY_FDB_LAUNDRY).setValue(laundryID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = viewPagerAdapter.getItem(viewPagerPosition);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
