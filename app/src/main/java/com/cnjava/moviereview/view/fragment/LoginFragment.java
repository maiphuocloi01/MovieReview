package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentLoginBinding;
import com.cnjava.moviereview.viewmodel.BaseViewModel;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, CommonViewModel>{

    private static final String TAG = LoginFragment.class.getName();

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected FragmentLoginBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {


    }

    @Override
    public void apiError(String key, int code, Object data) {

    }
}
