package com.cnjava.moviereview.view.fragment.searchresult;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentSearchResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.RecommendSearchAdapter;
import com.cnjava.moviereview.view.adapter.ResultAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailMovieFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

public class SearchResultFragment extends BaseFragment<FragmentSearchResultBinding, CommonViewModel> implements PopularAdapter.MovieCallBack, RecommendSearchAdapter.RecommendSearchCallBack {

    public static final String TAG = SearchResultFragment.class.getName();
    private Object mData;
    private Movie movie;
    private String strSearch = "";

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        //if(MyApplication.getInstance().getStorage().movieSearch == null) {
        Bundle newData = (Bundle) mData;
        if (newData.getString("search") != null) {
            strSearch = newData.getString("search");
            binding.etSearch1.setQuery(strSearch, false);
            Log.d(TAG, "search: " + strSearch);
            if (!strSearch.equals("")) {
                viewModel.searchMovie(strSearch);
            }
        } else if (newData.getInt("keyword") != 0) {
            Log.d(TAG, "keyword: ");
            int id = newData.getInt("keyword", 0);
            if (id != 0) {
                viewModel.getMovieByKeywordId(id);
            }
        } else if (newData.getString("category") != null) {
            Log.d(TAG, "category: ");
            String category = newData.getString("category");
            if (!category.equals("")) {
                viewModel.getMovieByCategory(category);
            }
        }
        ImageView searchIcon = binding.etSearch1.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = binding.etSearch1.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchIcon.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
        closeIcon.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
        binding.etSearch1.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
                IMEUtils.showSoftInput(binding.etSearch1);
            }
        });

    }

    @Override
    protected FragmentSearchResultBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchResultBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_SEARCH_MOVIE)) {
            movie = (Movie) data;
            ViewUtils.show(binding.layoutSearchResult);
            if (movie.results.size() > 0) {
                binding.tvCountMovie.setText(String.format("Found %s results", movie.results.size()));
                ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
                binding.rvResult.setAdapter(resultAdapter);
            } else {
                binding.tvCountMovie.setText(String.format("No results for %s", strSearch));
                binding.tvCountMovie.append(String.format("\nHere are some movies that you might like", strSearch));
            }
            //MyApplication.getInstance().getStorage().movieSearch = movie;
        } else if (key.equals(Constants.KEY_GET_MOVIE_BY_KEYWORD_ID)) {
            movie = (Movie) data;
            binding.tvCountMovie.setText(String.format("Found top %s results", movie.results.size()));
            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
            binding.rvResult.setAdapter(resultAdapter);
        } else if (key.equals(Constants.KEY_SEARCH_MOVIE_BY_CATEGORY)) {
            movie = (Movie) data;
            binding.tvCountMovie.setText(String.format("Found top %s results", movie.results.size()));
            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
            binding.rvResult.setAdapter(resultAdapter);
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {
            Log.d(TAG, "apiError: " + data.toString());
            Toast.makeText(context, "Unable connect to server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void gotoSearch(String name) {
        hideSoftInput(binding.etSearch1);
        binding.etSearch1.setQuery(name, false);
        viewModel.searchMovie(name);
    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        MyApplication.getInstance().getStorage().movieSearch = null;
//    }
}
