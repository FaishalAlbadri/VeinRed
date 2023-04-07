package com.tugasakhir.veinred.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tugasakhir.veinred.data.DataImageLocal;
import com.tugasakhir.veinred.databinding.ItemGalleryBinding;
import com.tugasakhir.veinred.ui.GalleryActivity;
import com.tugasakhir.veinred.ui.ImageViewerActivity;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryAdapterHolder> {

    private List<DataImageLocal> listData;
    private Context context;

    public GalleryAdapter(List<DataImageLocal> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public GalleryAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGalleryBinding binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new GalleryAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapterHolder holder, int position) {
        DataImageLocal dataImageLocal = listData.get(position);
        Glide.with(holder.binding.img)
                .load(dataImageLocal.getPath())
                .centerCrop()
                .into(holder.binding.img);

        holder.binding.layout.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ImageViewerActivity.class)
                    .putExtra("image", dataImageLocal.getPath())
                    .putExtra("title", dataImageLocal.getTitle())
            );
        });

        holder.binding.layout.setOnLongClickListener(v -> {
            Snackbar.make(((GalleryActivity) context).binding.parentlayout, dataImageLocal.getTitle(), Snackbar.LENGTH_LONG).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class GalleryAdapterHolder extends RecyclerView.ViewHolder {

        private ItemGalleryBinding binding;

        public GalleryAdapterHolder(@NonNull ItemGalleryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void delete() {
        int size = listData.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                listData.remove(0);
            }
            notifyItemRangeChanged(0, size);
        }
    }
}
