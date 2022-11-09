package com.cnjava.moviereview.view.fragment.result;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentResultBinding;
import com.cnjava.moviereview.view.fragment.BaseFragment;

public class ResultFragment extends BaseFragment<FragmentResultBinding, ResultViewModel> {

    private Object mData;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<ResultViewModel> getClassVM() {
        return ResultViewModel.class;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected FragmentResultBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentResultBinding.inflate(inflater, container, false);
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
