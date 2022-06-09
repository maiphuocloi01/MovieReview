package com.cnjava.moviereview.view.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentResetPasswordBinding;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public class ResetPasswordFragment extends BaseFragment<FragmentResetPasswordBinding, CommonViewModel>{

    public static final String TAG = ResetPasswordFragment.class.getName();

    public Object mData;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        String email = (String) mData;
        binding.btConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(binding.etPassword.getText())) {
                    binding.etPassword.setError("Please fill your password");
                } else if (TextUtils.isEmpty(binding.etPasswordConfirm.getText())) {
                    binding.etPasswordConfirm.setError("Please confirm your password");
                } else if (!binding.etPassword.getText().toString().equals(binding.etPasswordConfirm.getText().toString())) {
                    binding.etPasswordConfirm.setError("Confirmation password does not match");
                } else {
                    User user = new User(email,
                            binding.etPassword.getText().toString()
                    );
                    IMEUtils.hideSoftInput(binding.btConfirmPassword);
                    DialogUtils.showLoadingDialog(context);
                    viewModel.forgotPassword(user);
                }
            }
        });
    }

    @Override
    protected FragmentResetPasswordBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentResetPasswordBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if(key.equals(Constants.KEY_FORGOT_PASSWORD)){
            DialogUtils.hideLoadingDialog();
            callBack.backToPrev();
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if(code == 400){
            DialogUtils.hideLoadingDialog();
            ResponseBody res = (ResponseBody) data;
            Gson gson = new Gson();
            Type type = new TypeToken<Response>() {
            }.getType();
            Response errorResponse = gson.fromJson(res.charStream(), type);
            Toast.makeText(context, errorResponse.getError(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
