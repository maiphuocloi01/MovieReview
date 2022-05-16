package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentAddReviewBinding;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class AddReviewFragment extends BaseFragment<FragmentAddReviewBinding, CommonViewModel>{

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected FragmentAddReviewBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentAddReviewBinding.inflate(inflater, container, false);
    }
    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }
}
