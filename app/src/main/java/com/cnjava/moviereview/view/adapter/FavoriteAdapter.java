package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemFavoriteBinding;
import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private Context context;
    private List<Favorite> favoriteList;
    private FavoriteCallBack callBack;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList, FavoriteCallBack callBack) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.callBack = callBack;
    }

    public void deleteItem(Favorite favorite) {
        if (favoriteList != null) {
            this.favoriteList.remove(favorite);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFavoriteBinding  binding = ItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Favorite favorite = favoriteList.get(position);
        holder.binding.tvName.setText(favorite.movie.title);
        holder.binding.tvRate.setText(String.format(Locale.US, "%.1f", favorite.movie.rating));
        holder.binding.tvDate.setText(NumberUtils.convertDateType3(favorite.movie.releaseDate));
        holder.binding.tvOverview.setText(favorite.movie.overview);
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + favorite.movie.poster))
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(holder.binding.ivPoster);
        holder.binding.ivUnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.deleteMovieFavorite(favorite);
            }
        });
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoMovieDetail(favorite);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(favoriteList != null){
            return favoriteList.size();
        }
        return 0;
    }

    public interface FavoriteCallBack {
        void gotoMovieDetail(Favorite favorite);
        void deleteMovieFavorite(Favorite favorite);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemFavoriteBinding binding;

        public MyViewHolder(ItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
