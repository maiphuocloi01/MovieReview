package com.cnjava.moviereview.view.adapter.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cnjava.moviereview.databinding.ItemLoadStateBinding;
import com.cnjava.moviereview.databinding.ItemSearchResultBinding;

public class SearchLoadStateAdapter extends LoadStateAdapter<SearchLoadStateAdapter.LoadStateViewHolder> {

    private View.OnClickListener mRetryCallback;

    public SearchLoadStateAdapter(View.OnClickListener mRetryCallback) {
        this.mRetryCallback = mRetryCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull LoadStateViewHolder viewHolder, @NonNull LoadState loadState) {
        viewHolder.bind(loadState);
    }

    @NonNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        ItemLoadStateBinding binding = ItemLoadStateBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new SearchLoadStateAdapter.LoadStateViewHolder(binding);
    }

    public class LoadStateViewHolder extends RecyclerView.ViewHolder {
        private ItemLoadStateBinding binding;

        public LoadStateViewHolder(ItemLoadStateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(LoadState loadState) {
            if (loadState instanceof LoadState.Error) {
                LoadState.Error loadStateError = (LoadState.Error) loadState;
                binding.errorMsg.setText(loadStateError.getError().getLocalizedMessage());
            }
            binding.progressBar.setVisibility(loadState instanceof LoadState.Loading
                    ? View.VISIBLE : View.GONE);
            binding.retryButton.setVisibility(loadState instanceof LoadState.Error
                    ? View.VISIBLE : View.GONE);
            binding.errorMsg.setVisibility(loadState instanceof LoadState.Error
                    ? View.VISIBLE : View.GONE);
        }
    }
}
