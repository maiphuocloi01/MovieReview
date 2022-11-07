package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemSearchResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    private Context context;
    private Movie movie;

    private PopularAdapter.MovieCallBack callBack;

    public ResultAdapter(Context context, Movie movie, PopularAdapter.MovieCallBack callBack) {
        this.callBack = callBack;
        this.context = context;
        this.movie = movie;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie.Result item = movie.results.get(position);
        holder.binding.tvName.setText(item.title);
        holder.binding.tvRate.setText(String.valueOf(item.voteAverage));
        if(item.releaseDate != null && !item.releaseDate.equals(""))
            holder.binding.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
        if(item.overview != null) {
            holder.binding.tvOverview.setText(item.overview);
        }
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.posterPath))
                .placeholder(R.drawable.progress_animation)
                .centerCrop()
                .error(R.drawable.ic_movie)
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
        if(movie != null){
            return movie.results.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemSearchResultBinding binding;
        public MyViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
