package com.cnjava.moviereview.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentProfileBinding;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, CommonViewModel>{

    public static final String TAG = ProfileFragment.class.getName();

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

        /*binding.btEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionShowFragment(EditProfileFragment.TAG, null, true);
            }
        });

        binding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btLogout.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                showAlertDialog();
            }
        });*/

        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(EditProfileFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });
    }

    @Override
    protected FragmentProfileBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    /*private void actionShowFragment(String tag, Object data, boolean isBack, int anim) {
        NavigateFragment parentFrag = ((NavigateFragment) ProfileFragment.this.getParentFragment());
        if (parentFrag != null) {
            parentFrag.setActionShowFragment(tag, data, isBack, anim);
        }
    }*/

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
            callBack.showFragment(LoginFragment.TAG, null, false, Constants.ANIM_SLIDE);
            //actionShowFragment(LoginFragment.TAG, null, false, Constants.ANIM_SLIDE);
            //CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
            //CommonUtils.getInstance().clearPref(Constants.USERNAME);
            dialog.dismiss();
        });
        dialog.show();

    }
}
