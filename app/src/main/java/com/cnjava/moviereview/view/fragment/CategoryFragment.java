package com.cnjava.moviereview.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentCategoryBinding;
import com.cnjava.moviereview.model.Genres;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.CategoryAdapter;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.List;

public class CategoryFragment extends BaseFragment<FragmentCategoryBinding, CommonViewModel> implements CategoryAdapter.CategoryCallBack {

    public static final String TAG = CategoryFragment.class.getName();

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.backToPrev();
            }
        });
        List<Genres> listGenres = MyApplication.getInstance().getStorage().genresList;
        CategoryAdapter adapter = new CategoryAdapter(context, listGenres, this);
        binding.rvResult.setAdapter(adapter);
    }

    @Override
    protected FragmentCategoryBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentCategoryBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    public void gotoSearchByCategory(String name) {
        Bundle bundle = new Bundle();
        bundle.putString("category", name);
        callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }
}
