package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.databinding.ItemMovieBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private Movie movie;

    public MovieAdapter(Context context, Movie movie) {
        this.context = context;
        this.movie = movie;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie.Result item = movie.results.get(position);
        holder.binding.tvDate.setText(item.releaseDate);
        holder.binding.tvName.setText(item.title);
        Glide.with(context).load(String.format(Constants.IMAGE_URL + item.posterPath)).into(holder.binding.ivPoster);
    }

    @Override
    public int getItemCount() {
        if (movie != null){
            return movie.results.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemMovieBinding binding;
        public MyViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
