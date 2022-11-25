package com.cnjava.moviereview.view.fragment.review;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentReviewBinding;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.adapter.ReviewAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.editreview.EditReviewFragment;
import com.cnjava.moviereview.view.fragment.login.LoginFragment;
import com.cnjava.moviereview.view.fragment.personal.PersonalFragment;
import com.cnjava.moviereview.view.fragment.register.RegisterFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewFragment extends BaseFragment<FragmentReviewBinding, ReviewViewModel> implements ReviewAdapter.ReviewCallBack {

    public static final String TAG = ReviewFragment.class.getName();
    private final String[] items = {"Newest", "Oldest"};
    private Object mData;
    private ReviewAdapter reviewAdapter;
    private List<Review> tempReviews = new ArrayList<>();
    private MovieDetail movieDetail;
    private MainViewModel mainViewModel;

    @Override
    protected Class<ReviewViewModel> getClassVM() {
        return ReviewViewModel.class;
    }

    @Override
    protected void initViews() {

        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(context, R.layout.item_dropdown_ver2, items);
        binding.autoCompleteTxt.setAdapter(adapterItems);
        binding.autoCompleteTxt.setShowSoftInputOnFocus(false);
        binding.autoCompleteTxt.setDropDownBackgroundResource(R.drawable.bg_light_dark_corner_10);
        binding.rbAll.setChecked(true);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        movieDetail = (MovieDetail) mData;
        if (mainViewModel.movieReviewLD().getValue() == null) {
            mainViewModel.getReviewByMovieId(String.valueOf(movieDetail.id));
            mainViewModel.movieReviewLD().observe(this, _reviews -> {
                initListReview(_reviews);
            });
        } else {
            initListReview(mainViewModel.movieReviewLD().getValue());
        }

        binding.tvName.setText(movieDetail.title);
        Glide.with(context)
                .load(Constants.IMAGE_URL + movieDetail.posterPath)
                .error(R.drawable.ic_image)
                .centerInside()
                .into(binding.ivPoster);

        binding.ivBack.setOnClickListener(view -> {
            binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
            callBack.backToPrev();
        });

    }

    private void initListReview(List<Review> reviews) {
        initReview(reviews);
        initButtonFeatures(reviews);
        tempReviews = reviews;
        binding.autoCompleteTxt.setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            sortReviewByDay(item, tempReviews);
        });
    }

    private void initButtonFeatures(List<Review> reviews) {
        binding.rbAll.setOnClickListener(view -> {
            binding.tvSort.setText(binding.rbAll.getText());
            ViewUtils.show(binding.layoutSort);
            setVisibilityView(reviews);
        });

        binding.rbPositive.setOnClickListener(view -> {
            binding.tvSort.setText(binding.rbPositive.getText());
            ViewUtils.gone(binding.layoutSort);
            List<Review> sortedReview = reviews.stream()
                    .sorted(Comparator.comparing(review -> review.like.size()))
                    .collect(Collectors.toList());
            Collections.reverse(sortedReview);
            if (sortedReview.size() > 0) {
                ViewUtils.show(binding.rvReview);
                ViewUtils.gone(binding.layoutEmpty);
                initReview(sortedReview);
            } else {
                ViewUtils.gone(binding.rvReview);
                ViewUtils.show(binding.layoutEmpty);
            }
        });

        binding.rbCritical.setOnClickListener(view -> {
            binding.tvSort.setText(binding.rbCritical.getText());
            ViewUtils.gone(binding.layoutSort);
            List<Review> sortedReview = reviews.stream()
                    .filter(review -> review.content.length() > 150)
                    .collect(Collectors.toList());
            sortedReview = sortedReview.stream()
                    .filter(review -> review.rating < 5.0)
                    .collect(Collectors.toList());
            Collections.reverse(sortedReview);
            if (sortedReview.size() > 0) {
                ViewUtils.show(binding.rvReview);
                ViewUtils.gone(binding.layoutEmpty);
                initReview(sortedReview);
            } else {
                ViewUtils.gone(binding.rvReview);
                ViewUtils.show(binding.layoutEmpty);
            }
        });

        binding.rbTenStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 10.0));

        binding.rbNineStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 9.0));

        binding.rbEightStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 8.0));

        binding.rbSevenStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 7.0));

        binding.rbSixStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 6.0));

        binding.rbFiveStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 5.0));

        binding.rbFourStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 4.0));

        binding.rbThreeStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 3.0));

        binding.rbTwoStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 2.0));

        binding.rbOneStar.setOnClickListener(view -> sortReviewByStar(reviews, (RadioButton) view, 1.0));
    }

    private Double starCheck() {
        if (binding.rbAll.isChecked()) {
            return 0.0;
        } else if (binding.rbOneStar.isChecked()) {
            return 1.0;
        } else if (binding.rbTwoStar.isChecked()) {
            return 2.0;
        } else if (binding.rbThreeStar.isChecked()) {
            return 3.0;
        } else if (binding.rbFourStar.isChecked()) {
            return 4.0;
        } else if (binding.rbFiveStar.isChecked()) {
            return 5.0;
        } else if (binding.rbSixStar.isChecked()) {
            return 6.0;
        } else if (binding.rbSevenStar.isChecked()) {
            return 7.0;
        } else if (binding.rbEightStar.isChecked()) {
            return 8.0;
        } else if (binding.rbNineStar.isChecked()) {
            return 9.0;
        } else if (binding.rbTenStar.isChecked()) {
            return 10.0;
        }
        return -1.0;
    }

    private void sortReviewByStar(List<Review> reviews, RadioButton radioButton, double star) {
        binding.tvSort.setText(radioButton.getText());
        ViewUtils.show(binding.layoutSort);
        sortReviewByDay("Newest", reviews);
    }

    private void sortReviewByDay(String item, List<Review> reviews) {
        double star = starCheck();
        if (star == 0) {
            if (item.equals("Newest")) {
                List<Review> sortedReview = reviews.stream()
                        .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                        .collect(Collectors.toList());
                setVisibilityView(sortedReview);
            } else if (item.equals("Oldest")) {
                List<Review> sortedReview = reviews.stream()
                        .sorted(Comparator.comparing(Review::getCreatedAt))
                        .collect(Collectors.toList());
                setVisibilityView(sortedReview);
            }
        } else {
            if (item.equals("Newest")) {
                List<Review> sortedReview = reviews.stream()
                        .filter(review -> review.getRating() == star)
                        .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                        .collect(Collectors.toList());
                setVisibilityView(sortedReview);
            } else if (item.equals("Oldest")) {
                List<Review> sortedReview = reviews.stream()
                        .filter(review -> review.getRating() == star)
                        .sorted(Comparator.comparing(Review::getCreatedAt))
                        .collect(Collectors.toList());
                setVisibilityView(sortedReview);
            }
        }
    }

    private void setVisibilityView(List<Review> reviews) {
        if (reviews.size() > 0) {
            ViewUtils.show(binding.rvReview);
            ViewUtils.gone(binding.layoutEmpty);
            initReview(reviews);
        } else {
            ViewUtils.gone(binding.rvReview);
            ViewUtils.show(binding.layoutEmpty);
        }
    }

    @Override
    protected FragmentReviewBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentReviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    private void initReview(List<Review> listReview) {
        if (viewModel.yourProfileLD().getValue() == null) {
            viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            viewModel.yourProfileLD().observe(this, _user -> {
                reviewAdapter = new ReviewAdapter(context, listReview, _user, this);
                binding.rvReview.setAdapter(reviewAdapter);
            });
        } else {
            reviewAdapter = new ReviewAdapter(context, listReview, viewModel.yourProfileLD().getValue(), this);
            binding.rvReview.setAdapter(reviewAdapter);
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
        } else {
            showLoginDialog();
        }
    }

    @Override
    public void dislikeReview(String id) {
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.dislikeReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        } else {
            showLoginDialog();
        }
    }

    private void showLoginDialog() {
        Log.d(TAG, "showAlertDialog: ");
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_login_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        dialog.setCancelable(true);

        TextView btnCancel = dialog.findViewById(R.id.bt_signup);
        Button btnConfirm = dialog.findViewById(R.id.bt_signin);

        btnCancel.setOnClickListener(view -> {
            callBack.replaceFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            callBack.replaceFragment(LoginFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });
        dialog.show();

    }

    @Override
    public void deleteReview(String id) {
        reviewAdapter.deleteItem(id);
        viewModel.deleteReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
    }

    @Override
    public void updateReview(Review review) {
        callBack.replaceFragment(EditReviewFragment.TAG, review, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoUserReview(String userId) {
        if (userId != null) {
            callBack.replaceFragment(PersonalFragment.TAG, userId, true, Constants.ANIM_SLIDE);
        }
    }
}
