package com.cnjava.moviereview.view.fragment;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentSearchResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.ResultAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends BaseFragment<FragmentSearchResultBinding, CommonViewModel> implements PopularAdapter.MovieCallBack {

    public static final String TAG = SearchResultFragment.class.getName();
    private Object mData;
    private Movie movie;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        //if(MyApplication.getInstance().getStorage().movieSearch == null) {
        Bundle newData = (Bundle) mData;
        if(newData.getString("search") != null) {
            String strSearch = newData.getString("search");
            binding.etSearch.setText(strSearch);
            Log.d(TAG, "search: " + strSearch);
            if (!strSearch.equals("")) {
                viewModel.searchMovie(strSearch);
            }
        } else if(newData.getInt("keyword") != 0){
            Log.d(TAG, "keyword: ");
            int id = newData.getInt("keyword", 0);
            if (id != 0){
                viewModel.getMovieByKeywordId(id);
            }
        } else if(newData.getString("category") != null){
            Log.d(TAG, "category: ");
            String category = newData.getString("category");
            if (!category.equals("")){
                viewModel.getMovieByCategory(category);
            }
        }

//        } else {
//            movie = MyApplication.getInstance().getStorage().movieSearch;
//            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
//            binding.rvResult.setAdapter(resultAdapter);
//        }


        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftInput(binding.etSearch);
                    viewModel.searchMovie(v.getText().toString().trim());
                    return true;
                }
                return false;
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
            binding.tvCountMovie.setText(String.format("Found %s results", movie.results.size()));
            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
            binding.rvResult.setAdapter(resultAdapter);
            //MyApplication.getInstance().getStorage().movieSearch = movie;
        } else if(key.equals(Constants.KEY_GET_MOVIE_BY_KEYWORD_ID)){
            movie = (Movie) data;
            binding.tvCountMovie.setText(String.format("Found %s results", movie.results.size()));
            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
            binding.rvResult.setAdapter(resultAdapter);
        } else if(key.equals(Constants.KEY_SEARCH_MOVIE_BY_CATEGORY)){
            movie = (Movie) data;
            binding.tvCountMovie.setText(String.format("Found %s results", movie.results.size()));
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
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
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
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        MyApplication.getInstance().getStorage().movieSearch = null;
//    }
}
