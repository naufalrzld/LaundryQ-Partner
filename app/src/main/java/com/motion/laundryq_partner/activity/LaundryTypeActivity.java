package com.motion.laundryq_partner.activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.adapter.CategoryAdapter;
import com.motion.laundryq_partner.model.CategoryModel;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.model.LaundryServicesModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORIES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY_NAME;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY_PRICE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_CATEGORY_UNIT;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_SERVICES;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_SERVICES;

public class LaundryTypeActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.btnSimpan)
    Button btnSimpan;

    private List<CategoryModel> currentTypeListSelected = new ArrayList<>();
    private List<CategoryModel> updateTypeListSelected = new ArrayList<>();
    private CategoryAdapter adapter;
    private SharedPreference sharedPreference;
    private LaundryServicesModel laundryServicesModel;

    private ProgressDialog updateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laundry_type);
        ButterKnife.bind(this);

        updateLoading = new ProgressDialog(this);
        updateLoading.setMessage("Updating . . .");
        updateLoading.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_laundry_type_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreference = new SharedPreference(this);
        laundryServicesModel = sharedPreference.getObjectData(KEY_LAUNDRY_SERVICES, LaundryServicesModel.class);
        LaundryModel laundryModel = sharedPreference.getObjectData(KEY_LAUNDRY_PROFILE, LaundryModel.class);
        final String laundryID = laundryModel.getLaundryID();
        currentTypeListSelected = laundryServicesModel.getCagoryList();

        adapter = new CategoryAdapter(this, new CategoryAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(CategoryModel categoryModel) {
                updateTypeListSelected.add(categoryModel);
            }

            @Override
            public void onItemUpdate(CategoryModel categoryModel) {

            }

            @Override
            public void onItemUncheck(CategoryModel categoryModel) {
                updateTypeListSelected.remove(categoryModel);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory(laundryID);
            }
        });

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

                    CategoryModel categoryModelSelected = checkSelectedCategory(ds.getKey());
                    if (categoryModelSelected != null) {
                        categoryModel.setSelected(true);
                        categoryModel.setCategoryUnit(categoryModelSelected.getCategoryUnit());
                        categoryModel.setCategoryPrice(categoryModelSelected.getCategoryPrice());
                    }

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

    private CategoryModel checkSelectedCategory(String categoryID) {
        int i = 0;
        while (i < currentTypeListSelected.size() && !currentTypeListSelected.get(i).getCategoryID().equals(categoryID)) {
            i++;
        }

        if (i < currentTypeListSelected.size() && currentTypeListSelected.get(i).getCategoryID().equals(categoryID)) {
            return currentTypeListSelected.get(i);
        } else {
            return null;
        }
    }

    private void updateCategory(String laundryID) {
        updateLoading.show();
        Map<String, Object> categoriesMap = new HashMap<>();
        for (CategoryModel cm : updateTypeListSelected) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_FDB_CATEGORY_NAME, cm.getCategoryName());
            map.put(KEY_FDB_CATEGORY_PRICE, cm.getCategoryPrice());
            map.put(KEY_FDB_CATEGORY_UNIT, cm.getCategoryUnit());
            categoriesMap.put(cm.getCategoryID(), map);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_LAUNDRY_SERVICES);
        databaseReference.child(laundryID).child(KEY_FDB_CATEGORIES).setValue(categoriesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateLoading.dismiss();
                if (task.isSuccessful()) {
                    laundryServicesModel.setCagoryList(updateTypeListSelected);
                    sharedPreference.storeData(KEY_LAUNDRY_SERVICES, laundryServicesModel);
                    Toast.makeText(LaundryTypeActivity.this, "Update Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LaundryTypeActivity.this, "Update Gagal", Toast.LENGTH_SHORT).show();
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
