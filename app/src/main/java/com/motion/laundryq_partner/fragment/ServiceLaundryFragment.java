package com.motion.laundryq_partner.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.DaysAdapter;
import com.motion.laundryq_partner.model.TimeOperationModel;
import com.motion.laundryq_partner.utils.TimeOperationalData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceLaundryFragment extends Fragment {
    @BindView(R.id.rv_time)
    RecyclerView rvTime;

    private List<TimeOperationModel> timeListSelected = new ArrayList<>();

    public ServiceLaundryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service_laundry, container, false);
        ButterKnife.bind(this, v);

        DaysAdapter daysAdapter = new DaysAdapter(getContext(), new DaysAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(TimeOperationModel timeOperationModel) {
                timeListSelected.add(timeOperationModel);
            }

            @Override
            public void onItemUpdate(TimeOperationModel timeOperationModel) {
                timeListSelected.remove(timeOperationModel);
                timeListSelected.add(timeOperationModel);
            }

            @Override
            public void onItemUncheck(TimeOperationModel timeOperationModel) {
                timeListSelected.remove(timeOperationModel);
            }
        });

        rvTime.setHasFixedSize(true);
        rvTime.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTime.setNestedScrollingEnabled(false);
        rvTime.setAdapter(daysAdapter);

        daysAdapter.setData(TimeOperationalData.setTimeOperational());

        return v;
    }

    public List<TimeOperationModel> getTimeOperation() {
        return timeListSelected;
    }
}
