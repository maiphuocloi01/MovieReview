package com.cnjava.moviereview.view.fragment.addreview;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentAddReviewBinding;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class AddReviewFragment extends BaseFragment<FragmentAddReviewBinding, CommonViewModel> {

    public static final String TAG = AddReviewFragment.class.getName();
    private Object mData;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        MovieDetail movieDetail = (MovieDetail) mData;

        binding.tvName.setText(movieDetail.title);
        //binding.tvDate.setText(NumberUtils.convertDateType3(movieDetail.releaseDate));
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + movieDetail.posterPath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(binding.ivPoster);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.backToPrev();
            }
        });

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                binding.tvStar.setText(String.valueOf((int) v));
            }
        });

        binding.etWriteReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                binding.tvCountCharacter.setText(s.length() + "/3000");
                if (s.length() >= 3000) {
                    binding.tvCountCharacter.setTextColor(ContextCompat.getColor(context, R.color.primary));
                } else {
                    binding.tvCountCharacter.setTextColor(ContextCompat.getColor(context, R.color.mid_white));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tvNotification.setOnClickListener(view -> {
            Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentByTag(DetailFragment.TAG);
            Log.d(TAG, "apiSuccess: " + currentFragment);
        });
        binding.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.etWriteReview.getText())) {
                    binding.etWriteReview.setError("Please write your review");
                } else {
                    String content = binding.etWriteReview.getText().toString().trim();
                    double star = (double) binding.ratingBar.getRating();
                    Review.MovieReview movieReview = new Review.MovieReview(
                            movieDetail.backdropPath,
                            String.valueOf(movieDetail.id),
                            movieDetail.title,
                            movieDetail.overview,
                            movieDetail.releaseDate
                    );

                    Review review = new Review(content, star, movieReview);
                    DialogUtils.showLoadingDialog(context);
                    if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                        viewModel.addReview(review, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                        IMEUtils.hideSoftInput(view);

                    }
                }
            }
        });

    }

    @Override
    protected FragmentAddReviewBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentAddReviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_ADD_REVIEW)) {
            Review review = (Review) data;
            if (review.id != null) {
                DialogUtils.hideLoadingDialog();
                callBack.reloadFragment(callBack.getBackStack());
                callBack.backToPrev();
            }
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (key.equals(Constants.KEY_ADD_REVIEW)) {
            DialogUtils.hideLoadingDialog();
            Log.d(TAG, "apiError: " + code + data.toString());
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
