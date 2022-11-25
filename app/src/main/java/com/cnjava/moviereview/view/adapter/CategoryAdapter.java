package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.databinding.ItemCategoryBinding;
import com.cnjava.moviereview.databinding.ItemGenresBinding;
import com.cnjava.moviereview.model.Genres;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<Genres> genresList;
    private CategoryCallBack callBack;

    public interface CategoryCallBack{
        void gotoSearchByCategory(String id, String name);
    }

    public CategoryAdapter(Context context, List<Genres> genresList, CategoryCallBack callBack) {
        this.context = context;
        this.genresList = genresList;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Genres item = genresList.get(position);
        holder.binding.btCategoryItem.setText(item.getName());
        holder.binding.btCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoSearchByCategory(String.valueOf(item.getId()), item.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(genresList != null){
            return genresList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemCategoryBinding binding;
        public MyViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
