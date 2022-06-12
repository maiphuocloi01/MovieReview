package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.ItemOnboardBinding;
import com.cnjava.moviereview.databinding.ItemPopularMovieBinding;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OnboardAdapter extends RecyclerView.Adapter<OnboardAdapter.MyViewHolder> {

    private Context context;
    private List<Genres> listOnboard;


    public OnboardAdapter(Context context, List<Genres> listOnboard) {
        this.context = context;
        this.listOnboard = listOnboard;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOnboardBinding binding = ItemOnboardBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Genres item = listOnboard.get(position);
        holder.binding.lottie.setAnimation(item.getId());
        holder.binding.tvTitleOnboard.setText(item.getName());
        holder.binding.tvDescriptionOnboard.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        if (listOnboard != null) {
            return listOnboard.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemOnboardBinding binding;

        public MyViewHolder(ItemOnboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
