package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentProfileBinding;
import com.cnjava.moviereview.model.Statistic;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.Locale;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, CommonViewModel> {

    public static final String TAG = ProfileFragment.class.getName();

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            User user = (User) data;
            MyApplication.getInstance().getStorage().myUser = user;
            initViewUser(user);
        } else if (key.equals(Constants.KEY_GET_MY_STATISTIC)) {
            Statistic statistic = (Statistic) data;
            if ((int) statistic.likes > 1) {
                binding.tvCountLike.setText((int) statistic.likes + " likes");
            } else {
                binding.tvCountLike.setText((int) statistic.likes + " like");
            }

            if ((int) statistic.reviews > 1) {
                binding.tvCountReview.setText((int) statistic.reviews + " reviews");
            } else {
                binding.tvCountReview.setText((int) statistic.reviews + " review");
            }

            binding.tvAverageRate.setText(String.format(Locale.US, "%.1f scores", statistic.avgStars));
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

        viewModel.getMyStatistics(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(SettingFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });

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
                if (MyApplication.getInstance().getStorage().myUser != null) {
                    callBack.replaceFragment(EditProfileFragment.TAG, MyApplication.getInstance().getStorage().myUser, true, Constants.ANIM_SLIDE);
                }
            }
        });

        binding.ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivLogout.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                //showAlertDialog();
                callBack.showFragment(FavoriteFragment.TAG, null, true, Constants.ANIM_SLIDE);
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


}
