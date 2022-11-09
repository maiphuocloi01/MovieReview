package com.cnjava.moviereview.view.fragment.editreview;

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

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentEditReviewBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class EditReviewFragment extends BaseFragment<FragmentEditReviewBinding, CommonViewModel> {

    public static final String TAG = EditReviewFragment.class.getName();
    private Object mData;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        Review review = (Review) mData;

        binding.tvName.setText(review.movie.title);
        //binding.tvDate.setText(NumberUtils.convertDateType3(movieDetail.releaseDate));
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + review.movie.backdropPath))
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
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

        binding.tvCountCharacter.setText(review.content.length() + "/3000");

        binding.ratingBar.setRating((float) review.rating);
        binding.etWriteReview.setText(review.content);

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

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(binding.etWriteReview.getText())) {
                    binding.etWriteReview.setError("Please write your review");
                } else {
                    String content = binding.etWriteReview.getText().toString().trim();
                    double star = (double) binding.ratingBar.getRating();

                    Review newReview = new Review(content, star);
                    DialogUtils.showLoadingDialog(context);
                    if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                        Log.d(TAG, "addReview: ");
                        viewModel.updateReview(review.id, newReview, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    }
                }
            }
        });

    }

    @Override
    protected FragmentEditReviewBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentEditReviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if(key.equals(Constants.KEY_UPDATE_REVIEW)){
            Review review = (Review) data;
            if (review.id != null){
                DialogUtils.hideLoadingDialog();
                //MyApplication.getInstance().getStorage().reviewList = null;
                callBack.backToPrev();
            }
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if(key.equals(Constants.KEY_UPDATE_REVIEW)){
            DialogUtils.hideLoadingDialog();
            Log.d(TAG, "apiError: " + code + data.toString());
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
