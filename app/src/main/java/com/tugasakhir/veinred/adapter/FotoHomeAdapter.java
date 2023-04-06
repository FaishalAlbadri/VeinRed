package com.tugasakhir.veinred.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tugasakhir.veinred.data.DataImageLocal;
import com.tugasakhir.veinred.databinding.ItemFotoBinding;
import com.tugasakhir.veinred.ui.ImageViewerActivity;

import java.text.DecimalFormat;
import java.util.List;

public class FotoHomeAdapter extends RecyclerView.Adapter<FotoHomeAdapter.FotoHomeAdapterHolder> {

    private List<DataImageLocal> listData;
    private Context context;

    public FotoHomeAdapter(List<DataImageLocal> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public FotoHomeAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFotoBinding binding = ItemFotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new FotoHomeAdapterHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoHomeAdapterHolder holder, int position) {
        DataImageLocal dataImageLocal = listData.get(position);
        holder.binding.txtTitle.setText(dataImageLocal.getTitle());
        holder.binding.txtSize.setText("Size: " + getSize(dataImageLocal.getSize()));
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
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class FotoHomeAdapterHolder extends RecyclerView.ViewHolder {
        private ItemFotoBinding binding;

        public FotoHomeAdapterHolder(ItemFotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static String getSize(long size) {
        if (size <= 0) {
            return "0";
        }

        double d = (double) size;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        StringBuilder stringBuilder = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#");
        double power = Math.pow(1024.0d, log10);
        stringBuilder.append(decimalFormat.format(d / power));
        stringBuilder.append(" ");
        stringBuilder.append(new String[]{"B", "KB", "MB", "GB", "TB"}[log10]);
        return stringBuilder.toString();
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
