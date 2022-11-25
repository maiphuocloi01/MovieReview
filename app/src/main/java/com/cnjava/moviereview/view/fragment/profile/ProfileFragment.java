package com.cnjava.moviereview.view.fragment.profile;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

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
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.adapter.MyReviewAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;
import com.cnjava.moviereview.view.fragment.editprofile.EditProfileFragment;
import com.cnjava.moviereview.view.fragment.editreview.EditReviewFragment;
import com.cnjava.moviereview.view.fragment.favorite.FavoriteFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;
import com.cnjava.moviereview.view.fragment.setting.SettingFragment;

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
    private MainViewModel mainViewModel;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    private void initReviewView(List<Review> reviewList, User user) {
        setReviewAdapter(reviewList, user);
        binding.rbCritical.setOnClickListener(view -> {
            List<Review> sortedReview;
            sortedReview = reviewList.stream()
                    .filter(review -> review.content.length() > 150)
                    .collect(Collectors.toList());
            sortedReview = sortedReview.stream()
                    .filter(review -> review.rating < 5.0)
                    .collect(Collectors.toList());
            setReviewAdapter(sortedReview, user);
        });
        binding.rbAll.setOnClickListener(view -> setReviewAdapter(reviewList, user));
        binding.rbTopLike.setOnClickListener(view -> {
            List<Review> sortedReview;
            sortedReview = reviewList.stream()
                    .sorted(Comparator.comparing(review -> review.like.size()))
                    .collect(Collectors.toList());
            Collections.reverse(sortedReview);
            setReviewAdapter(sortedReview, user);
        });

        binding.rbTopScore.setOnClickListener(view -> {
            List<Review> sortedReview;
            sortedReview = reviewList.stream()
                    .sorted(Comparator.comparing(Review::getRating).reversed())
                    .collect(Collectors.toList());
            setReviewAdapter(sortedReview, user);
        });
    }

    private void setReviewAdapter(List<Review> reviewList, User user) {
        if (reviewList != null) {
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
    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            ViewUtils.show(binding.progressCircularProfile);
            ViewUtils.gone(binding.layoutProfile);
        } else {
            ViewUtils.gone(binding.progressCircularProfile);
            ViewUtils.show(binding.layoutProfile);
        }
    }

    @Override
    protected Class<ProfileViewModel> getClassVM() {
        return ProfileViewModel.class;
    }

    @Override
    protected void initViews() {


        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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

            if (mainViewModel.yourProfileLD().getValue() == null) {
                mainViewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                mainViewModel.yourProfileLD().observe(this, _user -> {
                    initViewUser(_user);
                });
            } else {
                Log.d(TAG, "yourProfileLD: ");
                initViewUser(mainViewModel.yourProfileLD().getValue());
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
        if (mainViewModel.reviewByUserIdLD().getValue() == null) {
            mainViewModel.getReviewByUserId(user.getId());
            mainViewModel.reviewByUserIdLD().observe(this, _reviews -> {
                initReviewView(_reviews, user);
            });
        } else {
            List<Review> reviews = mainViewModel.reviewByUserIdLD().getValue();
            if (reviews != null) {
                initReviewView(reviews, user);
            }
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
