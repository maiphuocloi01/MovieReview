package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentAboutBinding;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class AboutFragment extends BaseFragment<FragmentAboutBinding, CommonViewModel>{

    public static final String TAG = AboutFragment.class.getName();

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });
    }

    @Override
    protected FragmentAboutBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentAboutBinding.inflate(inflater, container, false);
    }
}
