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
    private CastCallBack callBack;

    public interface CastCallBack{
        void gotoCastDetail(String id);
    }

    public CastAdapter(Context context, Actor actor, CastCallBack callBack) {
        this.context = context;
        this.actor = actor;
        this.callBack = callBack;
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
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_profile2)
                .into(holder.binding.ivCast);
        holder.binding.ivCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoCastDetail(item.creditId);
            }
        });
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
