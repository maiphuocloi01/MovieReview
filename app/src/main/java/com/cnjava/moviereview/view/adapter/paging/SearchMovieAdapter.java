package com.cnjava.moviereview.view.adapter.paging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemSearchResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.squareup.picasso.Picasso;

public class SearchMovieAdapter extends PagingDataAdapter<Movie.Result, SearchMovieAdapter.MovieViewHolder> {

    private Context context;
    private OnItemClickListener mListener;
    //private List<Movie.Result> results = new ArrayList<>();


    public interface OnItemClickListener {
        void onItemClick(Movie.Result movieResult);
    }

    public SearchMovieAdapter(Context context, OnItemClickListener callBack) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.mListener = callBack;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie.Result item = getItem(position);
        if (item != null) {
            holder.bind(item);
        }
    }

    /*private void setData(List<Movie.Result> newData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallBack(newData, results));
        diffResult.dispatchUpdatesTo(this);
        results.clear();
        this.results.addAll(newData);
    }*/

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchResultBinding binding;

        public MovieViewHolder(ItemSearchResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull Movie.Result item) {
            binding.tvName.setText(item.title);
            binding.tvRate.setText(String.valueOf(item.voteAverage));
            if (item.releaseDate != null && !item.releaseDate.equals("")) {
                binding.tvDate.setText(NumberUtils.convertDateType3(item.releaseDate));
            } else {
                binding.tvDate.setText(context.getString(R.string.coming_soon));
            }
            if (item.overview != null) {
                binding.tvOverview.setText(item.overview);
            }
            Picasso.get().load(Constants.IMAGE_URL + item.posterPath)
                    .resize(256, 384)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_movie2)
                    .centerCrop().into(binding.ivPoster);

            binding.getRoot().setOnClickListener(view -> mListener.onItemClick(item));
        }

    }

    /*public class MyDiffUtilCallBack extends DiffUtil.Callback {
        List<Movie.Result> newList;
        List<Movie.Result> oldList;

        public MyDiffUtilCallBack(List<Movie.Result> newList, List<Movie.Result> oldList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition).id == oldList.get(oldItemPosition).id;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return newList.get(newItemPosition).title.equals(oldList.get(oldItemPosition).title);

        }
    }*/

    public static DiffUtil.ItemCallback<Movie.Result> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie.Result>() {
                @Override
                public boolean areItemsTheSame(Movie.Result oldItem, Movie.Result newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(Movie.Result oldItem, @NonNull Movie.Result newItem) {
                    return oldItem.title.equals(newItem.title);
                }
            };


}
