package com.motion.laundryq_partner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.motion.laundryq_partner.R;
import com.motion.laundryq_partner.model.UserModel;
import com.motion.laundryq_partner.utils.SharedPreference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_EMAIL;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_NAME;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_PHONE_NUMBER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_URL_PHOTO;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_USERS;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_USER_PARTNER;
import static com.motion.laundryq_partner.utils.AppConstant.KEY_PROFILE;

public class EditProfileActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_photo_profile)
    ImageView imgPhotoProfile;
    @BindView(R.id.tv_change_photo)
    TextView tvChangePhoto;
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.til_phone_number)
    TextInputLayout tilPhoneNumber;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.et_phone_number)
    TextInputEditText etPhoneNumber;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;

    public static final int RESULT_LOAD_IMG = 1;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private SharedPreference sharedPreference;

    private UserModel userModel;

    private Uri imageUri;
    private String userID;

    private ProgressDialog updateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        updateLoading = new ProgressDialog(this);
        updateLoading.setMessage("Uploading photo . . .");
        updateLoading.setCancelable(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_edit_profile_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference(KEY_FDB_USERS).child(KEY_FDB_USER_PARTNER);
        storageReference = FirebaseStorage.getInstance().getReference();

        sharedPreference = new SharedPreference(this);

        initView();
    }

    private void initView() {
        if (sharedPreference.checkIfDataExists(KEY_PROFILE)) {
            userModel = sharedPreference.getObjectData(KEY_PROFILE, UserModel.class);
            userID = userModel.getUserID();
            String name = userModel.getNama();
            String phoneNumber = userModel.getNoTlp();
            String email = userModel.getEmail();
            String urlPhoto = userModel.getUrlPhoto();

            if (!TextUtils.isEmpty(urlPhoto)) {
                Glide.with(this)
                        .load(urlPhoto)
                        .apply(RequestOptions.circleCropTransform())
                        .apply(new RequestOptions().override(400, 400))
                        .into(imgPhotoProfile);
            }

            etName.setText(name);
            etPhoneNumber.setText(phoneNumber);
            etEmail.setText(email);

            tvChangePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMG);
                }
            });
        }
    }

    private void updateProfile(final String userID, final UserModel userModel) {
        updateLoading.show();

        final Map<String, Object> updateUser = new HashMap<>();
        updateUser.put(KEY_FDB_NAME, userModel.getNama());
        updateUser.put(KEY_FDB_EMAIL, userModel.getEmail());
        updateUser.put(KEY_FDB_PHONE_NUMBER, userModel.getNoTlp());

        final StorageReference ref = storageReference.child("images/users/partner/" + userID);
        UploadTask uploadTask = ref.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.d("error", "ERROR");
                    return null;
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    updateLoading.setMessage("Updating . . .");
                    String url = task.getResult().toString();

                    userModel.setUrlPhoto(url);

                    updateUser.put(KEY_FDB_URL_PHOTO, url);

                    databaseReference.child(userID).updateChildren(updateUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateLoading.dismiss();
                            if (task.isSuccessful()) {
                                sharedPreference.storeData(KEY_PROFILE, userModel);

                                Toast.makeText(EditProfileActivity.this, "Update berhasil", Toast.LENGTH_SHORT).show();

                                finish();
                            } else {
                                Toast.makeText(EditProfileActivity.this, "Update gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    updateLoading.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Upload photo failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_save:
                String name = etName.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                String email = etEmail.getText().toString();

                userModel.setNama(name);
                userModel.setNoTlp(phoneNumber);
                userModel.setEmail(email);

                updateProfile(userID, userModel);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageUri = selectedImage;

            Glide.with(this)
                    .load(selectedImage)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgPhotoProfile);
        }
    }
}
