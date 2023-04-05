package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import com.tugasakhir.veinred.databinding.ActivityRegisterBinding;
import com.tugasakhir.veinred.di.RegisterRepositoryInject;
import com.tugasakhir.veinred.presenter.register.RegisterContract;
import com.tugasakhir.veinred.presenter.register.RegisterPresenter;

public class RegisterActivity extends AppCompatActivity implements RegisterContract.registerView {

    ActivityRegisterBinding binding;
    private RegisterPresenter registerPresenter;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setView();
        binding.btnLogin.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), LoginActivity.class))
        );

        binding.btnShowPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.edtRepassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else  {
                binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.edtRepassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.edtName.getText().toString())) {
                binding.edtName.setError("Masukkan nama kamu");
            }
            if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                binding.edtEmail.setError("Masukkan email kamu");
            }
            if (TextUtils.isEmpty(binding.edtPassword.getText().toString())) {
                binding.edtPassword.setError("Masukkan password kamu");
            }
            if (TextUtils.isEmpty(binding.edtRepassword.getText().toString())) {
                binding.edtRepassword.setError("Masukkan Ulang Password kamu");
            } else {
                if (binding.edtPassword.getText().toString().equals(binding.edtRepassword.getText().toString())) {
                    pd.show();
                    registerPresenter.getDataRegister(binding.edtName.getText().toString(), binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "Password kamu tidak cocok", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setView() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Daftar Akun");
        registerPresenter = new RegisterPresenter(RegisterRepositoryInject.provideToRepository(this));
        registerPresenter.onAttachView(this);
    }

    @Override
    public void onSuccessRegister() {
        pd.dismiss();
        Toast.makeText(this, "Berhasil daftar akun", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public void onErrorRegister(String msg) {
        pd.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}