package com.cnjava.moviereview.view.fragment.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentProfileBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MyReviewAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;
import com.cnjava.moviereview.view.fragment.editprofile.EditProfileFragment;
import com.cnjava.moviereview.view.fragment.editreview.EditReviewFragment;
import com.cnjava.moviereview.view.fragment.favorite.FavoriteFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;
import com.cnjava.moviereview.view.fragment.setting.SettingFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment<FragmentProfileBinding, CommonViewModel> implements MyReviewAdapter.MyReviewCallBack {

    public static final String TAG = ProfileFragment.class.getName();
    private MyReviewAdapter myReviewAdapter;
    private List<Review> reviewList;
    private List<Review> sortedReview;

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            User user = (User) data;
            MyApplication.getInstance().getStorage().myUser = user;
            initViewUser(user);
        }
    }

    private void initReviewView(List<Review> reviewList) {
        if (reviewList.size() > 0) {
            myReviewAdapter = new MyReviewAdapter(context, reviewList, this);
            binding.rvReview.setAdapter(myReviewAdapter);
            ViewUtils.show(binding.rvReview);
            ViewUtils.gone(binding.layoutEmpty);
        } else {
            ViewUtils.gone(binding.rvReview);
            ViewUtils.show(binding.layoutEmpty);
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {

        }
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            ViewUtils.show(binding.progressCircularProfile);
            ViewUtils.gone(binding.layoutTitlePersonal);
            ViewUtils.gone(binding.layoutScroll);
        } else {
            ViewUtils.gone(binding.progressCircularProfile);
            ViewUtils.show(binding.layoutTitlePersonal);
            ViewUtils.show(binding.layoutScroll);
        }
    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        MyApplication.getInstance().getStorage().fragmentTag = TAG;

        binding.rbAll.setChecked(true);
        profileViewModel.getLiveDataIsLoading().observe(this, this::setLoading);

        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            profileViewModel.getMyStatistics(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            profileViewModel.myStatisticsLD().observe(this, _statistic -> {
                if ((int) _statistic.likes > 1) {
                    binding.tvCountLike.setText((int) _statistic.likes + " likes");
                } else {
                    binding.tvCountLike.setText((int) _statistic.likes + " like");
                }
                if ((int) _statistic.reviews > 1) {
                    binding.tvCountReview.setText((int) _statistic.reviews + " reviews");
                } else {
                    binding.tvCountReview.setText((int) _statistic.reviews + " review");
                }
                binding.tvAverageRate.setText(String.format(Locale.US, "%.1f scores", _statistic.avgStars));
            });

            profileViewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            profileViewModel.yourProfileLD().observe(this, _user -> {
                initViewUser(_user);
                profileViewModel.getReviewByUserId(_user.getId());
                profileViewModel.reviewByUserIdLD().observe(this, _reviews -> {
                    reviewList = _reviews;
                    Collections.reverse(reviewList);
                    sortedReview = new ArrayList<>(reviewList);
                    initReviewView(sortedReview);
                });
            });

        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.replaceFragment(SettingFragment.TAG, null, true, Constants.ANIM_SLIDE);
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
                callBack.replaceFragment(FavoriteFragment.TAG, null, true, Constants.ANIM_SLIDE);
            }
        });

        binding.rbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initReviewView(reviewList);
            }
        });

        binding.rbTopLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortedReview = reviewList.stream()
                        .sorted(Comparator.comparing(review -> review.like.size()))
                        .collect(Collectors.toList());
                Collections.reverse(sortedReview);
                initReviewView(sortedReview);
            }
        });

        binding.rbTopScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortedReview = reviewList.stream()
                        .sorted(Comparator.comparing(Review::getRating).reversed())
                        .collect(Collectors.toList());
                initReviewView(sortedReview);
            }
        });

        binding.rbCritical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortedReview = reviewList.stream()
                        .filter(review -> review.content.length() > 150)
                        .collect(Collectors.toList());
                sortedReview = sortedReview.stream()
                        .filter(review -> review.rating < 5.0)
                        .collect(Collectors.toList());
                initReviewView(sortedReview);
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
                    .placeholder(R.drawable.progress_animation)
                    .into(binding.ivAvatar);
        }
    }

    @Override
    protected FragmentProfileBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    public void gotoReviewDetail(Review review) {
        callBack.replaceFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void likeReview(String id) {
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.likeReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        }
    }

    @Override
    public void dislikeReview(String id) {
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.dislikeReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        }
    }

    @Override
    public void deleteReview(String id) {
        myReviewAdapter.deleteItem(id);
        viewModel.deleteReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
    }

    @Override
    public void updateReview(Review review) {
        callBack.replaceFragment(EditReviewFragment.TAG, review, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoMovieDetail(String id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, Integer.valueOf(id), true, Constants.ANIM_SLIDE);
    }


}
