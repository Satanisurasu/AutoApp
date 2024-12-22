package com.example.autoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AutoAdapter extends RecyclerView.Adapter<AutoAdapter.AutoViewHolder> {

    private List<Auto> autoList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Auto auto, int position);
    }

    public AutoAdapter(List<Auto> autoList, OnItemClickListener onItemClickListener) {
        this.autoList = autoList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto, parent, false);
        return new AutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AutoViewHolder holder, int position) {
        Auto auto = autoList.get(position);

        // Устанавливаем текстовые значения
        holder.producerTextView.setText(auto.getProducer());
        holder.modelTextView.setText(auto.getModel());
        holder.priceTextView.setText(String.valueOf(auto.getPrice()));

        // Загружаем изображение через Glide
        Glide.with(holder.itemView.getContext())
                .load(auto.getImageUrl())
                .into(holder.carImageView);

        // Устанавливаем обработчик клика на элемент списка
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(auto, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return autoList.size();
    }

    public void updateList(List<Auto> newAutoList) {
        autoList.addAll(newAutoList); // Добавляем новые данные
        notifyDataSetChanged(); // Уведомляем адаптер об изменениях
    }


    public static class AutoViewHolder extends RecyclerView.ViewHolder {
        ImageView carImageView;
        TextView producerTextView, modelTextView, priceTextView;

        public AutoViewHolder(@NonNull View itemView) {
            super(itemView);

            carImageView = itemView.findViewById(R.id.carImageView);
            producerTextView = itemView.findViewById(R.id.producerText);
            modelTextView = itemView.findViewById(R.id.modelText);
            priceTextView = itemView.findViewById(R.id.priceText);
        }
    }
}
