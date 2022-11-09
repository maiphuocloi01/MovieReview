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
import com.cnjava.moviereview.databinding.ItemVideoBinding;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.util.Constants;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {

    private Context context;
    private Video video;
    private VideoCallBack callBack;

    public interface VideoCallBack{
        void gotoVideoYoutube(String key);
    }

    public VideoAdapter(Context context, Video video, VideoCallBack callBack) {
        this.context = context;
        this.video = video;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVideoBinding binding = ItemVideoBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Video.VideoDetail item = video.results.get(position);
        if(item.site.equals("YouTube")){
            Glide.with(context)
                    .load(String.format(Constants.IMAGE_URL_YOUTUBE + item.key + "/0.jpg"))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_movie2)
                    .into(holder.binding.ivPoster);
            holder.binding.tvName.setText(item.name);
            holder.binding.tvType.setText(item.type);
            holder.binding.ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.gotoVideoYoutube(item.key);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(video.results != null){
            if (video.results.size() > 5) {
                return 5;
            } else {
                return video.results.size();
            }
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemVideoBinding binding;
        public MyViewHolder(ItemVideoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
