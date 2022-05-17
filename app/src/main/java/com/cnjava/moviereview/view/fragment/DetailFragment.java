package com.cnjava.moviereview.view.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentDetailBinding;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.CastAdapter;
import com.cnjava.moviereview.view.adapter.GenresAdapter;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.ReviewAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends BaseFragment<FragmentDetailBinding, CommonViewModel> implements PopularAdapter.MovieCallBack {

    public static final String TAG = DetailFragment.class.getName();

    private Object mData;
    private MovieDetail movieDetail;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        ViewUtils.show(binding.progressCircular);
        ViewUtils.gone(binding.layoutMovieDetail);
        int id = (int) mData;
        viewModel.getMovieDetail(id);
        viewModel.getCast(id);
        viewModel.getRecommendation(id);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        binding.tvAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(AddReviewFragment.TAG, null, true);
            }
        });

        binding.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.showFragment(ReviewFragment.TAG, null, true);
            }
        });

        List<Review> reviewList = new ArrayList<>();

        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 7.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", "Great", 7.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));

        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList);
        binding.rvReview.setAdapter(reviewAdapter);

    }

    @Override
    protected FragmentDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDetailBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_MOVIE_DETAIL)){
            movieDetail = (MovieDetail) data;
            List<String> listGenres = new ArrayList<>();
            binding.tvName.setText(movieDetail.title);
            binding.tvRuntime.setText(String.format(movieDetail.runtime + " min"));
            binding.tvRating.setText(String.valueOf(movieDetail.voteAverage));
            binding.tvOverview.setText(movieDetail.overview);
            binding.tvReleaseDate.setText(movieDetail.releaseDate);
            binding.tvRateCount.setText(String.valueOf(movieDetail.voteCount));
            binding.tvPopularity.setText(String.valueOf(movieDetail.popularity));
            Glide.with(context)
                    .load(String.format(Constants.IMAGE_URL + movieDetail.backdropPath))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.ivCover);
            Glide.with(context)
                    .load(String.format(Constants.IMAGE_URL + movieDetail.posterPath))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_image)
                    .into(binding.ivPoster);
            if (movieDetail.budget == 0){
                binding.tvBudget.setText(R.string.undefined);
            } else {
                binding.tvBudget.setText(NumberUtils.convertCurrency(movieDetail.budget));
            }
            if (movieDetail.revenue == 0){
                binding.tvRevenue.setText(R.string.undefined);
            } else {
                binding.tvRevenue.setText(NumberUtils.convertCurrency(movieDetail.revenue));
            }

            for(MovieDetail.Genres item : movieDetail.genres){
                listGenres.add(item.name);
            }

            binding.rvGenres.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            GenresAdapter adapterGenres = new GenresAdapter(context, listGenres);
            binding.rvGenres.setAdapter(adapterGenres);

        } else if(key.equals(Constants.KEY_GET_CAST)){
            Actor actor = (Actor) data;
            binding.rvCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CastAdapter castAdapter = new CastAdapter(context, actor);
            binding.rvCast.setAdapter(castAdapter);

        } else if(key.equals(Constants.KEY_GET_RECOMMENDATION)){
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.layoutMovieDetail);
            Movie movie = (Movie) data;
            if(movie.results.size() == 0){
                binding.tvNoRecommend.setText(String.format("We don't have enough data to suggest any movies based on %s. You can help by rating movies you've seen.", movieDetail.title));
                binding.tvNoRecommend.setVisibility(View.VISIBLE);
            } else {
                MovieAdapter adapter = new MovieAdapter(context, movie, this);
                binding.rvRecommend.setAdapter(adapter);
            }
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if(code == 999) {
            Log.d(TAG, "apiError: "+ data.toString());
            Toast.makeText(context, "Unable connect to server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.showFragment(DetailFragment.TAG, id, true);
    }
}
