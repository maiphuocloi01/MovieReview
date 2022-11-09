package com.cnjava.moviereview.view.fragment.search;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentSearchBinding;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.RecommendSearchAdapter;
import com.cnjava.moviereview.view.adapter.ResultAdapter;
import com.cnjava.moviereview.view.adapter.TrendingAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.cnjava.moviereview.view.fragment.searchresult.SearchResultFragment;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends BaseFragment<FragmentSearchBinding, SearchViewModel> implements TrendingAdapter.TrendingCallBack, PopularAdapter.MovieCallBack, RecommendSearchAdapter.RecommendSearchCallBack {

    public static final String TAG = SearchFragment.class.getName();

    private RecommendSearchAdapter searchAdapter;
    private static final String TODAY = "day";
    private static final String WEEK = "week";

    @Override
    protected Class<SearchViewModel> getClassVM() {
        return SearchViewModel.class;
    }

    @Override
    protected void initViews() {

        viewModel.getTrending(WEEK);
        viewModel.trendingWeekLD().observe(this, _movie -> {
            MovieAdapter adapter = new MovieAdapter(context, _movie, this);
            binding.rvRecommend.setAdapter(adapter);
        });
        viewModel.getTrending(TODAY);
        viewModel.trendingDayLD().observe(this, _movie -> {
            List<String> nameTrendingToday = _movie.results.stream().limit(4)
                    .map(name -> name.title)
                    .collect(Collectors.toList());
            TrendingAdapter trendingAdapter = new TrendingAdapter(context, nameTrendingToday, this);
            binding.rvTrending.setAdapter(trendingAdapter);
        });
        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        searchAdapter = new RecommendSearchAdapter(context, this);
        binding.rvRecommendSearch.setAdapter(searchAdapter);
        binding.etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                if (text != null) {
                    searchMovie(text);
                    /*Bundle bundle = new Bundle();
                    bundle.putString("search", text.trim());
                    callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);*/
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!s.equals("")) {
                    ViewUtils.gone(binding.layoutRecommendName);
                    ViewUtils.gone(binding.layoutRecommendMovie);
                    ViewUtils.show(binding.rvRecommendSearch);
                    ViewUtils.gone(binding.layoutSearchResult);
                    viewModel.autoCompleteSearch(s);
                    viewModel.movieNameLD().observe(getViewLifecycleOwner(), _names -> {
                        searchAdapter.renewItems(_names);
                    });
                } else {
                    ViewUtils.show(binding.layoutRecommendName);
                    ViewUtils.show(binding.layoutRecommendMovie);
                    ViewUtils.gone(binding.rvRecommendSearch);
                    ViewUtils.gone(binding.layoutSearchResult);
                }
                return true;
            }
        });
        ImageView searchIcon = binding.etSearch.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = binding.etSearch.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
        closeIcon.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
    }

    @Override
    protected FragmentSearchBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.etSearch.requestFocus();
        IMEUtils.showSoftInput(binding.etSearch);
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
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoSearch(String name) {
        searchMovie(name);
    }

    private void searchMovie(String text){
        hideSoftInput(binding.etSearch);
        viewModel.searchMovie(text);
        viewModel.movieResultLD().observe(getViewLifecycleOwner(), _result -> {
            ResultAdapter resultAdapter;
            resultAdapter = new ResultAdapter(context, _result, SearchFragment.this);
            binding.rvResult.setAdapter(resultAdapter);
            if (_result.results.size() > 0) {
                binding.tvCountMovie.setText(String.format("Found %s results", _result.results.size()));
                ViewUtils.show(binding.layoutSearchResult);
                ViewUtils.gone(binding.layoutRecommendMovie);
                ViewUtils.gone(binding.layoutRecommendName);
                ViewUtils.gone(binding.rvRecommendSearch);
            } else {
                binding.tvCountMovie.setText(String.format("No results for %s", text));
                binding.tvCountMovie.append(String.format("\nHere are some movies that you might like", text));
                ViewUtils.gone(binding.rvRecommendSearch);
                ViewUtils.show(binding.layoutSearchResult);
                ViewUtils.show(binding.layoutRecommendMovie);
                ViewUtils.gone(binding.layoutRecommendName);
            }
        });
    }
}
