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
import com.cnjava.moviereview.databinding.ItemCastBinding;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.util.Constants;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    private Context context;
    private Actor actor;

    public CastAdapter(Context context, Actor actor) {
        this.context = context;
        this.actor = actor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCastBinding binding = ItemCastBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Actor.Cast item = actor.cast.get(position);
        holder.binding.tvName.setText(item.name);
        holder.binding.tvCharacter.setText(item.character);
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.profilePath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_profile)
                .into(holder.binding.ivCast);
    }

    @Override
    public int getItemCount() {
        if(actor != null){
            if(actor.cast.size() > 10)
                return 10;
            else
                return actor.cast.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemCastBinding binding;
        public MyViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
