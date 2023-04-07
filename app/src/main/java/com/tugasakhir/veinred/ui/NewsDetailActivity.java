package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.tugasakhir.veinred.api.Server;
import com.tugasakhir.veinred.databinding.ActivityNewsDetailBinding;

public class NewsDetailActivity extends AppCompatActivity {

    private ActivityNewsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setData();

    }

    private void setData() {
        Glide.with(binding.imgNews)
                .load(Server.BASE_URL_IMG + "news/" + getIntent().getStringExtra("img"))
                .centerCrop()
                .into(binding.imgNews);

        binding.txtJudul.setText(getIntent().getStringExtra("judul"));
        binding.txtDesc.setText(getIntent().getStringExtra("desc"));
        binding.txtDate.setText(getIntent().getStringExtra("date"));

        binding.btnBack.setOnClickListener(v ->{
            finish();
        });
    }
}