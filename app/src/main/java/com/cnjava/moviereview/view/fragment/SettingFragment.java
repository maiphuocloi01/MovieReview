package com.cnjava.moviereview.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentSettingBinding;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class SettingFragment extends BaseFragment<FragmentSettingBinding, CommonViewModel> {
    public static final String TAG = SettingFragment.class.getName();


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

        binding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btLogout.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                showAlertDialog();
            }
        });

        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            binding.switch1.setChecked(true);
        } else {
            binding.switch1.setChecked(false);
        }

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    String tokenBackup = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN_2ND);
                    if (tokenBackup != null) {
                        CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, tokenBackup);
                        Log.d(TAG, "onCheckedChanged: " + b);
                    }
                } else {
                    String tokenBackup = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN_2ND);
                    if (tokenBackup != null) {
                        CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
                        Log.d(TAG, "onCheckedChanged: " + b);
                    }
                }
                //Toast.makeText(context, "Click " + b, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected FragmentSettingBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSettingBinding.inflate(inflater, container, false);
    }

    private void showAlertDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_dialog);
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

        Button btnCancel = dialog.findViewById(R.id.bt_cancel3);
        Button btnConfirm = dialog.findViewById(R.id.bt_confirm3);

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        btnConfirm.setOnClickListener(view -> {
            CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
            CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN_2ND);
            MyApplication.getInstance().getStorage().myUser = null;
            MyApplication.getInstance().getStorage().reviewList = null;
            MyApplication.getInstance().getStorage().moviePopular = null;
            MyApplication.getInstance().getStorage().movieNowPlaying = null;
            MyApplication.getInstance().getStorage().movieUpcoming = null;
            MyApplication.getInstance().getStorage().movieTopRated = null;
            MyApplication.getInstance().getStorage().movieRecommend = null;
            callBack.replaceFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
            //actionShowFragment(LoginFragment.TAG, null, false, Constants.ANIM_SLIDE);
            //CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
            //CommonUtils.getInstance().clearPref(Constants.USERNAME);
            dialog.dismiss();
        });
        dialog.show();

    }
}
