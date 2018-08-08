package com.motion.laundryq_partner.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.LaundryLocationModel;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.model.LaundryServicesModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_ADDRESS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_ADDRESS_DETAIL;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_EDIT;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_DELIVERY_ORDER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_SERVICES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_SERVICES;

public class DetailLaundryActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sw_open_close)
    Switch swOpenClose;
    @BindView(R.id.lyt_location)
    LinearLayout lytLaundryLocation;
    @BindView(R.id.lyt_time_operational)
    LinearLayout lytTimeOperational;
    @BindView(R.id.lyt_laundry_type)
    LinearLayout lytLaundryType;
    @BindView(R.id.lyt_delivery_order)
    LinearLayout lytDeliveryOrder;
    @BindView(R.id.lyt_washing_history)
    LinearLayout lytWashingHistory;

    private SharedPreference sharedPreference;
    private LaundryLocationModel laundryLocationModel;
    private LaundryServicesModel laundryServicesModel;

    private String laundryID;
    private boolean deliveryOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_laundry_detail_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        swOpenClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean status) {
                String statusMsg;
                if (status) {
                    statusMsg = "Buka";
                } else {
                    statusMsg = "Tutup";
                }
                swOpenClose.setText(statusMsg);
            }
        });

        lytLaundryLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailLaundryActivity.this, MapActivity.class);
                if (laundryLocationModel != null) {
                    intent.putExtra(KEY_DATA_INTENT_EDIT, true);
                    intent.putExtra(KEY_DATA_INTENT_ADDRESS, laundryLocationModel.getAddress());
                    intent.putExtra(KEY_DATA_INTENT_ADDRESS_DETAIL, laundryLocationModel.getAddressDetail());
                }
                startActivity(intent);
            }
        });

        lytTimeOperational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailLaundryActivity.this, TimeOperationalActivity.class));
            }
        });

        lytLaundryType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailLaundryActivity.this, LaundryTypeActivity.class));
            }
        });

        lytDeliveryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogDeliveryOrder();
            }
        });
    }

    private void showDialogDeliveryOrder() {
        View dialogView = getLayoutInflater().inflate(R.layout.delivery_order_layout, null);
        final RadioGroup rgDeliveryOrder = dialogView.findViewById(R.id.rgDeliveryOrder);
        RadioButton rbNo = dialogView.findViewById(R.id.rb_no);
        RadioButton rbYes = dialogView.findViewById(R.id.rb_yes);

        if (deliveryOrder) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }

        new AlertDialog.Builder(this)
                .setTitle("Pesan Antar")
                .setView(dialogView)
                .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean deliveryOrder = false;
                        int id = rgDeliveryOrder.getCheckedRadioButtonId();
                        if (id == R.id.rb_yes) {
                            deliveryOrder = true;
                        }

                        updateDeliveryOrder(laundryID, deliveryOrder);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private void updateDeliveryOrder(String laundryID, final boolean deliveryOrder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_LAUNDRY_SERVICES);
        databaseReference.child(laundryID).child(KEY_FDB_DELIVERY_ORDER).setValue(deliveryOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String message;
                if (task.isSuccessful()) {
                    message = "Update Berhasil";

                    laundryServicesModel.setDeliveryOrder(deliveryOrder);
                    sharedPreference.storeData(KEY_LAUNDRY_SERVICES, laundryServicesModel);

                    onResume();
                } else {
                    message = "Update Gagal";
                }

                Toast.makeText(DetailLaundryActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreference = new SharedPreference(this);
        LaundryModel laundryModel = sharedPreference.getObjectData(KEY_LAUNDRY_PROFILE, LaundryModel.class);
        laundryServicesModel = sharedPreference.getObjectData(KEY_LAUNDRY_SERVICES, LaundryServicesModel.class);
        laundryID = laundryModel.getLaundryID();
        deliveryOrder = laundryServicesModel.isDeliveryOrder();
        laundryLocationModel = laundryModel.getLocation();
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
