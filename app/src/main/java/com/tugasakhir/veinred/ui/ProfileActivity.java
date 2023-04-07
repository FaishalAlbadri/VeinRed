package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tugasakhir.veinred.R;
import com.tugasakhir.veinred.api.Server;
import com.tugasakhir.veinred.data.user.UserItem;
import com.tugasakhir.veinred.databinding.ActivityProfileBinding;
import com.tugasakhir.veinred.di.LoginRepositoryInject;
import com.tugasakhir.veinred.presenter.login.LoginContract;
import com.tugasakhir.veinred.presenter.login.LoginPresenter;
import com.tugasakhir.veinred.util.BorderTransformation;
import com.tugasakhir.veinred.util.session.SessionUser;
import com.tugasakhir.veinred.util.session.User;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements LoginContract.loginView {

    private ActivityProfileBinding binding;
    private LoginPresenter loginPresenter;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setView();

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnLogout.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(ProfileActivity.this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog);
            alertDialogBuilder.setMessage("Apakah yakin ingin keluar dari akun?");
            alertDialogBuilder.setPositiveButton("Iya", (dialog, which) -> {
                SessionUser.getInstance(this).logout();
            });

            alertDialogBuilder.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();
        });
    }

    private void setView() {
        SessionUser.getInstance(this).setDataUser();
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading");
        pd.show();
        loginPresenter = new LoginPresenter(LoginRepositoryInject.provideToRepository(this));
        loginPresenter.onAttachView(this);
        loginPresenter.getDataLogin(User.getInstance().getUser_email(), User.getInstance().getUser_password());
    }

    @Override
    public void onSuccessLogin(List<UserItem> userItems) {
        UserItem data = userItems.get(0);
        Glide.with(this).load(Server.BASE_URL_IMG + "user/" + data.getUserImage()).transform(new BorderTransformation()).into(binding.imgProfile);
        binding.txtNameValue.setText(data.getUserName());
        binding.txtEmailValue.setText(data.getUserEmail());
        binding.txtCreateValue.setText(data.getUserCreate());
        pd.dismiss();
    }

    @Override
    public void onErrorLogin(String msg) {
        pd.dismiss();
        if (msg.equals("Password atau email salah, bisa juga belum terdaftar")) {
            SessionUser.getInstance(this).logout();
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}