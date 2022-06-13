package com.cnjava.moviereview.view.fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentReviewDetailBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.model.Summary;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class ReviewDetailFragment extends BaseFragment<FragmentReviewDetailBinding, CommonViewModel>{

    public static final String TAG = ReviewDetailFragment.class.getName();
    private Object mData;
    private String shorten = null;
    private Review review;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {
        review = (Review) mData;
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });
        Glide.with(context)
                .load(Constants.IMAGE_URL + review.movie.backdropPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.view);
        //binding.tvNameMovie.setText(review.movie.title);
        //binding.tvReleaseDate.setText(NumberUtils.convertDateType3(review.movie.releaseDate));

        Glide.with(context)
                .load(review.user.getAvatar())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_default_avt)
                .into(binding.ivAvatar);
        binding.tvName.setText(review.user.getName());
        binding.ratingBar.setRating((int) review.rating);
        binding.tvInfo.setText(String.format((int) review.rating + " star  |  " + review.like.size() + " likes  |  " + review.dislike.size() + " dislikes"));
        binding.tvDate.setText(NumberUtils.convertDateType7(review.createdAt));
        binding.tvContent.setText(review.content);

        binding.btShorten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (review.content.length() > 300) {
                    if (shorten == null) {
                        ViewUtils.show(binding.progressCircular);
                        ViewUtils.gone(binding.btShorten);
                        viewModel.summarizationReview(review.content);
                    } else {
                        binding.tvContent.setText(shorten);
                        ViewUtils.show(binding.tvNeedMore);
                        int percent = (int) (((float)shorten.length()/review.content.length())*100);
                        binding.tvNeedMore.setText("Text reduce to " + percent + "% (" + countWord(shorten) + "/" + countWord(review.content) + " words)");
                        ViewUtils.gone(binding.btShorten);
                        ViewUtils.show(binding.btOriginal);
                    }
                } else {
                    ViewUtils.show(binding.tvNeedMore);
                    ViewUtils.gone(binding.btShorten);
                    ViewUtils.show(binding.progressCircular);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ViewUtils.show(binding.btShorten);
                            ViewUtils.gone(binding.progressCircular);
                        }
                    }, 200);
                }
            }
        });

        binding.btOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvContent.setText(review.content);
                ViewUtils.gone(binding.btOriginal);
                ViewUtils.show(binding.btShorten);
                ViewUtils.gone(binding.tvNeedMore);

            }
        });

    }

    @Override
    protected FragmentReviewDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentReviewDetailBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if(key.equals(Constants.KEY_SUMMARIZATION)){
            Summary summary = (Summary) data;
            shorten = summary.summaryText;
            binding.tvContent.setText(summary.summaryText);
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.btOriginal);
            ViewUtils.show(binding.tvNeedMore);
            int percent = (int) (((float)shorten.length()/review.content.length())*100);
            binding.tvNeedMore.setText("Text reduce to " + percent + "% (" + countWord(shorten) + "/" + countWord(review.content) + " words)");
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        ViewUtils.gone(binding.progressCircular);
        ViewUtils.show(binding.btShorten);
        Toast.makeText(context, "Unable to connect server", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    private int countWord(String s){
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length;
    }
}
