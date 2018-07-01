package com.motion.laundryq_partner.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq_partner.activity.EditProfileActivity;
import com.motion.laundryq_partner.activity.LoginActivity;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.model.UserModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_ACTIVE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_OPEN;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_laundry_status)
    TextView tvLaundryStatus;
    @BindView(R.id.img_photo_laundry)
    ImageView imgPhotoLaundry;
    @BindView(R.id.tv_laundry_name)
    TextView tvLaundryName;
    @BindView(R.id.tv_count_trx)
    TextView tvCountTrx;
    @BindView(R.id.tv_rate_count)
    TextView tvRateCount;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.tv_count_people_rate)
    TextView tvCountPeopleRate;
    @BindView(R.id.btn_laundry_setting)
    Button btnLaundrySetting;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.btn_edit_profile)
    Button btnEditProfile;
    @BindView(R.id.lyt_logout)
    LinearLayout lytLogout;

    private DatabaseReference databaseReference;

    private SharedPreference sharedPreference;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);

        sharedPreference = new SharedPreference(getContext());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(KEY_FDB_LAUNDRY);

        initViewOwnerProfile();
        initViewLaundryProfile();

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });

        lytLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return v;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        sharedPreference.clearAllData();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }

    private void initViewOwnerProfile() {
        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            UserModel userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            String name = userModel.getNama();
            String phoneNumber = userModel.getNoTlp();
            String email = userModel.getEmail();
            String urlPhoto = userModel.getUrlPhoto();

            if (!TextUtils.isEmpty(urlPhoto)) {
                Glide.with(this)
                        .load(urlPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .apply(new RequestOptions().override(400, 400))
                        .into(imgProfile);
            }

            tvName.setText(name);
            tvPhoneNumber.setText(phoneNumber);
            tvEmail.setText(email);
        }
    }

    private void initViewLaundryProfile() {
        if (sharedPreference.checkIfDataExists(KEY_LAUNDRY_PROFILE)) {
            final LaundryModel laundryModel = sharedPreference.getObjectData(KEY_LAUNDRY_PROFILE, LaundryModel.class);
            String laundryID = laundryModel.getLaundryID();
            String laundryName = laundryModel.getLaundryName();
            String urlPhoto = laundryModel.getUrlPhoto();

            Glide.with(this).load(urlPhoto).into(imgPhotoLaundry);
            tvLaundryName.setText(laundryName);

            databaseReference.child(laundryID).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    String key = dataSnapshot.getKey();
                    Boolean status = (Boolean) dataSnapshot.getValue();
                    assert key != null;
                    switch (key) {
                        case KEY_FDB_LAUNDRY_ACTIVE:
                            laundryModel.setActive(status);
                            break;
                        case KEY_FDB_LAUNDRY_OPEN:
                            laundryModel.setOpen(status);
                            break;
                    }

                    changeStatusLaundry(laundryModel.isActive(), laundryModel.isOpen());

                    sharedPreference.storeData(KEY_LAUNDRY_PROFILE, laundryModel);

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            boolean active  = laundryModel.isActive();
            boolean open = laundryModel.isOpen();

            changeStatusLaundry(active, open);

            tvCountTrx.setText("0");
            tvRateCount.setText("0.0");
            tvCountPeopleRate.setText("0");
        }
    }

    private void changeStatusLaundry(boolean active, boolean open) {
        String status;
        if (active) {
            if (open) {
                status = "Buka";
                tvLaundryStatus.setBackgroundResource(R.drawable.tv_status_open_template);
                tvLaundryStatus.setTextColor(this.getResources().getColor(R.color.colorPrimary));
            } else {
                status = "Tutup";
                tvLaundryStatus.setBackgroundResource(R.drawable.tv_status_close_template);
                tvLaundryStatus.setTextColor(this.getResources().getColor(R.color.colorRed));
            }
        } else {
            status = "Belum Aktif";
            tvLaundryStatus.setBackgroundResource(R.drawable.tv_status_inactive_template);
            tvLaundryStatus.setTextColor(this.getResources().getColor(R.color.colorGray_5));
        }

        tvLaundryStatus.setText(status);
    }

    @Override
    public void onResume() {
        super.onResume();
        initViewOwnerProfile();
        initViewLaundryProfile();
    }
}
