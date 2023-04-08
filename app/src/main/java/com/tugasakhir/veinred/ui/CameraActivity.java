package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tugasakhir.veinred.databinding.ActivityCameraBinding;
import com.tugasakhir.veinred.databinding.ActivityHomeBinding;

public class CameraActivity extends AppCompatActivity {

    private ActivityCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}