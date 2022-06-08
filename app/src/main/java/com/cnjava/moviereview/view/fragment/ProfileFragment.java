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
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentProfileBinding;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, CommonViewModel> {

    public static final String TAG = ProfileFragment.class.getName();

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            User user = (User) data;
            MyApplication.getInstance().getStorage().myUser = user;
            initViewUser(user);
        }
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

        if (MyApplication.getInstance().getStorage().myUser == null) {
            //DialogUtils.showLoadDataDialog(context);
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            }
        } else {
            initViewUser(MyApplication.getInstance().getStorage().myUser);
        }

        binding.tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvEditProfile.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.replaceFragment(EditProfileFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });

        binding.ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivLogout.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                showAlertDialog();
            }
        });
    }

    private void initViewUser(User user) {
        binding.tvName.setText(user.getName());
        binding.tvEmail.setText(user.getEmail());
        if (user.getAvatar() != null) {
            Glide.with(context)
                    .load(String.format(user.getAvatar()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.img_default_avt)
                    .into(binding.ivAvatar);
        }
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
            CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
            MyApplication.getInstance().getStorage().myUser = null;
            MyApplication.getInstance().getStorage().moviePopular = null;
            MyApplication.getInstance().getStorage().movieNowPlaying = null;
            MyApplication.getInstance().getStorage().movieUpcoming = null;
            MyApplication.getInstance().getStorage().movieTopRated = null;
            MyApplication.getInstance().getStorage().movieRecommend = null;
            //actionShowFragment(LoginFragment.TAG, null, false, Constants.ANIM_SLIDE);
            //CommonUtils.getInstance().clearPref(Constants.ACCESS_TOKEN);
            //CommonUtils.getInstance().clearPref(Constants.USERNAME);
            dialog.dismiss();
        });
        dialog.show();

    }
}
