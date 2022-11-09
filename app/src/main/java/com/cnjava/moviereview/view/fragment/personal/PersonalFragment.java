package com.cnjava.moviereview.view.fragment.personal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentPersonalBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Statistic;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MyReviewAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PersonalFragment extends BaseFragment<FragmentPersonalBinding, CommonViewModel> implements MyReviewAdapter.MyReviewCallBack {

    public static final String TAG = PersonalFragment.class.getName();
    private Object mData;
    private String userId;
    private User user;
    private MyReviewAdapter myReviewAdapter;
    private List<Review> reviewList;
    private List<Review> sortedReview;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {
        userId = (String) mData;
        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        ViewUtils.show(binding.progressCircularProfile);
        ViewUtils.gone(binding.layoutTitlePersonal);
        ViewUtils.gone(binding.layoutScroll);
        viewModel.getUserById(userId);
        viewModel.getStatisticsByUserId(userId);

        binding.rbAll.setChecked(true);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
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
        if (user.getAvatar() != null) {
            Glide.with(context)
                    .load(String.format(user.getAvatar()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.img_default_avt)
                    .into(binding.ivAvatar);
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
    protected FragmentPersonalBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentPersonalBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_USER_BY_ID)) {
            user = (User) data;
            initViewUser(user);
            reviewList = user.getReviews();
            Collections.reverse(reviewList);
            sortedReview = new ArrayList<>(reviewList);
            initReviewView(sortedReview);
        } else if (key.equals(Constants.KEY_GET_STATISTIC_BY_USER_ID)) {
            Statistic statistic = (Statistic) data;
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
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {
            ViewUtils.gone(binding.progressCircularProfile);
            ViewUtils.show(binding.layoutTitlePersonal);
            ViewUtils.show(binding.layoutScroll);
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
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

    }

    @Override
    public void updateReview(Review review) {

    }

    @Override
    public void gotoMovieDetail(String id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, Integer.valueOf(id), true, Constants.ANIM_SLIDE);
    }
}
