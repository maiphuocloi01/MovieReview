package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.databinding.ItemTrendingSearchBinding;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> {

    private Context context;
    private List<String> listTrending;
    private TrendingCallBack callBack;

    public interface TrendingCallBack{
        void selectTrending(String name);
    }

    public TrendingAdapter(Context context, List<String> listTrending, TrendingCallBack callBack) {
        this.context = context;
        this.listTrending = listTrending;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrendingSearchBinding binding = ItemTrendingSearchBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String item = listTrending.get(position);
        holder.binding.tvSearch.setText(item);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.selectTrending(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listTrending != null){
            return listTrending.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemTrendingSearchBinding binding;
        public MyViewHolder(ItemTrendingSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
