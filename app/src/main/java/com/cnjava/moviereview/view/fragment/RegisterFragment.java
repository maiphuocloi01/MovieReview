package com.cnjava.moviereview.view.fragment;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentRegisterBinding;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.util.PasswordMeter;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding, CommonViewModel> {

    public static final String TAG = RegisterFragment.class.getName();
    private static Dialog dialog;
    private String otp;
    private int rank = 0;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MyApplication.getInstance().getStorage().fragmentTag = TAG;

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                StringBuilder s = new StringBuilder(charSequence.toString());
                rank = PasswordMeter.passwordRanking(s);
                Log.d(TAG, "onTextChanged: " + s);
                ViewUtils.show(binding.tvMeter);
                if (rank < 4) {
                    binding.tvMeter.setText("Weak");
                    Log.d(TAG, "Weak: " + rank);
                    binding.tvMeter.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                    //binding.layoutPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_light_black_corner_10_border_red));
                } else if (rank >= 5) {
                    binding.tvMeter.setText("Strong");
                    Log.d(TAG, "Strong: " + rank);
                    //binding.layoutPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_light_black_corner_10_border_green));
                    binding.tvMeter.setTextColor(ContextCompat.getColor(context, R.color.colorGreen));
                } else {
                    binding.tvMeter.setText("Medium");
                    Log.d(TAG, "Medium: " + rank);
                    //binding.layoutPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_light_black_corner_10_border_yellow));
                    binding.tvMeter.setTextColor(ContextCompat.getColor(context, R.color.colorWarning));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvLogin.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.backToPrev();
            }
        });

        binding.etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? R.color.primary : R.color.light_white;
                binding.layoutEmail.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
                binding.layoutEmail.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
            }
        });

        binding.etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? R.color.primary : R.color.light_white;
                binding.layoutName.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
                binding.layoutName.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
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

        binding.etPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                int color = hasFocus ? R.color.primary : R.color.light_white;
                binding.layoutPasswordConfirm.setStartIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
                binding.layoutPasswordConfirm.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(context, color)));
            }
        });


        binding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.etEmail.getText())) {
                    binding.layoutEmail.setError("Please fill your email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText()).matches()) {
                    binding.layoutEmail.setError("Please enter the correct email format");
                } else if (TextUtils.isEmpty(binding.etUsername.getText())) {
                    binding.layoutName.setError("Please fill your full name");
                } else if (TextUtils.isEmpty(binding.etPassword.getText())) {
                    binding.layoutPassword.setError("Please fill your password");
                    binding.tvMeter.setVisibility(View.GONE);
                } else if (rank < 4) {
                    binding.layoutPassword.setError("Password is too weak");
                    binding.tvMeter.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(binding.etPasswordConfirm.getText())) {
                    binding.layoutPasswordConfirm.setError("Please confirm your password");
                } else if (!binding.etPassword.getText().toString().equals(binding.etPasswordConfirm.getText().toString())) {
                    binding.layoutPasswordConfirm.setError("Confirmation password does not match");
                } else {
                    User user = new User(binding.etEmail.getText().toString().trim(),
                            binding.etPassword.getText().toString(),
                            binding.etUsername.getText().toString().trim()
                    );
                    viewModel.registration(user);
                    DialogUtils.showLoadingDialog(context);
//                    viewModel.setEmail(binding.etEmail.getText().toString());
//                    viewModel.setUsername(binding.etUsername.getText().toString());
//                    viewModel.setPassword(binding.etUsername.getText().toString());

                }
            }
        });
    }

    private void sendOTP() {
        viewModel.sendOTP(binding.etEmail.getText().toString());
    }

    private void openSendOTPDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_confirm_otp);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(false);

        //Ánh xạ view
        TextView tvEmail = dialog.findViewById(R.id.tv_email);
        TextView tvResend = dialog.findViewById(R.id.tv_resend);
        TextView tvCDTitle = dialog.findViewById(R.id.tv_countdown_title);
        TextView tvCountdown = dialog.findViewById(R.id.tv_countdown);
        EditText etOtp = dialog.findViewById(R.id.et_otp);
        Button btnCancel = dialog.findViewById(R.id.bt_cancel);
        Button btnConfirm = dialog.findViewById(R.id.bt_confirm);

        String email = binding.etEmail.getText().toString();
        tvEmail.setText(email);

        countDownTimer(tvCountdown, tvCDTitle, tvResend);

        tvResend.setOnClickListener(view -> {
            sendOTP();
            tvCountdown.setVisibility(View.VISIBLE);
            tvCDTitle.setVisibility(View.VISIBLE);
            tvResend.setVisibility(View.GONE);
            countDownTimer(tvCountdown, tvCDTitle, tvResend);
        });

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        btnConfirm.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etOtp.getText())) {
                etOtp.setError("Please fill OTP code");
            } else {
                DialogUtils.showLoadingDialog(context);
                IMEUtils.hideSoftInput(btnConfirm);
                viewModel.confirmOTP(email, etOtp.getText().toString().trim());
                //Toast.makeText(context, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                //callBack.showFragment(ResetPasswordFragment.TAG, email, false);
                //dialog.dismiss();
            }

        });

        dialog.show();
    }

    private void countDownTimer(TextView tvCountdown, TextView tvCDTitle, TextView tvResend) {
        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH, "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                );

                tvCountdown.setText(sDuration);
            }

            @Override
            public void onFinish() {
                tvCountdown.setVisibility(View.GONE);
                tvCDTitle.setVisibility(View.GONE);
                tvResend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    protected FragmentRegisterBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentRegisterBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_REGISTER)) {
            Log.d(TAG, "KEY_REGISTER: ");
            sendOTP();
            openSendOTPDialog();
            DialogUtils.hideLoadingDialog();
        } else if (key.equals(Constants.KEY_SEND_OTP)) {
            Response response = (Response) data;
            Toast.makeText(context, response.getToken(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, otp, Toast.LENGTH_SHORT).show();
        } else if (key.equals(Constants.KEY_CONFIRM_OTP)) {
            Response response = (Response) data;
            Log.d(TAG, "KEY_CONFIRM_OTP: ");
            DialogUtils.hideLoadingDialog();
            if (response.getError() == null) {
                dialog.dismiss();
                CommonUtils.getInstance().clearPref(Constants.USERNAME);
                callBack.replaceFragment(LoginFragment.TAG, null, false, Constants.ANIM_SLIDE);
            }
            //Toast.makeText(context, otp, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (key.equals(Constants.KEY_REGISTER)) {
            if (code == 400) {
                DialogUtils.hideLoadingDialog();
//                Response response = (Response) data;
//                if (response.getError() != null) {
//                    Toast.makeText(context, "email already taken", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(context, "Email already taken", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "email already taken: ");
            } else {
                DialogUtils.hideLoadingDialog();
                Toast.makeText(context, "Unable to connect Heroku", Toast.LENGTH_SHORT).show();
            }
        } else if (key.equals(Constants.KEY_CONFIRM_OTP)) {
            if (code == 400) {
                DialogUtils.hideLoadingDialog();
                Log.d(TAG, "KEY_CONFIRM_OTP: ");
                //Toast.makeText(context, "invalid otp", Toast.LENGTH_SHORT).show();
                ResponseBody res = (ResponseBody) data;
                Gson gson = new Gson();
                Type type = new TypeToken<Response>() {
                }.getType();
                Response errorResponse = gson.fromJson(res.charStream(), type);
                Toast.makeText(context, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                /*try {
                    JSONObject jsonObj = new JSONObject(response.body().toString());
                    Toast.makeText(context, jsonObj.getString("error"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();

                }*/
            }
        }
    }
}
