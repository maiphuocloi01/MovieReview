package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentLoginBinding;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, CommonViewModel>{

    public static final String TAG = LoginFragment.class.getName();

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {
        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(ForgotPasswordFragment.TAG, null, true, 0);
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });
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
