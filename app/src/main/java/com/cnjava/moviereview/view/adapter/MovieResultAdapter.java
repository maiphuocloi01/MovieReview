package com.cnjava.moviereview.view.adapter;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType3;
import static com.cnjava.moviereview.view.adapter.paging.SearchMovieAdapter.DIFF_CALLBACK;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemMovieLayout1Binding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MovieResultAdapter extends PagingDataAdapter<Movie.Result, MovieResultAdapter.GridLayoutViewHolder> {

    private Context context;
    public static final int VIEW_TYPE_ITEM = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    private int VIEW_TYPE = VIEW_TYPE_ITEM;

    public interface MovieResultCallBack {
        void gotoMovieDetail(int id);
    }

    private MovieResultCallBack callBack;

    public MovieResultAdapter(Context context, MovieResultCallBack callBack) {
        super(DIFF_CALLBACK);
        this.callBack = callBack;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieResultAdapter.GridLayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieLayout1Binding binding = ItemMovieLayout1Binding.inflate(LayoutInflater.from(context), parent, false);
        return new MovieResultAdapter.GridLayoutViewHolder(binding);
    }

    public void changeLayout(int viewType) {
        this.VIEW_TYPE = viewType;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MovieResultAdapter.GridLayoutViewHolder holder, int position) {
        Movie.Result item = getItem(position);
        if (item != null) {
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class GridLayoutViewHolder extends RecyclerView.ViewHolder {
        private ItemMovieLayout1Binding binding;

        public GridLayoutViewHolder(ItemMovieLayout1Binding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Movie.Result item) {
            if (item.releaseDate == null || item.releaseDate.equals("")) {
                binding.tvDate.setText(context.getString(R.string.coming_soon));
            } else {
                binding.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
            }

            binding.tvName.setText(item.title);
            binding.tvStar.setText(String.format(Locale.US, "%.1f", item.voteAverage));
            Picasso.get().load(Constants.IMAGE_URL + item.posterPath)
                    .resize(256, 384)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_movie2)
                    .into(binding.ivPoster);
            binding.getRoot().setOnClickListener(view -> callBack.gotoMovieDetail(item.id));
        }

       /* public void bind2(@NonNull Movie.Result item) {
            //binding2.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
            binding2.tvName.setText(item.title);
            binding2.tvRate.setText(String.format(Locale.US, "%.1f", item.voteAverage));
            Picasso.get().load(Constants.IMAGE_URL + item.posterPath)
                    .resize(256, 384)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_movie2)
                    .into(binding2.ivPoster);
            binding2.getRoot().setOnClickListener(view -> callBack.gotoMovieDetail(item.id));
        }*/
    }
}