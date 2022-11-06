package com.cnjava.moviereview.view.fragment;

import android.os.Bundle;
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
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Statistic;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MyReviewAdapter;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
        } else if (key.equals(Constants.KEY_REVIEW_BY_USER_ID)){
            reviewList = (List<Review>) data;
            Collections.reverse(reviewList);
            sortedReview = new ArrayList<>(reviewList);
            initReviewView(sortedReview);
        }
    }

    private void initReviewView(List<Review> reviewList) {
        ViewUtils.gone(binding.progressCircularProfile);
        ViewUtils.show(binding.layoutTitlePersonal);
        ViewUtils.show(binding.layoutScroll);
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
        if(code == 999){
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

        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        ViewUtils.show(binding.progressCircularProfile);
        ViewUtils.gone(binding.layoutTitlePersonal);
        ViewUtils.gone(binding.layoutScroll);
        binding.rbAll.setChecked(true);


        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.getMyStatistics(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            if (MyApplication.getInstance().getStorage().myUser != null) {
                viewModel.getReviewByUserId(MyApplication.getInstance().getStorage().myUser.getId());
            }
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
                    callBack.showFragment(EditProfileFragment.TAG, MyApplication.getInstance().getStorage().myUser, true, Constants.ANIM_SLIDE);
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
                    .placeholder(R.drawable.img_default_avt)
                    .into(binding.ivAvatar);
        }
    }

    @Override
    protected FragmentProfileBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    public void gotoReviewDetail(Review review) {
        callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
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
        Bundle bundle = new Bundle();
        bundle.putSerializable("review", review);
        bundle.putString("tag", ProfileFragment.TAG);
        callBack.showFragment(EditReviewFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoMovieDetail(String id) {
        callBack.showFragment(DetailFragment.TAG, Integer.valueOf(id), true, Constants.ANIM_SLIDE);
    }


}
