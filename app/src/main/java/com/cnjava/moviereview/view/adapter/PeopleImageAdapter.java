package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemCastBinding;
import com.cnjava.moviereview.databinding.ItemPeopleImageBinding;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.PeopleImage;
import com.cnjava.moviereview.util.Constants;
import com.squareup.picasso.Picasso;

public class PeopleImageAdapter extends RecyclerView.Adapter<PeopleImageAdapter.MyViewHolder> {

    private Context context;
    private PeopleImage actor;
    private ImageCallBack callBack;

    public interface ImageCallBack{
        void gotoCastDetail(PeopleImage.Profile cast);
    }

    public PeopleImageAdapter(Context context, PeopleImage actor, ImageCallBack callBack) {
        this.context = context;
        this.actor = actor;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPeopleImageBinding binding = ItemPeopleImageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PeopleImage.Profile item = actor.profiles.get(position);
        /*holder.binding.tvName.setText(item.name);
        holder.binding.tvCharacter.setText(item.character);*/
        /*Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + item.profilePath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_profile2)
                .into(holder.binding.ivCast);*/
        Picasso.get().load(Constants.IMAGE_URL + item.file_path)
                .resize(300, 450)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_profile2)
                .into(holder.binding.ivCast);
        holder.binding.ivCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoCastDetail(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(actor != null){
            if(actor.profiles.size() > 10)
                return 10;
            else
                return actor.profiles.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemPeopleImageBinding binding;
        public MyViewHolder(ItemPeopleImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
