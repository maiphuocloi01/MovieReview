package com.cnjava.moviereview.view.fragment;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentForgotPasswordBinding;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;

public class ForgotPasswordFragment extends BaseFragment<FragmentForgotPasswordBinding, CommonViewModel> {

    public static final String TAG = ForgotPasswordFragment.class.getName();
    private static Dialog dialog;
    private String otp = "0";

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_SEND_OTP)) {
            Response response = (Response) data;
            Toast.makeText(context, response.getToken(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, otp, Toast.LENGTH_SHORT).show();
        } else if (key.equals(Constants.KEY_CONFIRM_OTP)) {
            Response response = (Response) data;
            Log.d(TAG, "KEY_CONFIRM_OTP: ");
            DialogUtils.hideLoadingDialog();
            if (response.getError() == null) {
                dialog.dismiss();
                if (binding.etEmail.getText() != null) {
                    callBack.replaceFragment(ResetPasswordFragment.TAG, binding.etEmail.getText().toString().trim(), false, Constants.ANIM_SLIDE);
                }
            }
            //Toast.makeText(context, otp, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (key.equals(Constants.KEY_CONFIRM_OTP)) {
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
        } else if (key.equals(Constants.KEY_SEND_OTP)) {
            DialogUtils.hideLoadingDialog();
            Toast.makeText(context, "Unable to connect Heroku", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        binding.btSendOtp.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.etEmail.getText())) {
                binding.layoutEmail.setError("Please enter email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.getText()).matches()) {
                binding.layoutEmail.setError("Please enter the correct email format");
            } else {
                sendOTP();
                openSendOTPDialog();

                //viewModel.checkEmailExist(binding.etEmail.getText().toString());
                //DialogUtils.showLoadingDialog(context);
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

    }

    @Override
    protected FragmentForgotPasswordBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentForgotPasswordBinding.inflate(inflater, container, false);
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

        String email = binding.etEmail.getText().toString().trim();
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
}
