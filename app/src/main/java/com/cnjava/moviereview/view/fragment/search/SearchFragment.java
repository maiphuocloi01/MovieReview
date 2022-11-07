package com.cnjava.moviereview.view.fragment.search;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentSearchBinding;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.RecommendSearchAdapter;
import com.cnjava.moviereview.view.adapter.TrendingAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.searchresult.SearchResultFragment;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        if (binding.etSearch.requestFocus()) {
            IMEUtils.showSoftInput(binding.etSearch);
        }

        String jsonFileString = NumberUtils.getJsonFromAssets(context, "movieName.json");
        Gson gson = new Gson();
        Type listMovieNameType = new TypeToken<List<MovieName>>() {
        }.getType();
        List<MovieName> listMovieName = gson.fromJson(jsonFileString, listMovieNameType);
        List<String> movieNames = listMovieName.stream()
                .map(movieName -> movieName.name)
                .collect(Collectors.toList());

        searchAdapter = new RecommendSearchAdapter(context, this);
        binding.rvRecommendSearch.setAdapter(searchAdapter);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    ViewUtils.gone(binding.layoutRecommend);
                    ViewUtils.show(binding.rvRecommendSearch);
                    viewModel.autoCompleteSearchFromJson(movieNames, s.toString());
                    viewModel.movieNamesLD().observe(getViewLifecycleOwner(), _names -> {
                        Log.d(TAG, "onTextChanged: " + _names.size() + " " + _names);
                        searchAdapter.renewItems(_names);
                    });
                } else {
                    ViewUtils.show(binding.layoutRecommend);
                    ViewUtils.gone(binding.rvRecommendSearch);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.etSearch.getText() != null) {
                    hideSoftInput(binding.etSearch);
                    Bundle bundle = new Bundle();
                    bundle.putString("search", binding.etSearch.getText().toString().trim());
                    callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
                }
                return true;
            }
            return false;
        });

        binding.layoutSearch.setStartIconOnClickListener(view -> callBack.backToPrev());
    }


    @Override
    protected FragmentSearchBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    public void selectTrending(String name) {
        hideSoftInput(binding.etSearch);
        Bundle bundle = new Bundle();
        bundle.putString("search", name);
        callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoSearch(String name) {
        hideSoftInput(binding.etSearch);
        Bundle bundle = new Bundle();
        bundle.putString("search", name);
        callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }
}
