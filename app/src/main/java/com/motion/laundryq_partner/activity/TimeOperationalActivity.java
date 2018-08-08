package com.motion.laundryq_partner.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.DaysAdapter;
import com.motion.laundryq_partner.model.TimeOperationModel;
import com.motion.laundryq_partner.utils.TimeOperationalData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimeOperationalActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private List<TimeOperationModel> timeListSelected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_operational);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_time_operational_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setDay();
    }

    private void setDay() {
        DaysAdapter daysAdapter = new DaysAdapter(this, new DaysAdapter.OnItemCheckListener() {
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
        rvTime.setLayoutManager(new LinearLayoutManager(this));
        rvTime.setAdapter(daysAdapter);

        daysAdapter.setData(TimeOperationalData.setTimeOperational());
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
