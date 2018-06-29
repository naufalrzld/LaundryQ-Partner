package com.motion.laundryq_partner;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.motion.laundryq_partner.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq_partner.utils.AppConstant.KEY_FDB_USER_PARTNER;

public class RegisterAccountActivity extends AppCompatActivity {
    @BindView(R.id.til_nama)
    TextInputLayout tilNama;
    @BindView(R.id.til_no_tlp)
    TextInputLayout tilNoTlp;
    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.til_confirm_password)
    TextInputLayout tilConfPassword;

    @BindView(R.id.et_nama)
    TextInputEditText etNama;
    @BindView(R.id.et_no_tlp)
    TextInputEditText etNoTlp;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_passwod)
    TextInputEditText etPassword;
    @BindView(R.id.et_confirm_passwod)
    TextInputEditText etConfPassword;

    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog registerLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        registerLoading = new ProgressDialog(this);
        registerLoading.setMessage("Loading . . .");
        registerLoading.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNama.getText().toString();
                String noTlp = etNoTlp.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confPassword = etConfPassword.getText().toString();

                if (isInputValid(nama, noTlp, email, password, confPassword)) {
                    registerUser(nama, noTlp, email, password);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean isInputValid(String nama, String noTlp, String email, String pass, String confPass) {
        tilNama.setErrorEnabled(false);
        tilNoTlp.setErrorEnabled(false);
        tilEmail.setErrorEnabled(false);
        tilPassword.setErrorEnabled(false);
        tilConfPassword.setErrorEnabled(false);

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(noTlp) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(pass) || TextUtils.isEmpty(confPass)) {
            if (TextUtils.isEmpty(nama)) {
                tilNama.setErrorEnabled(true);
                tilNama.setError("Nama tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(noTlp)) {
                tilNoTlp.setErrorEnabled(true);
                tilNoTlp.setError("Nomot telepon tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(email)) {
                tilEmail.setErrorEnabled(true);
                tilEmail.setError("Email tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(pass)) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Password tidak boleh kosong!");
            }

            if (TextUtils.isEmpty(confPass)) {
                tilConfPassword.setErrorEnabled(true);
                tilConfPassword.setError("Konfirmasi password tidak boleh kosong!");
            }

            return false;
        } else {
            if (pass.length() < 6) {
                tilPassword.setErrorEnabled(true);
                tilPassword.setError("Password minimal 6 karakter!");

                return false;
            }

            if (!pass.equals(confPass)) {
                tilConfPassword.setErrorEnabled(true);
                tilConfPassword.setError("Konfirmasi password tidak sesuai!");

                return false;
            }

            return true;
        }
    }

    private void registerUser(final String nama, final String noTlp, final String email, String password) {
        registerLoading.show();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterAccountActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        registerLoading.dismiss();

                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registrasi gagal", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                            saveUserToDatabase(nama, noTlp, email);
                            finish();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String nama, String noTlp, String email) {
        String split[] = email.split("@");
        String userID = split[0];
        UserModel userModel = new UserModel(nama, noTlp, email, false);

        databaseReference.child(KEY_FDB_USER_PARTNER).child(userID).setValue(userModel);
    }
}
