package com.tugasakhir.veinred.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tugasakhir.veinred.api.Server;
import com.tugasakhir.veinred.data.news.NewsItem;
import com.tugasakhir.veinred.databinding.ItemNewsBinding;
import com.tugasakhir.veinred.ui.NewsDetailActivity;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder> {
    private List<NewsItem> listdata;
    private Context context;

    public NewsAdapter(List<NewsItem> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    @Override
    public NewsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new NewsAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterHolder holder, int position) {
        NewsItem newsItem = listdata.get(position);
        Glide.with(context)
                .load(Server.BASE_URL_IMG + "news/" + newsItem.getNewsImg())
                .centerCrop()
                .into(holder.binding.imgNews);

        holder.binding.txtJudul.setText(newsItem.getNewsJudul());
        holder.binding.txtDesc.setText(newsItem.getNewsDesc());
        holder.binding.txtDate.setText(newsItem.getNewsCreate());

        holder.binding.btnNews.setOnClickListener(v -> {
            context.startActivity(new Intent(context, NewsDetailActivity.class)
                    .putExtra("img", newsItem.getNewsImg())
                    .putExtra("judul", newsItem.getNewsJudul())
                    .putExtra("date", newsItem.getNewsCreate())
                    .putExtra("desc", newsItem.getNewsDesc())
            );
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class NewsAdapterHolder extends RecyclerView.ViewHolder {
        private final ItemNewsBinding binding;

        public NewsAdapterHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void delete() {
        int size = listdata.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listdata.remove(0);
            }
            notifyItemRangeChanged(0, size);
        }
    }
}
