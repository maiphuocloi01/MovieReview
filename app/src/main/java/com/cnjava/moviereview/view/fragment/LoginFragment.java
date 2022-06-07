package com.cnjava.moviereview.view.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentLoginBinding;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

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
                if (TextUtils.isEmpty(binding.etUsername.getText())) {
                    binding.etUsername.setError("Please fill your email");
                } else if (TextUtils.isEmpty(binding.etPassword.getText())) {
                    binding.etPassword.setError("Please fill your password");
                } else {
                    viewModel.login(
                            binding.etUsername.getText().toString(),
                            binding.etPassword.getText().toString()
                    );
                    DialogUtils.showLoadingDialog(context);

//                new Handler().postDelayed(() -> {
//                    progressDialog.dismiss();
//                    callBack.showFragment(PagerFragment.TAG, null, false);
//                }, 1000);
                }

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
        if (key.equals(Constants.KEY_LOGIN)) {
            Response token = (Response) data;
            Log.d(TAG, "apiSuccess: " + token.getAccessToken());
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, token.getAccessToken());
            Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show();
            DialogUtils.hideLoadingDialog();
            //CommonUtils.getInstance().savePref(Constants.USERNAME, binding.etUsername.getText().toString());
            callBack.showFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
        }

    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 403) {
            //Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
            DialogUtils.hideLoadingDialog();
            ResponseBody res = (ResponseBody) data;
            Gson gson = new Gson();
            Type type = new TypeToken<Response>() {
            }.getType();
            Response errorResponse = gson.fromJson(res.charStream(), type);
            Toast.makeText(context, errorResponse.getError(), Toast.LENGTH_SHORT).show();
        }
    }
}
