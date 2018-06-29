package com.motion.laundryq_partner.fragment.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.motion.laundryq_partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

}
