package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentReviewDetailBinding;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class ReviewDetailFragment extends BaseFragment<FragmentReviewDetailBinding, CommonViewModel>{

    public static final String TAG = ReviewFragment.class.getName();
    private Object mData;


    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected FragmentReviewDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentReviewDetailBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
