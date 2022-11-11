package com.cnjava.moviereview.view.fragment.profile;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentProfileBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Statistic;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> implements MyReviewAdapter.MyReviewCallBack {

    public static final String TAG = ProfileFragment.class.getName();
    private MyReviewAdapter myReviewAdapter;
    private List<Review> reviewList;
    private List<Review> sortedReview;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    private void initReviewView(List<Review> reviewList, User user) {
        if (reviewList.size() > 0) {
            myReviewAdapter = new MyReviewAdapter(context, reviewList, user, this);
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
    protected Class<ProfileViewModel> getClassVM() {
        return ProfileViewModel.class;
    }

    @Override
    protected void initViews() {


        binding.rbAll.setChecked(true);
        viewModel.getLiveDataIsLoading().observe(this, this::setLoading);

        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {

            if (viewModel.myStatisticsLD().getValue() == null) {
                viewModel.getMyStatistics(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                viewModel.myStatisticsLD().observe(this, _statistic -> {
                    initStatisticView(_statistic);
                });
            } else {
                initStatisticView(viewModel.myStatisticsLD().getValue());
            }

            if (viewModel.yourProfileLD().getValue() == null) {
                viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                viewModel.yourProfileLD().observe(this, _user -> {
                    initViewUser(_user);
                });
            } else {
                initViewUser(viewModel.yourProfileLD().getValue());
            }

        }

        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());

        binding.ivSetting.setOnClickListener(view -> callBack.replaceFragment(SettingFragment.TAG, null, true, Constants.ANIM_SLIDE));


        binding.ivFavoriteList.setOnClickListener(view -> callBack.replaceFragment(FavoriteFragment.TAG, null, true, Constants.ANIM_SLIDE));


    }

    private void initStatisticView(Statistic statistic) {
        if ((int) statistic.likes > 1) {
            binding.tvCountLike.setText(getString(R.string.likes, (int) statistic.likes));
        } else {
            binding.tvCountLike.setText(getString(R.string.like, (int) statistic.likes));
        }

        if ((int) statistic.reviews > 1) {
            binding.tvCountReview.setText(getString(R.string.reviews, (int) statistic.reviews));
        } else {
            binding.tvCountReview.setText(getString(R.string.review, (int) statistic.reviews));
        }
        binding.tvAverageRate.setText(String.format(Locale.US, "%.1f scores", statistic.avgStars));
    }

    private void initViewUser(User user) {
        binding.rbCritical.setOnClickListener(view -> {
            sortedReview = reviewList.stream()
                    .filter(review -> review.content.length() > 150)
                    .collect(Collectors.toList());
            sortedReview = sortedReview.stream()
                    .filter(review -> review.rating < 5.0)
                    .collect(Collectors.toList());
            initReviewView(sortedReview, user);
        });
        binding.rbAll.setOnClickListener(view -> initReviewView(reviewList, user));
        binding.rbTopLike.setOnClickListener(view -> {
            sortedReview = reviewList.stream()
                    .sorted(Comparator.comparing(review -> review.like.size()))
                    .collect(Collectors.toList());
            Collections.reverse(sortedReview);
            initReviewView(sortedReview, user);
        });

        binding.rbTopScore.setOnClickListener(view -> {
            sortedReview = reviewList.stream()
                    .sorted(Comparator.comparing(Review::getRating).reversed())
                    .collect(Collectors.toList());
            initReviewView(sortedReview, user);
        });
        if (viewModel.reviewByUserIdLD().getValue() == null) {
            viewModel.getReviewByUserId(user.getId());
            viewModel.reviewByUserIdLD().observe(this, _reviews -> {
                reviewList = _reviews;
                sortedReview = new ArrayList<>(reviewList);
                initReviewView(sortedReview, user);
            });
        } else {
            reviewList = viewModel.reviewByUserIdLD().getValue();
            sortedReview = new ArrayList<>(reviewList);
            initReviewView(sortedReview, user);
        }

        binding.tvName.setText(user.getName());
        binding.tvEmail.setText(user.getEmail());
        if (user.getAvatar() != null) {
            Glide.with(context)
                    .load(user.getAvatar())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.progress_animation)
                    .into(binding.ivAvatar);
        }
        binding.tvEditProfile.setOnClickListener(view -> callBack.replaceFragment(EditProfileFragment.TAG, user, true, Constants.ANIM_SLIDE));
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
