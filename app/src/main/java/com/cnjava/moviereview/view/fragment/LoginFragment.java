package com.cnjava.moviereview.view.fragment;

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentLoginBinding;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.IMEUtils;
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


        if (CommonUtils.getInstance().getPref(Constants.USERNAME) != null) {
            Log.d(TAG, "USERNAME: ");
            binding.etUsername.setText(CommonUtils.getInstance().getPref(Constants.USERNAME));
        }

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.replaceFragment(ForgotPasswordFragment.TAG, null, true, 0);
            }
        });

        binding.etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? R.color.primary : R.color.light_white;
                binding.layoutEmail.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
                binding.layoutEmail.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
            }
        });

        binding.etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? R.color.primary : R.color.light_white;
                binding.layoutPassword.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
                binding.layoutPassword.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.etUsername.getText())) {
                    binding.layoutEmail.setError("Please fill your email");
                } else if (TextUtils.isEmpty(binding.etPassword.getText())) {
                    binding.layoutPassword.setError("Please fill your password");
                } else {
                    viewModel.login(
                            binding.etUsername.getText().toString().trim(),
                            binding.etPassword.getText().toString()
                    );
                    DialogUtils.showLoadingDialog(context);
                    IMEUtils.hideSoftInput(view);

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
                callBack.replaceFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SLIDE);
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
            CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN_2ND);
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, token.getAccessToken());
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN_2ND, token.getAccessToken());
            if(binding.etUsername.getText() != null) {
                CommonUtils.getInstance().savePref(Constants.USERNAME, binding.etUsername.getText().toString().trim());
            }
            MyApplication.getInstance().getStorage().reviewList = null;
            //CommonUtils.getInstance().savePref(Constants.USERNAME, binding.etUsername.getText().toString());
            //callBack.replaceFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
            if (MyApplication.getInstance().getStorage().myUser == null) {
                //DialogUtils.showLoadDataDialog(context);
                Log.d(TAG, "myUser null");
                if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                    Log.d(TAG, "getYourProfile: ");
                    viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                }
            }
        } else if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            User user = (User) data;
            DialogUtils.hideLoadingDialog();
            MyApplication.getInstance().getStorage().myUser = user;
            Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show();
            callBack.backToPrev();
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
            Toast.makeText(context, errorResponse.getError(), Toast.LENGTH_LONG).show();
        } else if (code == 999){
            DialogUtils.hideLoadingDialog();
            Toast.makeText(context, "Unable to connect Heroku, try again", Toast.LENGTH_SHORT).show();
        }
    }
}
