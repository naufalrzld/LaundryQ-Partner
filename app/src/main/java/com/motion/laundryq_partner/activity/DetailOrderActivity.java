package com.motion.laundryq_partner.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.CategoryOrderedAdapter;
import com.motion.laundryq_partner.customviews.NonscrollRecylerview;
import com.motion.laundryq_partner.model.AddressModel;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.OrderModel;
import com.motion.laundryq_partner.utils.CurrencyConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_ORDER_MODEL;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_READY_TO_SEND;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_STATUS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORIES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY_ID;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_ID_STATUS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_ORDER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_STATUS_ORDER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_INTENT_LIST_ORDER;

public class DetailOrderActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_order_id)
    TextView tvOrderID;
    @BindView(R.id.tv_date_order)
    TextView tvDateOrder;
    @BindView(R.id.tv_address_pickup)
    TextView tvAddressPickup;
    @BindView(R.id.tv_address_delivery)
    TextView tvAddressDelivery;
    @BindView(R.id.tv_time_pickup)
    TextView tvTimePickup;
    @BindView(R.id.tv_time_delivery)
    TextView tvTimeDelivery;
    @BindView(R.id.rv_laundry)
    NonscrollRecylerview rvLaundry;
    @BindView(R.id.tv_net_income)
    TextView tvNetIncome;
    @BindView(R.id.tv_admin_cost)
    TextView tvAdminCost;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.v_border)
    View vBoder;
    @BindView(R.id.lyt_btn)
    LinearLayout lytBtn;
    @BindView(R.id.btn_decline)
    Button btnDecline;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.btn_finish)
    Button btnFinish;

    private DatabaseReference databaseReference;

    private CategoryOrderedAdapter adapter;
    private OrderModel orderModel;
    private AddressModel addressPickModel, addressDeliveryModel;

    private String laundryID;
    private int status;
    private boolean readyToSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_detail_order_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_ORDER);

        Intent dataIntent = getIntent();
        orderModel = dataIntent.getParcelableExtra(KEY_DATA_INTENT_ORDER_MODEL);
        addressPickModel = orderModel.getAddressPick();
        addressDeliveryModel = orderModel.getAddressDelivery();

        status = dataIntent.getIntExtra(KEY_DATA_INTENT_STATUS, 0);
        readyToSend = dataIntent.getBooleanExtra(KEY_DATA_INTENT_READY_TO_SEND, false);

        initView();
        setAdapter(status);
    }

    private void initView() {
        final String orderID = orderModel.getOrderID();
        String dateOrder = orderModel.getDateOrder();
        String pickupAddress = addressPickModel.getAlamat();
        if (!TextUtils.isEmpty(addressPickModel.getAlamatDetail())) {
            pickupAddress = addressPickModel.getAlamatDetail() + " | " + addressPickModel.getAlamat();
        }

        String deliveryAddress = addressDeliveryModel.getAlamat();
        if (!TextUtils.isEmpty(addressDeliveryModel.getAlamatDetail())) {
            deliveryAddress = addressDeliveryModel.getAlamatDetail() + " | " + addressDeliveryModel.getAlamat();
        }

        String datePick = orderModel.getDatePickup();
        String dateDeliv = orderModel.getDateDelivery();
        String timePick = orderModel.getTimePickup();
        String timeDeliv = orderModel.getTimeDelivery();
        String dateTimePick = datePick + ", " + timePick;
        String dateTimeDeliv = dateDeliv + ", " + timeDeliv;
        String total = CurrencyConverter.toIDR(orderModel.getTotal());
        String adminCost = CurrencyConverter.toIDR(orderModel.getAdminCost());
        String netIncome = CurrencyConverter.toIDR(orderModel.getLaundryCost());
        laundryID = orderModel.getLaundryID();

        tvOrderID.setText(orderID);
        tvDateOrder.setText(dateOrder);
        tvAddressPickup.setText(pickupAddress);
        tvAddressDelivery.setText(deliveryAddress);
        tvTimePickup.setText(dateTimePick);
        tvTimeDelivery.setText(dateTimeDeliv);
        tvTotal.setText(total);
        tvAdminCost.setText(adminCost);
        tvNetIncome.setText(netIncome);

        if (status == KEY_INTENT_LIST_ORDER) {
            vBoder.setVisibility(View.VISIBLE);
            lytBtn.setVisibility(View.VISIBLE);

            btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateStatus(orderID, laundryID, 2);
                    vBoder.setVisibility(View.GONE);
                    lytBtn.setVisibility(View.GONE);
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateStatus(orderID, laundryID, 1);
                    vBoder.setVisibility(View.GONE);
                    lytBtn.setVisibility(View.GONE);
                }
            });
        }

        if (isAllLaundryFinished(orderModel.getCategories())) {
            btnFinish.setVisibility(View.VISIBLE);
            if (readyToSend) {
                btnFinish.setText("Kirim");
            }
        }

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status;
                if (readyToSend) {
                    status = 5;
                } else {
                    status = 4;
                }
                updateStatus(orderID, laundryID, status);
                finish();
            }
        });
    }

    private void setAdapter(int status) {
        adapter = new CategoryOrderedAdapter(this, status);
        rvLaundry.setHasFixedSize(true);
        rvLaundry.setLayoutManager(new LinearLayoutManager(this));
        rvLaundry.setAdapter(adapter);

        adapter.setCategories(orderModel.getCategories());
        adapter.setOnButtonUpdateClicked(new CategoryOrderedAdapter.OnButtonUpdateClicked() {
            @Override
            public void onButtonClicked(int status, int position) {
                String orderID = orderModel.getOrderID();
                if (isAllLaundryNotWashing(orderModel.getCategories())) {
                    updateStatus(orderID, laundryID, 3);
                }

                updateStatusCategory(orderID, status, String.valueOf(position));
            }
        });
    }

    private void updateStatus(String orderID, String laundryID, final int status) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_FDB_STATUS_ORDER, status);
        if (status != 3) {
            map.put(KEY_FDB_LAUNDRY_ID_STATUS, laundryID + "_" + status);
        }

        databaseReference.child(orderID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String statusMsg = "";
                if (status == 1) {
                    statusMsg = "Diterima";
                } else if (status == 2) {
                    statusMsg = "Ditolak";
                } else if (status == 3) {
                    statusMsg = "Dicuci";
                } else if (status == 4) {
                    statusMsg = "Selesai";
                } else if (status == 5) {
                    statusMsg = "Dikirim";
                }

                Toast.makeText(getApplicationContext(), statusMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatusCategory(String orderID, int status, final String position) {
        final int updateStatus = status + 1;
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_FDB_STATUS_ORDER, updateStatus);

        databaseReference.child(orderID).child(KEY_FDB_CATEGORIES).child(position).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            int pos = Integer.parseInt(position);
                            List<CategoryModel> categories = orderModel.getCategories();
                            CategoryModel cm = categories.get(pos);
                            cm.setStatus(updateStatus);

                            categories.set(pos, cm);
                            orderModel.setCategories(categories);
                            adapter.notifyItemChanged(pos);

                            if (isAllLaundryFinished(categories)) {
                                btnFinish.setVisibility(View.VISIBLE);
                            }
                        }
                    }
        });
    }

    private boolean isAllLaundryFinished(List<CategoryModel> categories) {
        boolean status = false;
        int count = 0;
        for (CategoryModel cm : categories) {
            if (cm.getStatus() == 0 || cm.getStatus() == 1) {
                count++;
            }
        }

        if (count == 0) {
            status = true;
        }

        return status;
    }

    private boolean isAllLaundryNotWashing(List<CategoryModel> categories) {
        boolean status = false;
        int count = 0;
        for (CategoryModel cm : categories) {
            if (cm.getStatus() == 1) {
                count++;
            }
        }

        if (count == 0) {
            status = true;
        }

        return status;
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
