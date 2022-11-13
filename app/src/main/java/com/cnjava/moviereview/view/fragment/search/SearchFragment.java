package com.cnjava.moviereview.view.fragment.search;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.paging.PagingData;

import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentSearchBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.RecommendSearchAdapter;
import com.cnjava.moviereview.view.adapter.TrendingAdapter;
import com.cnjava.moviereview.view.adapter.paging.SearchLoadStateAdapter;
import com.cnjava.moviereview.view.adapter.paging.SearchMovieAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchViewModel> implements SearchMovieAdapter.OnItemClickListener, TrendingAdapter.TrendingCallBack, PopularAdapter.MovieCallBack, RecommendSearchAdapter.RecommendSearchCallBack {

    public static final String TAG = SearchFragment.class.getName();

    private RecommendSearchAdapter searchAdapter;
    private static final String TODAY = "day";
    private static final String WEEK = "week";
    private SearchMovieAdapter searchMovieAdapter;
    private SearchLoadStateAdapter loadStateAdapter;

    @Override
    protected Class<SearchViewModel> getClassVM() {
        return SearchViewModel.class;
    }


    @Override
    protected void initViews() {

        ImageView searchIcon = binding.etSearch.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = binding.etSearch.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
        closeIcon.setColorFilter(ContextCompat.getColor(context, R.color.light_white));

        binding.rvResult.setHasFixedSize(true);
        searchMovieAdapter = new SearchMovieAdapter(context, this);
        loadStateAdapter = new SearchLoadStateAdapter(v -> searchMovieAdapter.retry());
        binding.rvResult.setAdapter(searchMovieAdapter.withLoadStateFooter(loadStateAdapter));
        /*binding.rvResult.setItemViewCacheSize(20);
        binding.rvResult.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);*/
        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
        if (viewModel.trendingWeekLD().getValue() == null) {
            viewModel.getTrending(WEEK);
            viewModel.trendingWeekLD().observe(this, _movie -> {
                MovieAdapter adapter = new MovieAdapter(context, _movie, this);
                binding.rvRecommend.setAdapter(adapter);
            });
        } else {
            MovieAdapter adapter = new MovieAdapter(context, viewModel.trendingWeekLD().getValue(), this);
            binding.rvRecommend.setAdapter(adapter);
        }
        if (viewModel.trendingDayLD().getValue() == null) {
            viewModel.getTrending(TODAY);
            viewModel.trendingDayLD().observe(this, _movie -> {
                initTopSearch(_movie);
            });
        } else {
            initTopSearch(viewModel.trendingDayLD().getValue());
        }

        searchAdapter = new RecommendSearchAdapter(context, this);
        binding.rvRecommendSearch.setAdapter(searchAdapter);
        binding.etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                if (text != null) {
                    searchMovie(text);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    initAutoComplete(s);
                } else {
                    ViewUtils.show(binding.layoutRecommendName);
                    ViewUtils.show(binding.layoutRecommendMovie);
                    ViewUtils.gone(binding.rvRecommendSearch);
                    ViewUtils.gone(binding.layoutSearchResult);
                }
                return true;
            }
        });
    }

    private void initAutoComplete(String s) {
        ViewUtils.gone(binding.layoutRecommendName);
        ViewUtils.gone(binding.layoutRecommendMovie);
        ViewUtils.show(binding.rvRecommendSearch);
        ViewUtils.gone(binding.layoutSearchResult);
        viewModel.autoCompleteSearch(s);
        viewModel.movieNameLD().observe(this, _names -> searchAdapter.renewItems(_names));

    }

    private void initTopSearch(Movie movie) {
        List<String> nameTrendingToday = movie.results.stream().limit(4)
                .map(name -> name.title)
                .collect(Collectors.toList());
        TrendingAdapter trendingAdapter = new TrendingAdapter(context, nameTrendingToday, this);
        binding.rvTrending.setAdapter(trendingAdapter);
    }

    @Override
    protected FragmentSearchBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.movieResultLD2().getValue() != null) {
            initResultSearch(viewModel.movieResultLD2().getValue(), viewModel.getTextSearch());
        } else {
            binding.etSearch.requestFocus();
            IMEUtils.showSoftInput(binding.etSearch);
        }
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    public void selectTrending(String name) {
        searchMovie(name);
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoSearch(String name) {
        searchMovie(name);
    }

    private void searchMovie(String text) {
        hideSoftInput(binding.etSearch);
        binding.etSearch.clearFocus();
        viewModel.setTextSearch(text);
        binding.etSearch.setQuery(text, false);
        viewModel.getLiveDataIsLoading().observe(this, loading -> {
            setLoading(loading);
        });
        viewModel.searchMoviePaging(text);
        viewModel.movieResultLD2().observe(this, _result -> {
            initResultSearch(_result, text);
        });
    }

    private void setLoading(Boolean loading) {
        if (loading) {
            ViewUtils.show(binding.progressBar);
            ViewUtils.gone(binding.layoutContent);
        } else {
            new Handler().postDelayed(() -> {
                ViewUtils.gone(binding.progressBar);
                ViewUtils.show(binding.layoutContent);
            }, 1800);

        }
    }

    private void initResultSearch(PagingData<Movie.Result> result, String text) {
        searchMovieAdapter.submitData(getLifecycle(), result);
        if (searchMovieAdapter.getItemCount() > 0) {
            //binding.tvCountMovie.setText(String.format("Found %s results", searchMovieAdapter.getItemCount()));
            ViewUtils.gone(binding.layoutRecommendMovie);
            ViewUtils.gone(binding.layoutRecommendName);
            ViewUtils.gone(binding.rvRecommendSearch);
            ViewUtils.show(binding.layoutSearchResult);
        } else {
            //binding.tvCountMovie.setText(String.format("No results for %s", text));
            //binding.tvCountMovie.append("\nHere are some movies that you might like");
            ViewUtils.gone(binding.rvRecommendSearch);
            ViewUtils.show(binding.layoutSearchResult);
            ViewUtils.gone(binding.layoutRecommendMovie);
            ViewUtils.gone(binding.layoutRecommendName);
        }
    }

    private void initResultSearch1(Movie result, String text) {
        //resultAdapter = new ResultAdapter(context, result, SearchFragment.this);
        //searchMovieAdapter = new SearchMovieAdapter()
        //binding.rvResult.setAdapter(resultAdapter);
        if (result.results.size() > 0) {
            //binding.tvCountMovie.setText(String.format("Found %s results", result.results.size()));
            ViewUtils.gone(binding.layoutRecommendMovie);
            ViewUtils.gone(binding.layoutRecommendName);
            ViewUtils.gone(binding.rvRecommendSearch);
            ViewUtils.show(binding.layoutSearchResult);
        } else {
            //binding.tvCountMovie.setText(String.format("No results for %s", text));
            //binding.tvCountMovie.append("\nHere are some movies that you might like");
            ViewUtils.gone(binding.rvRecommendSearch);
            ViewUtils.show(binding.layoutSearchResult);
            ViewUtils.gone(binding.layoutRecommendMovie);
            ViewUtils.gone(binding.layoutRecommendName);
        }
    }

    @Override
    public void onItemClick(Movie.Result movieResult) {
        callBack.replaceFragment(DetailMovieFragment.TAG, movieResult.id, true, Constants.ANIM_SLIDE);
    }
}
