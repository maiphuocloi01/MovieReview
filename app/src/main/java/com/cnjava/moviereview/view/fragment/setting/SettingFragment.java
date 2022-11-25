package com.cnjava.moviereview.view.fragment.setting;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentSettingBinding;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.about.AboutFragment;
import com.cnjava.moviereview.view.fragment.changepassword.ChangePasswordFragment;
import com.cnjava.moviereview.view.fragment.home.HomeFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class SettingFragment extends BaseFragment<FragmentSettingBinding, CommonViewModel> {

    public static final String TAG = SettingFragment.class.getName();
    private MainViewModel mainViewModel;


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

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        MyApplication.getInstance().getStorage().fragmentTag = TAG;

        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());

        binding.btLogout.setOnClickListener(view -> {
            binding.btLogout.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
            showAlertDialog();
        });

        if (CommonUtils.getInstance().getPref(Constants.SAVE_SESSION) != null) {
            binding.switch1.setChecked(true);
        } else {
            binding.switch1.setChecked(false);
        }

        binding.switch1.setOnCheckedChangeListener((compoundButton, b) -> {
            String tokenBackup = CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN);
            if (b) {
                if (tokenBackup != null) {
                    CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, tokenBackup);
                }
            } else {
                if (tokenBackup != null) {
                    CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, null);
                }
            }
        });

        binding.row2.setOnClickListener(view -> callBack.replaceFragment(ChangePasswordFragment.TAG, null, true, Constants.ANIM_SLIDE));

        binding.row3.setOnClickListener(view -> callBack.replaceFragment(AboutFragment.TAG, null, true, Constants.ANIM_SLIDE));

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
            CommonUtils.getInstance().savePref(Constants.ACCESS_TOKEN, null);
            CommonUtils.getInstance().savePref(Constants.SAVE_SESSION, null);
            MyApplication.getInstance().getStorage().myUser = null;
            MyApplication.getInstance().getStorage().reviewList = null;
            MyApplication.getInstance().getStorage().movieDetail = null;
            callBack.clearBackStack();
            callBack.reloadFragmentByTag(HomeFragment.TAG);
            mainViewModel.clearMovieReview();
            mainViewModel.clearYourProfile();
            mainViewModel.clearReviewByUserId();
            callBack.replaceFragment(HomeFragment.TAG, null, false, Constants.ANIM_SLIDE);
            dialog.dismiss();
        });
        dialog.show();

    }
}
