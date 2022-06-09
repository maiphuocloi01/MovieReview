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
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemReviewBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

import java.util.List;

import ru.embersoft.expandabletextview.ExpandableTextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context context;
    private List<Review> listReview;
    private ReviewCallBack callBack;

    public interface ReviewCallBack{
        void gotoReviewDetail(Review review);
    }

    public ReviewAdapter(Context context, List<Review> listReview, ReviewCallBack callBack) {
        this.context = context;
        this.listReview = listReview;
        this.callBack = callBack;
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
        holder.binding.tvName.setText(item.user.getName());
        holder.binding.tvDate.setText(NumberUtils.convertDateType7(item.createdAt));
        holder.binding.tvContent.setText(item.content);
        holder.binding.tvRate.setText(String.valueOf((int) item.rating));
        Glide.with(context)
                .load(item.user.getAvatar())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_default_avt)
                .into(holder.binding.ivAvt);
        if(item.like != null) {
            Log.d("TAG", "tvLike: ");
            holder.binding.tvLike.setText(String.valueOf(item.like.size()));
        }
        if(item.dislike != null) {
            Log.d("TAG", "tvDislike: ");
            holder.binding.tvDislike.setText(String.valueOf(item.dislike.size()));
        }
        Log.d("TAG", "onBindViewHolder: " + item.like.size());

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
