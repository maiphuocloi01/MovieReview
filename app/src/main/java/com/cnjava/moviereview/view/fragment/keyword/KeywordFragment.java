package com.cnjava.moviereview.view.fragment.keyword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentSearchKeywordBinding;
import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.KeywordAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.searchresult.SearchResultFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class KeywordFragment extends BaseFragment<FragmentSearchKeywordBinding, CommonViewModel> implements KeywordAdapter.KeywordCallBack {

    public static final String TAG = KeywordFragment.class.getName();
    private Object mData;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        String name = (String) mData;
        MyApplication.getInstance().getStorage().fragmentTag = TAG;

        Log.d(TAG, "initViews: " + name);
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_popup_exit));
                callBack.backToPrev();
            }
        });

        viewModel.searchKeyword(name);
    }

    @Override
    protected FragmentSearchKeywordBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchKeywordBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_SEARCH_KEYWORD)) {
            Keyword keyword = (Keyword) data;
            Log.d(TAG, "apiSuccess: " + keyword.results.size());
            KeywordAdapter resultAdapter = new KeywordAdapter(context, keyword, this);
            binding.rvKeyword.setAdapter(resultAdapter);
            //MyApplication.getInstance().getStorage().movieSearch = movie;
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {
            Log.d(TAG, "apiError: " + data.toString());
            Toast.makeText(context, "Unable connect to server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void gotoSearchByKeywordId(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt("keyword", id);
        callBack.replaceFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }

}
