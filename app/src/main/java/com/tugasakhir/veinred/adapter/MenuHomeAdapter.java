package com.tugasakhir.veinred.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tugasakhir.veinred.data.DataMenu;
import com.tugasakhir.veinred.databinding.ItemMenuBinding;

import java.util.List;

public class MenuHomeAdapter extends RecyclerView.Adapter<MenuHomeAdapter.MenuHomeAdapterHolder> {

    private List<DataMenu> listData;

    public MenuHomeAdapter(List<DataMenu> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public MenuHomeAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMenuBinding binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MenuHomeAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHomeAdapterHolder holder, int position) {
        DataMenu dataMenu = listData.get(position);
        holder.binding.txtMenu.setText(dataMenu.getTitle());
        Glide.with(holder.binding.imgMenu)
                .load(dataMenu.getImage())
                .into(holder.binding.imgMenu);

        holder.binding.layoutMenu.setOnClickListener(v -> {
            if (dataMenu.getTitle().equals("Kamera")) {

            } else if (dataMenu.getTitle().equals("Galeri")) {

            } else if (dataMenu.getTitle().equals("Berita")) {

            } else {

            }
            Toast.makeText(v.getContext(), dataMenu.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MenuHomeAdapterHolder extends RecyclerView.ViewHolder {
        private final ItemMenuBinding binding;

        public MenuHomeAdapterHolder(ItemMenuBinding binding) {
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
