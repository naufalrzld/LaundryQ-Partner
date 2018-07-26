package com.motion.laundryq_partner.fragment.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.OrderAdapter;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.model.OrderModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_ID_STATUS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_ORDER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_STATUS_ORDER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListOrderFragment extends Fragment implements OrderAdapter.OnButtonDeclineClicked,
        OrderAdapter.OnButtonAcceptClicked {
    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_order_list)
    RecyclerView rvList;

    private DatabaseReference databaseReference;

    private OrderAdapter adapter;

    public ListOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);

        databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_ORDER);

        SharedPreference sharedPreference = new SharedPreference(getContext());
        LaundryModel laundryModel = sharedPreference.getObjectData(KEY_LAUNDRY_PROFILE, LaundryModel.class);
        final String laundryID_status = laundryModel.getLaundryID() + "_0";

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataOrder(laundryID_status);
            }
        });

        adapter = new OrderAdapter(getContext());
        adapter.setOnButtonDeclineClicked(this);
        adapter.setOnButtonAcceptClicked(this);
        rvList.setHasFixedSize(true);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvList.setAdapter(adapter);

        getDataOrder(laundryID_status);

        return v;
    }

    private void getDataOrder(String laundryID) {
        swipeRefreshLayout.setRefreshing(true);
        final List<OrderModel> orderList = new ArrayList<>();

        databaseReference.orderByChild(KEY_FDB_LAUNDRY_ID_STATUS).equalTo(laundryID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                swipeRefreshLayout.setRefreshing(false);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    OrderModel orderModel = ds.getValue(OrderModel.class);
                    orderList.add(orderModel);
                }

                adapter.setOrderList(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onAccClick(String orderID, String laundryID) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_FDB_STATUS_ORDER, 1);
        map.put(KEY_FDB_LAUNDRY_ID_STATUS, laundryID + "_1");

        databaseReference.child(orderID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Diterima", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDecClick(String orderID, String laundryID) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_FDB_STATUS_ORDER, 2);
        map.put(KEY_FDB_LAUNDRY_ID_STATUS, laundryID + "_2");

        databaseReference.child(orderID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "Ditolak", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
