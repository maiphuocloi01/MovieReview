package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.databinding.ItemCategoryBinding;
import com.cnjava.moviereview.databinding.ItemKeywordBinding;
import com.cnjava.moviereview.model.Keyword;

public class KeywordAdapter extends RecyclerView.Adapter<KeywordAdapter.MyViewHolder> {

    private Context context;
    private Keyword keywordList;
    private KeywordCallBack callBack;

    public KeywordAdapter(Context context, Keyword keywordList, KeywordCallBack callBack) {
        this.context = context;
        this.keywordList = keywordList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemKeywordBinding binding = ItemKeywordBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Keyword.KeywordDetail item = keywordList.results.get(position);
        Log.d("TAG", "Helloae: " + item.name);
        holder.binding.tvCategory.setText(item.name);
        holder.binding.bgKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.bgKeyword.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.gotoSearchByKeywordId(item.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (keywordList.results != null) {
            return keywordList.results.size();
        }
        return 0;
    }

    public interface KeywordCallBack {
        void gotoSearchByKeywordId(int id);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemKeywordBinding binding;
        public MyViewHolder(ItemKeywordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
