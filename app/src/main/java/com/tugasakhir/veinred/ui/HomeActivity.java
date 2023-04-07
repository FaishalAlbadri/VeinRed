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
import android.app.ProgressDialog;
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
import com.tugasakhir.veinred.adapter.MenuHomeAdapter;
import com.tugasakhir.veinred.adapter.NewsAdapter;
import com.tugasakhir.veinred.data.DataImageLocal;
import com.tugasakhir.veinred.data.DataMenu;
import com.tugasakhir.veinred.data.news.NewsItem;
import com.tugasakhir.veinred.databinding.ActivityHomeBinding;
import com.tugasakhir.veinred.di.NewsRepositoryInject;
import com.tugasakhir.veinred.presenter.news.NewsContract;
import com.tugasakhir.veinred.presenter.news.NewsPresenter;
import com.tugasakhir.veinred.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NewsContract.newsView {

    private ActivityHomeBinding binding;
    private ArrayList<DataMenu> menuArrayList = new ArrayList<>();
    private ArrayList<DataImageLocal> imageLocalArrayList = new ArrayList<>();

    private ArrayList<NewsItem> newsItemArrayList = new ArrayList<>();
    private MenuHomeAdapter menuHomeAdapter;
    private FotoHomeAdapter fotoHomeAdapter;
    private NewsAdapter newsAdapter;

    private NewsPresenter newsPresenter;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setView();
        setMenu();
        setFotoLokal();
        setNews();
    }

    private void setView() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading");
        pd.show();

        Util.refreshColor(binding.refreshNews);
        binding.refreshNews.setOnRefreshListener(() -> newsPresenter.news("5"));
    }

    private void setNews() {
        newsAdapter = new NewsAdapter(newsItemArrayList);
        binding.rvBerita.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        binding.rvBerita.setAdapter(newsAdapter);
        newsPresenter = new NewsPresenter(NewsRepositoryInject.provideToRepository(this));
        newsPresenter.onAttachView(this);
        newsPresenter.news("5");
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
        int maxFoto = 9;
        int countFoto = 0;
        imageLocalArrayList.clear();
        fotoHomeAdapter = new FotoHomeAdapter(imageLocalArrayList);
        binding.rvFoto.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvFoto.setAdapter(fotoHomeAdapter);
        fotoHomeAdapter.delete();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "veinred");
        File[] files = file.listFiles();
        Arrays.sort(files, Comparator.comparingLong(File::lastModified));
        if (files != null) {
            for (File file1 : files) {
                if (file1.getPath().endsWith(".png") || file1.getPath().endsWith(".jpg")) {
                    if (countFoto < maxFoto) {
                        imageLocalArrayList.add(new DataImageLocal(file1.getName(), file1.getPath(), file1.length()));
                        countFoto++;
                    }
                }
            }
        }

        if (imageLocalArrayList.size() > 0) {
            binding.rvFoto.setVisibility(View.VISIBLE);
            binding.txtFotoBlank.setVisibility(View.GONE);
        } else {
            binding.txtFotoBlank.setVisibility(View.VISIBLE);
            binding.rvFoto.setVisibility(View.GONE);
        }

        fotoHomeAdapter.notifyDataSetChanged();
    }

    private void setMenu() {
        menuHomeAdapter = new MenuHomeAdapter(menuArrayList);
        binding.rvMenu.setLayoutManager(new GridLayoutManager(this, 4));
        binding.rvMenu.setAdapter(menuHomeAdapter);

        menuHomeAdapter.delete();
        menuArrayList.clear();
        menuArrayList.add(new DataMenu(R.drawable.menu_kamera, "Kamera"));
        menuArrayList.add(new DataMenu(R.drawable.menu_galeri, "Galeri"));
        menuArrayList.add(new DataMenu(R.drawable.menu_berita, "Berita"));
        menuArrayList.add(new DataMenu(R.drawable.menu_profile, "Profil"));
        menuHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessNews(@NonNull List<? extends NewsItem> newsListItem, @NonNull String username, @NonNull String msg) {
        binding.txtNama.setText(username);
        binding.refreshNews.setRefreshing(false);
        pd.cancel();
        newsAdapter.delete();
        newsItemArrayList.clear();
        newsItemArrayList.addAll(newsListItem);
        newsAdapter.notifyDataSetChanged();

        if (newsItemArrayList.size() > 0) {
            binding.rvBerita.setVisibility(View.VISIBLE);
            binding.txtBeritaBlank.setVisibility(View.GONE);
        } else {
            binding.txtBeritaBlank.setVisibility(View.VISIBLE);
            binding.rvBerita.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorNews(@NonNull String username, @NonNull String msg) {
        binding.refreshNews.setRefreshing(false);
        pd.cancel();
        binding.txtNama.setText(username);
        if (msg.equals("Belum ada berita")) {
            binding.txtBeritaBlank.setVisibility(View.VISIBLE);
            binding.rvBerita.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkBefore30() {
        return ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocalImage();
    }

    private void requestBefore30() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(HomeActivity.this, "Storage permission required. Please allow this permission", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this,
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