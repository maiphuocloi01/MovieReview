package com.cnjava.moviereview.view.fragment.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingData;

import com.cnjava.moviereview.databinding.FragmentResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieResultAdapter;
import com.cnjava.moviereview.view.adapter.paging.SearchLoadStateAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ResultFragment extends BaseFragment<FragmentResultBinding, ResultViewModel> implements MovieResultAdapter.MovieResultCallBack {

    public static final String TAG = ResultFragment.class.getName();

    private MovieResultAdapter movieResultAdapter;
    private SearchLoadStateAdapter loadStateAdapter;

    private Object mData;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<ResultViewModel> getClassVM() {
        return ResultViewModel.class;
    }

    @Override
    protected void initViews() {
        Bundle bundle = (Bundle) mData;

        binding.rvResult.setHasFixedSize(true);
        movieResultAdapter = new MovieResultAdapter(context, this);
        loadStateAdapter = new SearchLoadStateAdapter(v -> movieResultAdapter.retry());
        binding.rvResult.setAdapter(movieResultAdapter.withLoadStateFooter(loadStateAdapter));

        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
        viewModel.getLiveDataIsLoading().observe(this, loading -> {
            setLoading(loading);
        });
        if (bundle.getString(Constants.KEY_TYPE_RESULT).equals(Constants.KEY_GET_UPCOMING_MOVIE)) {
            binding.tvShowsText.setText(bundle.getString(Constants.KEY_TYPE_RESULT));
            if (viewModel.upcomingMovieLD().getValue() == null) {
                viewModel.getUpComingMovie();
                viewModel.upcomingMovieLD().observe(this, _result -> {
                    initResultMovie(_result);
                });
            } else {
                initResultMovie(viewModel.upcomingMovieLD().getValue());
            }
        } else if (bundle.getString(Constants.KEY_TYPE_RESULT).equals(Constants.KEY_GET_NOW_PLAYING_MOVIE)) {
            binding.tvShowsText.setText(bundle.getString(Constants.KEY_TYPE_RESULT));
            if (viewModel.nowPlayingMovieLD().getValue() == null) {
                viewModel.getNowPlayingMoviePaging();
                viewModel.nowPlayingMovieLD().observe(this, _result -> {
                    initResultMovie(_result);
                });
            } else {
                initResultMovie(viewModel.nowPlayingMovieLD().getValue());
            }
        } else if (bundle.getString(Constants.KEY_TYPE_RESULT).equals(Constants.KEY_GET_TOP_RATED_MOVIE)) {
            binding.tvShowsText.setText(bundle.getString(Constants.KEY_TYPE_RESULT));
            if (viewModel.topRatedMovieLD().getValue() == null) {
                viewModel.getTopRatedMovie();
                viewModel.topRatedMovieLD().observe(this, _result -> {
                    initResultMovie(_result);
                });
            } else {
                initResultMovie(viewModel.topRatedMovieLD().getValue());
            }
        } else if (bundle.getString(Constants.KEY_TYPE_RESULT).equals(Constants.KEY_SEARCH_MOVIE_BY_CATEGORY)) {
            binding.tvShowsText.setText(bundle.getString("name"));
            if (viewModel.movieByCategoryLD().getValue() == null) {
                viewModel.getMovieByCategory(bundle.getString("category"));
                viewModel.movieByCategoryLD().observe(this, _result -> {
                    initResultMovie(_result);
                });
            } else {
                initResultMovie(viewModel.movieByCategoryLD().getValue());
            }
        }
    }

    private void setLoading(Boolean loading) {
        if (loading) {
            ViewUtils.show(binding.progressCircular);
        } else {
            binding.progressCircular.getHandler().postDelayed(() -> {
                ViewUtils.gone(binding.progressCircular);
            }, 1200);
        }
    }

    private void initResultMovie(PagingData<Movie.Result> result) {
        movieResultAdapter.submitData(getLifecycle(), result);
        ViewUtils.show(binding.rvResult);
    }

    @Override
    protected FragmentResultBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentResultBinding.inflate(inflater, container, false);
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, id, true, Constants.ANIM_FADE);
    }
}
