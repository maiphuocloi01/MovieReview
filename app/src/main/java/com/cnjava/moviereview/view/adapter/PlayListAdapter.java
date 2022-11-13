package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemPlaylistBinding;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.MyViewHolder> {

    private Context context;
    private Video video;
    private PlayListCallBack callBack;
    List<CardView> cardViewList = new ArrayList<>();

    public interface PlayListCallBack {
        void gotoVideoYoutube(Video.VideoDetail videoResult);
    }

    public PlayListAdapter(Context context, Video video, PlayListCallBack callBack) {
        this.context = context;
        this.video = video;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPlaylistBinding binding = ItemPlaylistBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Video.VideoDetail item = video.results.get(position);
        if (item.site.equals("YouTube")) {
            Glide.with(context)
                    .load(String.format(Constants.IMAGE_URL_YOUTUBE + item.key + "/0.jpg"))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_movie2)
                    .into(holder.binding.ivPoster);
            holder.binding.tvName.setText(item.name);
            holder.binding.tvType.setText(item.type);
            if (!cardViewList.contains(holder.binding.cardViewPlaylist)) {
                //cardViewList.clear();
                cardViewList.add(holder.binding.cardViewPlaylist);
            }
            holder.binding.cardViewPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(CardView cardView : cardViewList){
                        cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
                    }
                    holder.binding.cardViewPlaylist.setCardBackgroundColor(ContextCompat.getColor(context, R.color.mid_black_20));
                    callBack.gotoVideoYoutube(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (video.results != null) {
            return video.results.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemPlaylistBinding binding;

        public MyViewHolder(ItemPlaylistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
