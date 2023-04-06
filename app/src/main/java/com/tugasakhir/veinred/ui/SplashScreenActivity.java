package com.tugasakhir.veinred.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.tugasakhir.veinred.R;
import com.tugasakhir.veinred.base.BaseActivityFullScreen;
import com.tugasakhir.veinred.util.session.SessionUser;
import com.tugasakhir.veinred.util.session.User;

import java.io.File;

public class SplashScreenActivity extends BaseActivityFullScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SessionUser.getInstance(this).setDataUser();

        try {
            File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "veinred");
            if (!root.exists()) {
                root.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if (User.getInstance().getUser_email() == null && User.getInstance().getUser_password() == null) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        }, 3000);
    }
}