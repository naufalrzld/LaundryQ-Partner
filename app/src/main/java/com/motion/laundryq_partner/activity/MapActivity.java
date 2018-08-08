package com.motion.laundryq_partner.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.LaundryLocationModel;
import com.motion.laundryq_partner.model.LaundryModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_ADDRESS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_ADDRESS_DETAIL;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_DATA_INTENT_EDIT;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_LAUNDRY_LOCATION;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_LAUNDRY_PROFILE;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.btn_set_location)
    Button btnSetLocation;
    @BindView(R.id.til_alamat)
    TextInputLayout tilAlamat;
    @BindView(R.id.til_alamat_detail)
    TextInputLayout tilAlamatDetail;
    @BindView(R.id.et_alamat)
    TextInputEditText etAlamat;
    @BindView(R.id.et_alamat_detail)
    TextInputEditText etAlamatDetail;
    @BindView(R.id.btn_simpan)
    Button btnSimpan;

    private static final String TAG = MapActivity.class.getSimpleName();
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int MAP_REQUEST_CODE = 125;

    private boolean locationPermissionGranted = false;

    private GoogleMap mGoogleMap;

    private DatabaseReference databaseReference;
    private ProgressDialog mapLaoding;
    private SharedPreference sharedPreference;
    private LaundryModel laundryModel;

    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mapLaoding = new ProgressDialog(this);
        mapLaoding.setMessage("Loading . . .");
        mapLaoding.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_map_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(KEY_FDB_LAUNDRY);

        sharedPreference = new SharedPreference(this);
        laundryModel = sharedPreference.getObjectData(KEY_LAUNDRY_PROFILE, LaundryModel.class);
        final String laundryID = laundryModel.getLaundryID();

        Intent dataIntent = getIntent();
        if (dataIntent.getBooleanExtra(KEY_DATA_INTENT_EDIT, false)) {
            String address = dataIntent.getStringExtra(KEY_DATA_INTENT_ADDRESS);
            String addressDetail = dataIntent.getStringExtra(KEY_DATA_INTENT_ADDRESS_DETAIL);
            etAlamat.setText(address);
            etAlamatDetail.setText(addressDetail);

            etAlamat.setSelection(address.length());
            etAlamatDetail.setSelection(addressDetail.length());
        }

        getLocationPermission();

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = getCompleteAddressString(latitude, longitude);
                String enter[] = address.split("\n");
                etAlamat.setText(enter[0]);
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etAlamat.getText().toString();
                String detailAddress = etAlamatDetail.getText().toString();

                if (isInputValid(address)) {
                    saveLocation(laundryID, address, detailAddress, latitude, longitude);
                }
            }
        });
    }

    private boolean isInputValid(String address) {
        tilAlamat.setErrorEnabled(false);
        tilAlamatDetail.setErrorEnabled(false);

        if (TextUtils.isEmpty(address)) {
            if (TextUtils.isEmpty(address)) {
                tilAlamat.setErrorEnabled(true);
                tilAlamat.setError("Alamat harus diisi!");
            }

            return false;
        }

        return true;
    }

    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (locationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);

            mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
                    LatLng latLng = cameraPosition.target;
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
            });
        }
    }

    public void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat " + latLng.latitude + ", lng: " + latLng.longitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        latitude = latLng.latitude;
        longitude = latLng.longitude;

    }

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (locationPermissionGranted) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    moveCamera(new LatLng(latitude, longitude), DEFAULT_ZOOM);
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d(TAG, strReturnedAddress.toString());
            } else {
                Log.d(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, "Canont get Address!");
        }

        return strAdd;
    }

    private void saveLocation(final String laundryID, String alamat, String detailAlamat, double latitude, double longitude) {
        mapLaoding.show();
        LaundryLocationModel addressModel = new LaundryLocationModel(alamat, detailAlamat, latitude, longitude);

        databaseReference.child(laundryID).child(KEY_FDB_LAUNDRY_LOCATION).setValue(addressModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    databaseReference.child(laundryID).child(KEY_FDB_LAUNDRY_LOCATION).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mapLaoding.dismiss();
                            LaundryLocationModel result = dataSnapshot.getValue(LaundryLocationModel.class);

                            if (result != null) {
                                laundryModel.setLocation(result);

                                sharedPreference.storeData(KEY_LAUNDRY_PROFILE, laundryModel);

                                Toast.makeText(getApplicationContext(), "Lokasi Tersimpan", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal Menyimpan Lokasi", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, databaseError.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(MapActivity.this, "Update Lokasi Gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(MapActivity.this);
                    startActivityForResult(intent, MAP_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionGranted = false;
                            return;
                        }
                    }
                    locationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                moveCamera(place.getLatLng(), DEFAULT_ZOOM);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.d(TAG, status.getStatusMessage());
            }
        }
    }
}
