package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.databinding.ItemReviewBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;

import java.util.List;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context context;
    private List<Review> listReview;

    public ReviewAdapter(Context context, List<Review> listReview) {
        this.context = context;
        this.listReview = listReview;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Review item = listReview.get(position);

        holder.binding.tvName.setText(item.author);
        holder.binding.tvDate.setText(item.createdAt);
        holder.binding.tvContent.setText(item.content);
        holder.binding.tvRate.setText(String.valueOf(item.rating));
        Glide.with(context)
                .load(item.avatarPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.ivAvt);
        holder.binding.tvContent.setOnStateChangeListener(new ExpandableTextView.OnStateChangeListener() {
            @Override
            public void onStateChange(boolean isShrink) {
                Review contentItem = listReview.get(holder.getAdapterPosition());
                contentItem.isShrink = isShrink;
                listReview.set(holder.getAdapterPosition(), contentItem);
            }
        });
        holder.binding.tvContent.setText(item.content);
        holder.binding.tvContent.resetState(item.isShrink);

    }

    @Override
    public int getItemCount() {
        if(listReview != null){
            return listReview.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemReviewBinding binding;
        public MyViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
