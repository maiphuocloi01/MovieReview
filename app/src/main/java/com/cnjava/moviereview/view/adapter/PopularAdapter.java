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
import com.cnjava.moviereview.databinding.ItemPopularMovieBinding;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    private Context context;
    private Movie movie;
    private List<Genres> genresStorage = MyApplication.getInstance().getStorage().genresList;

    private MovieCallBack callBack;

    public PopularAdapter(Context context, Movie movie, MovieCallBack callBack) {
        this.callBack = callBack;
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
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.backdropPath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.ivCover);
        holder.binding.tvName.setText(item.title);
        List<String> listGenres = new ArrayList<>();
        StringBuilder sb = new StringBuilder("");
        if (item.genreIds.size() > 3) {
            for (int i = 0; i < 3; i++) {
                int id = item.genreIds.get(i);
                Genres genres1 = genresStorage.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
                sb.append(genres1.getName()).append(", ");
            }
        } else {
            for (int i = 0; i < item.genreIds.size(); i++) {
                int id = item.genreIds.get(i);
                Genres genres1 = genresStorage.stream().filter(a -> a.getId() == id).collect(Collectors.toList()).get(0);
                sb.append(genres1.getName()).append(", ");
            }
        }
        holder.binding.tvKeyword.setText(sb.substring(0, sb.length()-2));
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
        } else {
            return 0;
        }
    }

    public interface MovieCallBack {
        void gotoMovieDetail(int id);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemPopularMovieBinding binding;

        public MyViewHolder(ItemPopularMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
