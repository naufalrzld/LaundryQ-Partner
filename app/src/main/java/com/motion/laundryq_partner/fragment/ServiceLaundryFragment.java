package com.motion.laundryq_partner.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.CategoryAdapter;
import com.motion.laundryq_partner.adapter.DaysAdapter;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.TimeOperationModel;
import com.motion.laundryq_partner.utils.TimeOperationalData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.RegisterAccountActivity.USER_PARTNER;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceLaundryFragment extends Fragment {
    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<TimeOperationModel> timeListSelected = new ArrayList<>();
    private List<CategoryModel> listCategory = new ArrayList<>();

    public ServiceLaundryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_service_laundry, container, false);
        ButterKnife.bind(this, v);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("category");

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

        getDataCategory();

        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(CategoryModel categoryModel) {
                Toast.makeText(getContext(), categoryModel.getCategoryID() + " " + categoryModel.getCategoryName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemUpdate(CategoryModel categoryModel) {

            }

            @Override
            public void onItemUncheck(CategoryModel categoryModel) {

            }
        });

        rvCategory.setHasFixedSize(false);
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategory.setAdapter(categoryAdapter);

        categoryAdapter.setData(listCategory);

        return v;
    }

    public List<TimeOperationModel> getTimeOperation() {
        return timeListSelected;
    }

    public boolean isInputValid() {
        if (timeListSelected.size() == 0) {
            return false;
        }
        return true;
    }

    private void getDataCategory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CategoryModel categoryModel = new CategoryModel(ds.getKey(), ds.child("category_name").getValue(String.class));
                    listCategory.add(categoryModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
