package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tugasakhir.veinred.R;
import com.tugasakhir.veinred.databinding.ActivityImageViewerBinding;

import java.io.File;

public class ImageViewerActivity extends AppCompatActivity {

    private ActivityImageViewerBinding binding;
    private String path = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            binding.txtTitle.setText(intent.getStringExtra("title"));
            Glide.with(ImageViewerActivity.this).load(intent.getStringExtra("image")).centerCrop().into(binding.img);
            path = intent.getStringExtra("image");
        }

        String finalPath = path;
        binding.btnShare.setOnClickListener(v -> new ShareCompat.IntentBuilder(ImageViewerActivity.this).setStream(Uri.parse(finalPath)).setType("image/*").setChooserTitle("Share Image").startChooser());

        binding.btnDelete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(ImageViewerActivity.this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog);
            alertDialogBuilder.setMessage("Are you sure you want to delete this image ?");
            alertDialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                String[] projection = new String[]{MediaStore.Images.Media._ID};
                String selection = MediaStore.Images.Media.DATA + " = ?";
                String[] selectionArgs = new String[]{new File(finalPath).getAbsolutePath()};
                Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
                if (cursor.moveToFirst()) {
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    try {
                        contentResolver.delete(deleteUri, null, null);
                        boolean delete1 = new File(finalPath).delete();
                        Log.e("TAG", delete1 + "");
                        Toast.makeText(ImageViewerActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ImageViewerActivity.this, "Error Deleting Video", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ImageViewerActivity.this, "File Not Find", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            });

            alertDialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();
        });

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}