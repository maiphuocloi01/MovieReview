package com.cnjava.moviereview.view.adapter.paging;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemSearchResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

public class SearchMovieAdapter extends PagingDataAdapter<Movie, SearchMovieAdapter.MovieViewHolder> {

    private Context context;
    private OnItemClickListener mListener;
    private Movie movie;

    public interface OnItemClickListener {
        void onItemClick(Movie.Result movieResult);
    }

    public SearchMovieAdapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback, Context context, OnItemClickListener callBack) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.mListener = callBack;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SearchMovieAdapter.MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie.Result item = movie.results.get(position);
        holder.binding.tvName.setText(item.title);
        holder.binding.tvRate.setText(String.valueOf(item.voteAverage));
        if (item.releaseDate != null && !item.releaseDate.equals(""))
            holder.binding.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
        if (item.overview != null) {
            holder.binding.tvOverview.setText(item.overview);
        }
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.posterPath))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(holder.binding.ivPoster);

        holder.binding.getRoot().setOnClickListener(view -> mListener.onItemClick(item));
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private ItemSearchResultBinding binding;

        public MovieViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.page == newItem.page;
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.equals(newItem);
                }
            };

}
