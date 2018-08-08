package com.motion.laundryq_partner.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.CategoryAdapter;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.LaundryServicesModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_SERVICES;

public class LaundryTypeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private List<CategoryModel> categoryListSelected = new ArrayList<>();
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_type);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_laundry_type_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreference sharedPreference = new SharedPreference(this);
        LaundryServicesModel laundryServicesModel = sharedPreference.getObjectData(KEY_LAUNDRY_SERVICES, LaundryServicesModel.class);

        adapter = new CategoryAdapter(this, new CategoryAdapter.OnItemCheckListener() {
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

        adapter.setListCategorySelected(laundryServicesModel.getCagoryList());

        getDataCategory();
    }

    private void getDataCategory() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_CATEGORY);
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
                rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvCategory.setAdapter(adapter);

                adapter.setData(listCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("error", "onCancelled: " + databaseError.getMessage());
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
