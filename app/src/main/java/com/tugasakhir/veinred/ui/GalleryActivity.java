package com.tugasakhir.veinred.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.tugasakhir.veinred.R;
import com.tugasakhir.veinred.adapter.FotoHomeAdapter;
import com.tugasakhir.veinred.adapter.GalleryAdapter;
import com.tugasakhir.veinred.data.DataImageLocal;
import com.tugasakhir.veinred.databinding.ActivityGalleryBinding;
import com.tugasakhir.veinred.databinding.ActivityNewsBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GalleryActivity extends AppCompatActivity {

    public ActivityGalleryBinding binding;
    private GalleryAdapter galleryAdapter;
    private ArrayList<DataImageLocal> imageLocalArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        setFotoLokal();
    }

    private void setFotoLokal() {
        if (Build.VERSION.SDK_INT < 30) {
            if (!checkBefore30()) {
                requestBefore30();
            } else {
                setLocalImage();
            }
        } else if (Build.VERSION.SDK_INT >= 30) {
            check30AndAfter();
        } else {
            setLocalImage();
        }
    }


    private void setLocalImage() {
        imageLocalArrayList.clear();
        galleryAdapter = new GalleryAdapter(imageLocalArrayList);
        binding.rvGallery.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvGallery.setAdapter(galleryAdapter);
        galleryAdapter.delete();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "veinred");
        File[] files = file.listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        if (files != null) {
            for (File file1 : files) {
                if (file1.getPath().endsWith(".png") || file1.getPath().endsWith(".jpg")) {
                    imageLocalArrayList.add(new DataImageLocal(file1.getName(), file1.getPath(), file1.length()));
                }
            }
        }

        galleryAdapter.notifyDataSetChanged();
    }

    private boolean checkBefore30() {
        return ContextCompat.checkSelfPermission(GalleryActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocalImage();
    }

    private void requestBefore30() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(GalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(GalleryActivity.this, "Storage permission required. Please allow this permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(GalleryActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            ActivityCompat.requestPermissions(GalleryActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocalImage();
                } else {
                    if (Build.VERSION.SDK_INT < 30) {
                        if (!checkBefore30()) {
                            requestBefore30();
                        } else {
                            setLocalImage();
                        }
                    } else if (Build.VERSION.SDK_INT >= 30) {
                        check30AndAfter();
                    } else {
                        setLocalImage();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void check30AndAfter() {
        if (!Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, 200);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 200);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (30 >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    setLocalImage();
                } else {
                    if (Build.VERSION.SDK_INT < 30) {
                        if (!checkBefore30()) {
                            requestBefore30();
                        } else {
                            setLocalImage();
                        }
                    } else if (Build.VERSION.SDK_INT >= 30) {
                        check30AndAfter();
                    } else {
                        setLocalImage();
                    }
                }
            }
        }
    }
}