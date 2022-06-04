package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.databinding.ItemRecommendSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class RecommendSearchAdapter extends RecyclerView.Adapter<RecommendSearchAdapter.MyViewHolder> {

    private Context context;
    private List<String> listSearch = new ArrayList<>();
    private RecommendSearchCallBack callBack;

    public interface RecommendSearchCallBack{
        void gotoSearch(String name);
    }

    public RecommendSearchAdapter(Context context, RecommendSearchCallBack callBack) {
        this.context = context;

        this.callBack = callBack;
    }

    public void renewItems(List<String> listSearch) {
        this.listSearch = listSearch;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.listSearch.remove(position);
        notifyDataSetChanged();
    }

    public void deleteAllItem() {
        this.listSearch.clear();
        this.listSearch = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItem(String name) {
        this.listSearch.add(name);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecommendSearchBinding binding = ItemRecommendSearchBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = listSearch.get(position);
        holder.binding.tvSearch.setText(name);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoSearch(name);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listSearch != null){
            if(listSearch.size() > 4){
                return 4;
            }else {
                return listSearch.size();
            }
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemRecommendSearchBinding binding;
        public MyViewHolder(ItemRecommendSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
