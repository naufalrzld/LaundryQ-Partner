package com.motion.laundryq_partner.fragment.register_laundry;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.motion.laundryq_partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileLaundryFragment extends Fragment {
    @BindView(R.id.img_photo_laundry)
    ImageView imgPhotoLaundry;
    @BindView(R.id.til_laundry_name)
    TextInputLayout tilLaundryName;
    @BindView(R.id.til_no_tlp)
    TextInputLayout tilNoTlp;
    @BindView(R.id.til_id_line)
    TextInputLayout tilIDLine;
    @BindView(R.id.et_laundry_name)
    TextInputEditText etLaundryName;
    @BindView(R.id.et_no_tlp)
    TextInputEditText etNoTlp;
    @BindView(R.id.et_id_line)
    TextInputEditText etIDLine;

    public static final int RESULT_LOAD_IMG = 1;

    private Uri imageUri;

    public ProfileLaundryFragment() {
        // Required empty public constructor
    }

    public String getLaundryName() {
        return etLaundryName.getText().toString();
    }

    public String getNoTlp() {
        return etNoTlp.getText().toString();
    }

    public String getIdLine() {
        return etIDLine.getText().toString();
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_laundry, container, false);
        ButterKnife.bind(this, v);

        imgPhotoLaundry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(intent, RESULT_LOAD_IMG);
            }
        });

        return v;
    }

    public boolean isInputValid() {
        tilLaundryName.setErrorEnabled(false);
        tilNoTlp.setErrorEnabled(false);

        if (TextUtils.isEmpty(etLaundryName.getText().toString()) || TextUtils.isEmpty(etNoTlp.getText().toString())
                || imageUri == null) {
            if (TextUtils.isEmpty(etLaundryName.getText().toString())) {
                tilLaundryName.setErrorEnabled(true);
                tilLaundryName.setError("Nama laundry harus diisi!");
            }

            if (TextUtils.isEmpty(etNoTlp.getText().toString())) {
                tilNoTlp.setErrorEnabled(true);
                tilNoTlp.setError("Nomor telepon / WhatsApp harus diisi!");
            }

            if (imageUri == null) {
                Toast.makeText(getContext(), "Masukkan foto laundry anda!", Toast.LENGTH_SHORT).show();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            setImageUri(selectedImage);
            imgPhotoLaundry.setImageURI(selectedImage);
        }
    }
}
