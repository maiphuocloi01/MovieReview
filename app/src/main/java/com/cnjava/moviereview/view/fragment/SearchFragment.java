package com.cnjava.moviereview.view.fragment;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentSearchBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.TrendingAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, CommonViewModel> implements TrendingAdapter.TrendingCallBack, PopularAdapter.MovieCallBack {

    public static final String TAG = SearchFragment.class.getName();

    private static final int DEFAULT_ID = 675353;
    private Movie movieRecommend;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        if(MyApplication.getInstance().getStorage().moviePopular != null){
            int count = 0;
            Movie popularMovie = MyApplication.getInstance().getStorage().moviePopular;
            List<String> nameTrending = new ArrayList<>();
            for (Movie.Result item: popularMovie.results){
                if(count < 4) {
                    nameTrending.add(item.title);
                    count++;
                } else {
                    break;
                }
            }
            Log.d(TAG, "initViews: " + nameTrending.get(0));
            TrendingAdapter trendingAdapter = new TrendingAdapter(context, nameTrending, this);
            binding.rvTrending.setAdapter(trendingAdapter);
        }

        if(MyApplication.getInstance().getStorage().movieRecommend == null) {
            Log.d(TAG, "call api: ");
            viewModel.getRecommendation(DEFAULT_ID);
        } else {
            Log.d(TAG, "storage: ");
            movieRecommend = MyApplication.getInstance().getStorage().movieRecommend;
            MovieAdapter adapter = new MovieAdapter(context, movieRecommend, this);
            binding.rvRecommend.setAdapter(adapter);
        }

        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideSoftInput(binding.etSearch);
                    Bundle bundle = new Bundle();
                    bundle.putString("search", binding.etSearch.getText().toString().trim());
                    actionShowFragment(SearchResultFragment.TAG, bundle, true);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected FragmentSearchBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if(key.equals(Constants.KEY_GET_RECOMMENDATION)){
            movieRecommend = (Movie) data;
            MyApplication.getInstance().getStorage().movieRecommend = movieRecommend;
            MovieAdapter adapter = new MovieAdapter(context, movieRecommend, this);
            binding.rvRecommend.setAdapter(adapter);
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    private void actionShowFragment(String tag, Object data, boolean isBack) {
        NavigateFragment parentFrag = ((NavigateFragment) SearchFragment.this.getParentFragment());
        if (parentFrag != null) {
            parentFrag.setActionShowFragment(tag, data, isBack);
        }
    }

    @Override
    public void selectTrending(String name) {
        hideSoftInput(binding.etSearch);
        Bundle bundle = new Bundle();
        bundle.putString("search", name);
        actionShowFragment(SearchResultFragment.TAG, bundle, true);
    }

    @Override
    public void gotoMovieDetail(int id) {
        actionShowFragment(DetailFragment.TAG, id, true);
    }
}
