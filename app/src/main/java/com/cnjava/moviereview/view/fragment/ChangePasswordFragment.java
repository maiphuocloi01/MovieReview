package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentChangePasswordBinding;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class ChangePasswordFragment extends BaseFragment<FragmentChangePasswordBinding, CommonViewModel>{


    public static final String TAG = ChangePasswordFragment.class.getName();

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
    protected FragmentChangePasswordBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentChangePasswordBinding.inflate(inflater, container, false);
    }
}
