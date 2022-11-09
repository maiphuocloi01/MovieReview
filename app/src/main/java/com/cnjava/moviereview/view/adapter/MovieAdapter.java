package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemMovieBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

import java.util.Locale;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context context;
    private Movie movie;

    private PopularAdapter.MovieCallBack callBack;

    public MovieAdapter(Context context, Movie movie, PopularAdapter.MovieCallBack callBack) {
        this.callBack = callBack;
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
        holder.binding.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
        holder.binding.tvName.setText(item.title);
        holder.binding.tvStar.setText(String.format(Locale.US, "%.1f", item.voteAverage));
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.posterPath))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(holder.binding.ivPoster);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoMovieDetail(item.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movie != null) {
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
