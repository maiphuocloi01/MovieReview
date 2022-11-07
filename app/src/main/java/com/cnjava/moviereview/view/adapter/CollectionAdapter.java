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
import com.cnjava.moviereview.databinding.ItemMovieBinding;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    private Context context;
    private Collection collection;

    private PopularAdapter.MovieCallBack callBack;

    public CollectionAdapter(Context context, Collection collection, PopularAdapter.MovieCallBack callBack) {
        this.context = context;
        this.collection = collection;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CollectionAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Collection.Part item = collection.parts.get(position);
        holder.binding.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
        holder.binding.tvName.setText(item.title);
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.posterPath))
                .transition(DrawableTransitionOptions.withCrossFade())
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
        if (collection != null){
            return collection.parts.size();
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
