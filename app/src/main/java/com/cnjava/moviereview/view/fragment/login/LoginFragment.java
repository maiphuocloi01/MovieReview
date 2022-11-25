package com.cnjava.moviereview.view.fragment.login;

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
import androidx.lifecycle.ViewModelProvider;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentLoginBinding;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.model.UserResponse;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.view.activity.main.MainActivity;
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.callback.SignInGoogleCallBack;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.forgotpassword.ForgotPasswordFragment;
import com.cnjava.moviereview.view.fragment.register.RegisterFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.ResponseBody;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> {

    public static final String TAG = LoginFragment.class.getName();
    private MainViewModel mainViewModel;

    @Override
    protected Class<LoginViewModel> getClassVM() {
        return LoginViewModel.class;
    }

    @Override
    protected void initViews() {

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        if (CommonUtils.getInstance().getPref(Constants.USERNAME) != null) {
            Log.d(TAG, "USERNAME: ");
            binding.etUsername.setText(CommonUtils.getInstance().getPref(Constants.USERNAME));
        }

        binding.tvForgotPassword.setOnClickListener(view -> callBack.replaceFragment(ForgotPasswordFragment.TAG, null, true, 0));

        binding.etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? R.color.primary : R.color.light_white;
            binding.layoutEmail.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
            binding.layoutEmail.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
        });

        binding.etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            int color = hasFocus ? R.color.primary : R.color.light_white;
            binding.layoutPassword.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
            binding.layoutPassword.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
        });

        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());

        binding.btLogin.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.etUsername.getText())) {
                binding.layoutEmail.setError("Please fill your email");
            } else if (TextUtils.isEmpty(binding.etPassword.getText())) {
                binding.layoutPassword.setError("Please fill your password");
            } else {
                viewModel.getLiveDataIsLoading().observe(this, loading ->{
                    if(loading){
                        DialogUtils.showLoadingDialog(context);
                    } else {
                        DialogUtils.hideLoadingDialog();
                    }
                });
                viewModel.loginUser(binding.etUsername.getText().toString().trim(),
                        binding.etPassword.getText().toString());
                viewModel.loginLD().observe(this, userResponse -> {
                    mainViewModel.getYourProfile(userResponse.getAccessToken());
                    CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, userResponse.getAccessToken());
                    CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, userResponse.getAccessToken());
                    if (binding.etUsername.getText() != null) {
                        CommonUtils.getInstance().savePref(Constants.USERNAME, binding.etUsername.getText().toString().trim());
                    }
                    Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show();
                    callBack.backToPrev();
                });

                viewModel.getLiveDataOnError().observe(this, error ->{
                    /*ResponseBody res = (ResponseBody) error.getMessage();
                    Gson gson = new Gson();
                    Type type = new TypeToken<UserResponse>() {
                    }.getType();
                    UserResponse errorUserResponse = gson.fromJson(res.charStream(), type);*/
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                });
                IMEUtils.hideSoftInput(view);
            }

        });

        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.loginWithGoogle();
                mainViewModel.getLoginSuccessLD().observe(getViewLifecycleOwner(), firebaseUser -> {
                    firebaseUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task1) {
                            String token = task1.getResult().getToken();
                            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, token);
                            CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, token);
                            User newUser = new User(firebaseUser.getEmail(), firebaseUser.getDisplayName(), true);
                            viewModel.loginWithGoogle(newUser);
                            viewModel.loginWithGoogleLD().observe(getViewLifecycleOwner(), user -> {
                                mainViewModel.getYourProfile(token);
                                mainViewModel.yourProfileLD().observe(getViewLifecycleOwner(), profile ->{
                                    Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show();
                                    callBack.backToPrev();
                                });
                            });
                            Log.d(TAG, "onComplete: " + token);
                        }
                    });


                });
            }
        });

        binding.tvRegister.setOnClickListener(view -> callBack.replaceFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SLIDE));
    }

    @Override
    protected FragmentLoginBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_LOGIN)) {
            UserResponse token = (UserResponse) data;
            mainViewModel.getYourProfile(token.getAccessToken());
            Log.d(TAG, "apiSuccess: " + token.getAccessToken());
            //CommonUtils.getInstance().clearPref(Constants.SAVE_SESSION);
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, token.getAccessToken());
            CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, token.getAccessToken());
            if (binding.etUsername.getText() != null) {
                CommonUtils.getInstance().savePref(Constants.USERNAME, binding.etUsername.getText().toString().trim());
            }
            Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show();
            MyApplication.getInstance().getStorage().reviewList = null;
            MyApplication.getInstance().getStorage().myUser = null;
            DialogUtils.hideLoadingDialog();
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
            Type type = new TypeToken<UserResponse>() {
            }.getType();
            UserResponse errorUserResponse = gson.fromJson(res.charStream(), type);
            Toast.makeText(context, errorUserResponse.getError(), Toast.LENGTH_LONG).show();
        } else if (code == 999) {
            DialogUtils.hideLoadingDialog();
            Toast.makeText(context, "Unable to connect, try again", Toast.LENGTH_SHORT).show();
        }
    }

}
