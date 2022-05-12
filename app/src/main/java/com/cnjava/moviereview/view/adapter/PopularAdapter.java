package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.databinding.ItemPopularMovieBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    private Context context;
    private Movie movie;

    public PopularAdapter(Context context, Movie movie) {
        this.context = context;
        this.movie = movie;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPopularMovieBinding binding = ItemPopularMovieBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie.Result item = movie.results.get(position);
        Log.d("adapter", "onBindViewHolder: " + item.title + item.backdropPath);
        Glide.with(context).load(String.format(Constants.IMAGE_URL + item.backdropPath)).into(holder.binding.ivCover);
        holder.binding.tvName.setText(item.title);
    }

    @Override
    public int getItemCount() {
        if (movie != null) {
            return movie.results.size();
        }else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemPopularMovieBinding binding;
        public MyViewHolder(ItemPopularMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
