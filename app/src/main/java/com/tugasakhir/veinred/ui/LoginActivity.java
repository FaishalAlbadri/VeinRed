package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import com.tugasakhir.veinred.data.user.UserItem;
import com.tugasakhir.veinred.databinding.ActivityLoginBinding;
import com.tugasakhir.veinred.di.LoginRepositoryInject;
import com.tugasakhir.veinred.presenter.login.LoginContract;
import com.tugasakhir.veinred.presenter.login.LoginPresenter;
import com.tugasakhir.veinred.util.session.SessionManager;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoginContract.loginView {

    private ActivityLoginBinding binding;

    private LoginPresenter loginPresenter;
    private ProgressDialog pd;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setView();

        binding.btnRegister.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class))
        );

        binding.btnShowPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else  {
                binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            if (TextUtils.isEmpty(binding.edtEmail.getText().toString())) {
                binding.edtEmail.setError("Masukkan email kamu");
            }
            if (TextUtils.isEmpty(binding.edtPassword.getText().toString())) {
                binding.edtPassword.setError("Masukkan password kamu");
            }

            if (!TextUtils.isEmpty(binding.edtEmail.getText().toString()) && !TextUtils.isEmpty(binding.edtPassword.getText().toString())) {
                pd.show();
                loginPresenter.getDataLogin(binding.edtEmail.getText().toString(),binding. edtPassword.getText().toString());
            }
        });

    }

    private void setView() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Login");
        loginPresenter = new LoginPresenter(LoginRepositoryInject.provideToRepository(this));
        loginPresenter.onAttachView(this);
        sessionManager = new SessionManager(this);
    }

    @Override
    public void onSuccessLogin(List<UserItem> userItems) {
        sessionManager.createUser(userItems.get(0).getIdUser(), binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());
        pd.dismiss();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    public void onErrorLogin(String msg) {
        pd.dismiss();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}