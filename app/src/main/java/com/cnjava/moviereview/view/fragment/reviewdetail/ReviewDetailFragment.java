package com.cnjava.moviereview.view.fragment.reviewdetail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentReviewDetailBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.cutom.TranslateTextView;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewDetailFragment extends BaseFragment<FragmentReviewDetailBinding, CommonViewModel> {

    public static final String TAG = ReviewDetailFragment.class.getName();
    private Object mData;
    private String shorten = null;
    private String translateText = null;
    private Review review;
    private ReviewDetailViewModel reviewDetailViewModel;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {
        reviewDetailViewModel = new ViewModelProvider(this).get(ReviewDetailViewModel.class);
        review = (Review) mData;

        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        binding.collapsingToolbarLayout.setTitleEnabled(false);

        binding.toolbar.setNavigationOnClickListener(v -> callBack.backToPrev());

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbar.setTitle(review.user.getName());
                    isShow = true;
                } else if (isShow) {
                    binding.toolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

        Glide.with(context)
                .load(Constants.IMAGE_URL + review.movie.backdropPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.viewDetailHeaderReview.backdrop);

        Glide.with(context)
                .load(review.user.getAvatar())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_default_avt)
                .into(binding.viewDetailHeaderReview.imagePoster);
        binding.viewDetailHeaderReview.textTitle.setText(review.user.getName());
        binding.viewDetailHeaderReview.textCredits.setText((int) review.rating + " ★");
        //binding.viewDetailHeaderReview.textSubtitle.setText(String.format(review.like.size() + " likes  |  " + review.dislike.size() + " dislikes"));
        binding.viewDetailHeaderReview.textSubtitle.setText(NumberUtils.convertDateType7(review.createdAt));

        binding.textOverview.textBody.setText(review.content);

        reviewDetailViewModel.getLiveDataIsLoading().observe(this, this::setScreenLoading);
        reviewDetailViewModel.translateText(review.content);
        reviewDetailViewModel.translateLiveData().observe(this, translate -> {
            TranslateTextView.onChange(binding.textOverview.textBody,
                    binding.textOverview.buttonTranslate,
                    review.content,
                    translate.getText().get(0));
        });

    }

    private void setScreenLoading(boolean isLoading) {
        /*if (isLoading) {
            ViewUtils.show(binding.progressCircular);
            ViewUtils.gone(binding.btOriginal);
        } else {
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.btOriginal);
        }
        ViewUtils.gone(binding.btShorten);*/
    }

    @Override
    protected FragmentReviewDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentReviewDetailBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_SUMMARIZATION)) {
            /*Summary summary = (Summary) data;
            shorten = summary.summaryText;
            binding.tvContent.setText(summary.summaryText);
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.btOriginal);
            ViewUtils.show(binding.tvNeedMore);
            int percent = (int) (((float) shorten.length() / review.content.length()) * 100);
            binding.tvNeedMore.setText("Text reduce to " + percent + "% (" + countWord(shorten) + "/" + countWord(review.content) + " words)");*/
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        /*ViewUtils.gone(binding.progressCircular);
        ViewUtils.show(binding.btShorten);
        Toast.makeText(context, "Unable to connect server", Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    private int countWord(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length;
    }
}
