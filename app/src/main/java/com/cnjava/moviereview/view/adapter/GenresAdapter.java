package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.databinding.ItemGenresBinding;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder> {

    private Context context;
    private List<String> listGenres;

    public GenresAdapter(Context context, List<String> listGenres) {
        this.context = context;
        this.listGenres = listGenres;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGenresBinding binding = ItemGenresBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = listGenres.get(position);
        holder.binding.tvCategory.setText(item);
    }

    @Override
    public int getItemCount() {
        if (listGenres != null){
            return listGenres.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemGenresBinding binding;
        public MyViewHolder(ItemGenresBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
