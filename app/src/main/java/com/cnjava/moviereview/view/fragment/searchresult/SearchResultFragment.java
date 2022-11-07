package com.cnjava.moviereview.view.fragment.searchresult;

import static com.cnjava.moviereview.util.IMEUtils.hideSoftInput;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentSearchResultBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.StringConvert;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.RecommendSearchAdapter;
import com.cnjava.moviereview.view.adapter.ResultAdapter;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.movie.DetailFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchResultFragment extends BaseFragment<FragmentSearchResultBinding, CommonViewModel> implements PopularAdapter.MovieCallBack, RecommendSearchAdapter.RecommendSearchCallBack {

    public static final String TAG = SearchResultFragment.class.getName();
    private Object mData;
    private Movie movie;
    private String strSearch = "";
    private Movie movieRecommend;
    private static final int DEFAULT_ID = 675353;
    private static final int MSG_START_SEARCH = 1;
    private static final int MSG_ADD_SEARCH = 0;
    private RecommendSearchAdapter searchAdapter;
    private List<String> names = new ArrayList<>();
    private final Handler mHandler = new Handler(message -> {
        if(message.what == MSG_START_SEARCH){
            searchAdapter.deleteAllItem();
        }
        if (message.what == MSG_ADD_SEARCH) {
            Bundle newData = (Bundle) message.getData();
            String name = newData.getString("name");
            Log.d(TAG, "Name: " + name);
            searchAdapter.addItem(name);
        }
        return false;
    });

    private List<String> movieNames = new ArrayList<>();
    private String textSearch = "";
    private final Runnable rb = new Runnable() {
        @Override
        public void run() {
            int countName = 0;

            Message msg1 = new Message();
            msg1.what = MSG_START_SEARCH;
            msg1.arg1 = 1;
            msg1.setTarget(mHandler);
            msg1.sendToTarget();

            for (String name : movieNames) {
                if (StringConvert.removeDiacriticalMarks(name).toLowerCase().contains(textSearch.toLowerCase())) {
                    //names.add(name);
                    Message msg = new Message();
                    msg.what = MSG_ADD_SEARCH;
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    msg.setData(bundle);
                    msg.setTarget(mHandler);
                    msg.sendToTarget();
                    countName++;
                    if (countName == 4) {
                        break;
                    }
                }
            }


        }
    };
    private Thread th;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MyApplication.getInstance().getStorage().fragmentTag = TAG;
        //if(MyApplication.getInstance().getStorage().movieSearch == null) {
        Bundle newData = (Bundle) mData;
        if(newData.getString("search") != null) {
            strSearch = newData.getString("search");
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

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.backToPrev();
            }
        });

//        } else {
//            movie = MyApplication.getInstance().getStorage().movieSearch;
//            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
//            binding.rvResult.setAdapter(resultAdapter);
//        }

        String jsonFileString = NumberUtils.getJsonFromAssets(context, "movieName.json");
        Gson gson = new Gson();
        Type listMovieNameType = new TypeToken<List<MovieName>>() {
        }.getType();
        List<MovieName> listMovieName = gson.fromJson(jsonFileString, listMovieNameType);
        movieNames = listMovieName.stream()
                .map(movieName -> movieName.name)
                .collect(Collectors.toList());
        //Log.d(TAG, "initViews: " + movieNames.size() + " " + movieNames.get(0));
        searchAdapter = new RecommendSearchAdapter(context, this);
        binding.rvRecommendSearch.setAdapter(searchAdapter);
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int countName = 0;
                if (!s.toString().equals("")) {
                    ViewUtils.gone(binding.layoutSearchResult);
                    ViewUtils.show(binding.rvRecommendSearch);

                    textSearch = s.toString();
                    if (th == null || !th.isAlive()) {
                        th = new Thread(rb);
                        th.start();
                    }


                } else {
                    ViewUtils.show(binding.layoutSearchResult);
                    ViewUtils.gone(binding.rvRecommendSearch);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
            ViewUtils.show(binding.layoutSearchResult);
            ViewUtils.gone(binding.rvRecommendSearch);
            if (movie.results.size() > 0) {
                binding.tvCountMovie.setText(String.format("Found %s results", movie.results.size()));
                ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
                binding.rvResult.setAdapter(resultAdapter);
            } else {
                binding.tvCountMovie.setText(String.format("No results for %s", strSearch));
                binding.tvCountMovie.append(String.format("\nHere are some movies that you might like", strSearch));
                if (MyApplication.getInstance().getStorage().movieRecommend == null) {
                    //Log.d(TAG, "call api: ");
                    viewModel.getRecommendation(DEFAULT_ID);
                } else {
                    //Log.d(TAG, "storage: ");
                    ViewUtils.show(binding.rvRecommend);
                    ViewUtils.show(binding.tvRecommend);
                    movieRecommend = MyApplication.getInstance().getStorage().movieRecommend;
                    MovieAdapter adapter = new MovieAdapter(context, movieRecommend, this);
                    binding.rvRecommend.setAdapter(adapter);
                }
            }
            //MyApplication.getInstance().getStorage().movieSearch = movie;
        } else if(key.equals(Constants.KEY_GET_MOVIE_BY_KEYWORD_ID)){
            movie = (Movie) data;
            binding.tvCountMovie.setText(String.format("Found top %s results", movie.results.size()));
            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
            binding.rvResult.setAdapter(resultAdapter);
        } else if(key.equals(Constants.KEY_SEARCH_MOVIE_BY_CATEGORY)){
            movie = (Movie) data;
            binding.tvCountMovie.setText(String.format("Found top %s results", movie.results.size()));
            ResultAdapter resultAdapter = new ResultAdapter(context, movie, this);
            binding.rvResult.setAdapter(resultAdapter);
        } else if (key.equals(Constants.KEY_GET_RECOMMENDATION)) {
            movieRecommend = (Movie) data;
            ViewUtils.show(binding.rvRecommend);
            ViewUtils.show(binding.tvRecommend);
            MyApplication.getInstance().getStorage().movieRecommend = movieRecommend;
            MovieAdapter adapter = new MovieAdapter(context, movieRecommend, this);
            binding.rvRecommend.setAdapter(adapter);
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

    @Override
    public void gotoSearch(String name) {
        hideSoftInput(binding.etSearch);
        binding.etSearch.setText(name);
        viewModel.searchMovie(name);
    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        MyApplication.getInstance().getStorage().movieSearch = null;
//    }
}
