package com.motion.laundryq_partner.fragment.register_laundry;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.CategoryAdapter;
import com.motion.laundryq_partner.adapter.DaysAdapter;
import com.motion.laundryq_partner.customviews.NonscrollRecylerview;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.LaundryServicesModel;
import com.motion.laundryq_partner.model.TimeOperationModel;
import com.motion.laundryq_partner.utils.TimeOperationalData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceLaundryFragment extends Fragment {
    @BindView(R.id.rv_time)
    NonscrollRecylerview rvTime;
    @BindView(R.id.rv_category)
    NonscrollRecylerview rvCategory;
    @BindView(R.id.rgDeliveryOrder)
    RadioGroup rgDeliveryOrder;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<TimeOperationModel> timeListSelected = new ArrayList<>();
    private List<CategoryModel> categoryListSelected = new ArrayList<>();

    private CategoryAdapter categoryAdapter;

    private boolean deliberyOrder = false;

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
        databaseReference = firebaseDatabase.getReference(KEY_FDB_CATEGORY);

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
        rvTime.setAdapter(daysAdapter);

        daysAdapter.setData(TimeOperationalData.setTimeOperational());

        categoryAdapter = new CategoryAdapter(getContext(), new CategoryAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(CategoryModel categoryModel) {
                categoryListSelected.add(categoryModel);
            }

            @Override
            public void onItemUpdate(CategoryModel categoryModel) {

            }

            @Override
            public void onItemUncheck(CategoryModel categoryModel) {
                categoryListSelected.remove(categoryModel);
            }
        });

        getDataCategory();

        rgDeliveryOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.rb_no) {
                    deliberyOrder = false;
                } else if (id == R.id.rb_yes) {
                    deliberyOrder = true;
                }
            }
        });

        return v;
    }

    public List<TimeOperationModel> getTimeListSelected() {
        return timeListSelected;
    }

    public List<CategoryModel> getCategoryListSelected() {
        return categoryListSelected;
    }

    public boolean deliveryOrder() {
        return deliberyOrder;
    }

    public boolean isInputValid() {
        if (timeListSelected.size() == 0 || categoryListSelected.size() == 0) {
            return false;
        }
        return true;
    }

    private void getDataCategory() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<CategoryModel> listCategory = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    CategoryModel categoryModel = ds.getValue(CategoryModel.class);
                    assert categoryModel != null;
                    categoryModel.setCategoryID(ds.getKey());
                    listCategory.add(categoryModel);
                }

                rvCategory.setHasFixedSize(true);
                rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
                rvCategory.setAdapter(categoryAdapter);

                categoryAdapter.setData(listCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
