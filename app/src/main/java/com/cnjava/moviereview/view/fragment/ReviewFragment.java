package com.cnjava.moviereview.view.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentReviewBinding;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.view.adapter.ReviewAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends BaseFragment<FragmentReviewBinding, CommonViewModel>{

    public static final String TAG = ReviewFragment.class.getName();
    private Object mData;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MovieDetail movieDetail = (MovieDetail) mData;

        binding.tvName.setText(movieDetail.title);
        //binding.tvDate.setText(NumberUtils.convertDateType3(movieDetail.releaseDate));
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + movieDetail.posterPath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_image)
                .into(binding.ivPoster);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        binding.rbAll.setChecked(true);

        List<Review> reviewList = new ArrayList<>();

        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 8.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", "Great", 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", "", 6.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));

        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList);
        binding.rvReview.setAdapter(reviewAdapter);
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

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
