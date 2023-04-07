package com.tugasakhir.veinred.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.tugasakhir.veinred.adapter.NewsAdapter;
import com.tugasakhir.veinred.data.news.NewsItem;
import com.tugasakhir.veinred.databinding.ActivityNewsBinding;
import com.tugasakhir.veinred.databinding.ActivityNewsDetailBinding;
import com.tugasakhir.veinred.di.NewsRepositoryInject;
import com.tugasakhir.veinred.presenter.news.NewsContract;
import com.tugasakhir.veinred.presenter.news.NewsPresenter;
import com.tugasakhir.veinred.util.Util;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsContract.newsView {

    private ActivityNewsBinding binding;
    private ArrayList<NewsItem> newsItemArrayList = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private NewsPresenter newsPresenter;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setView();

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void setView() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Loading");
        pd.show();

        newsAdapter = new NewsAdapter(newsItemArrayList);
        binding.rvNews.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        binding.rvNews.setAdapter(newsAdapter);
        newsPresenter = new NewsPresenter(NewsRepositoryInject.provideToRepository(this));
        newsPresenter.onAttachView(this);
        newsPresenter.news("all");

        Util.refreshColor(binding.refreshNews);
        binding.refreshNews.setOnRefreshListener(() -> newsPresenter.news("all"));
    }

    @Override
    public void onSuccessNews(@NonNull List<? extends NewsItem> newsListItem, @NonNull String username, @NonNull String msg) {
        binding.refreshNews.setRefreshing(false);
        pd.cancel();
        newsAdapter.delete();
        newsItemArrayList.clear();
        newsItemArrayList.addAll(newsListItem);
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorNews(@NonNull String msg) {
        binding.refreshNews.setRefreshing(false);
        pd.cancel();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}