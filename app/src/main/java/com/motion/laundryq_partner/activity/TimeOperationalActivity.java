package com.motion.laundryq_partner.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.DaysAdapter;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.model.LaundryServicesModel;
import com.motion.laundryq_partner.model.TimeOperationalModel;
import com.motion.laundryq_partner.utils.SharedPreference;
import com.motion.laundryq_partner.utils.TimeOperationalData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_DAY_NUM;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_SERVICES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_TIME_CLOSE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_TIME_OPEN;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_TIME_OPERATIONAL;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_SERVICES;

public class TimeOperationalActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private List<TimeOperationalModel> currentTimeListSelected = new ArrayList<>();
    private List<TimeOperationalModel> updateTimeList = new ArrayList<>();

    private SharedPreference sharedPreference;
    private LaundryServicesModel laundryServicesModel;

    private ProgressDialog updateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_operational);
        ButterKnife.bind(this);

        updateLoading = new ProgressDialog(this);
        updateLoading.setMessage("Updating . . .");
        updateLoading.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_time_operational_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreference = new SharedPreference(this);
        laundryServicesModel = sharedPreference.getObjectData(KEY_LAUNDRY_SERVICES, LaundryServicesModel.class);
        LaundryModel laundryModel = sharedPreference.getObjectData(KEY_LAUNDRY_PROFILE, LaundryModel.class);
        final String laundryID = laundryModel.getLaundryID();
        currentTimeListSelected = laundryServicesModel.getTimeOperationalList();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTimeOperational(laundryID);
            }
        });

        setDay();
    }

    private void setDay() {
        List<TimeOperationalModel> timeList = new ArrayList<>();
        DaysAdapter daysAdapter = new DaysAdapter(this, new DaysAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(TimeOperationalModel timeOperationModel) {
                updateTimeList.add(timeOperationModel);
            }

            @Override
            public void onItemUpdate(TimeOperationalModel timeOperationModel) {

            }

            @Override
            public void onItemUncheck(TimeOperationalModel timeOperationModel) {
                updateTimeList.remove(timeOperationModel);
            }
        });

        rvTime.setHasFixedSize(true);
        rvTime.setLayoutManager(new LinearLayoutManager(this));
        rvTime.setAdapter(daysAdapter);

        for (TimeOperationalModel tom : TimeOperationalData.getTimeOperational()) {
            TimeOperationalModel selectedTom = getSelectedTime(tom.getDayNum());
            if (selectedTom != null) {
                tom.setSelected(true);
                tom.setTimeOpen(selectedTom.getTimeOpen());
                tom.setTimeClose(selectedTom.getTimeClose());
            }

            timeList.add(tom);
        }

        daysAdapter.setData(timeList);
    }

    private TimeOperationalModel getSelectedTime(int dayNum) {
        int i = 0;
        while (i < currentTimeListSelected.size() && currentTimeListSelected.get(i).getDayNum() != dayNum) {
            i++;
        }

        if (i < currentTimeListSelected.size() && currentTimeListSelected.get(i).getDayNum() == dayNum) {
            return currentTimeListSelected.get(i);
        }

        return null;
    }

    private void updateTimeOperational(String laundryID) {
        updateLoading.show();
        Map<String, Object> dayMap = new HashMap<>();
        for (TimeOperationalModel tom : updateTimeList) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_FDB_DAY_NUM, tom.getDay());
            map.put(KEY_FDB_TIME_CLOSE, tom.getTimeClose());
            map.put(KEY_FDB_TIME_OPEN, tom.getTimeOpen());
            dayMap.put(tom.getDay(), map);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_LAUNDRY_SERVICES);
        databaseReference.child(laundryID).child(KEY_FDB_TIME_OPERATIONAL).setValue(dayMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateLoading.dismiss();
                if (task.isSuccessful()) {
                    laundryServicesModel.setTimeOperationalList(updateTimeList);
                    sharedPreference.storeData(KEY_LAUNDRY_SERVICES, laundryServicesModel);
                    Toast.makeText(TimeOperationalActivity.this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TimeOperationalActivity.this, "Update Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
