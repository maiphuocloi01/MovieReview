package com.cnjava.moviereview.view.fragment;

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
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.databinding.FragmentSearchBinding;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.IMEUtils;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.StringConvert;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.RecommendSearchAdapter;
import com.cnjava.moviereview.view.adapter.TrendingAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchFragment extends BaseFragment<FragmentSearchBinding, CommonViewModel> implements TrendingAdapter.TrendingCallBack, PopularAdapter.MovieCallBack, RecommendSearchAdapter.RecommendSearchCallBack {

    public static final String TAG = SearchFragment.class.getName();

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
    private Movie movieRecommend;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        if (MyApplication.getInstance().getStorage().moviePopular != null) {
            int count = 0;
            Movie popularMovie = MyApplication.getInstance().getStorage().moviePopular;
            List<String> nameTrending = new ArrayList<>();
            for (Movie.Result item : popularMovie.results) {
                if (count < 4) {
                    nameTrending.add(item.title);
                    count++;
                } else {
                    break;
                }
            }
            //Log.d(TAG, "initViews: " + nameTrending.get(0));
            TrendingAdapter trendingAdapter = new TrendingAdapter(context, nameTrending, this);
            binding.rvTrending.setAdapter(trendingAdapter);
        }

        if (MyApplication.getInstance().getStorage().movieRecommend == null) {
            //Log.d(TAG, "call api: ");
            viewModel.getRecommendation(DEFAULT_ID);
        } else {
            //Log.d(TAG, "storage: ");
            movieRecommend = MyApplication.getInstance().getStorage().movieRecommend;
            MovieAdapter adapter = new MovieAdapter(context, movieRecommend, this);
            binding.rvRecommend.setAdapter(adapter);
        }

        if (binding.etSearch.requestFocus()) {
            IMEUtils.showSoftInput(binding.etSearch);
        }

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
                /*if(!s.toString().equals("")) {
                    ViewUtils.gone(binding.layoutRecommend);
                    ViewUtils.show(binding.rvRecommendSearch);
                    Observable.fromIterable(movieNames)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .filter(item -> StringConvert.removeDiacriticalMarks(item).toLowerCase().contains(s.toString().toLowerCase()))
                            .toList()
                            .subscribe(new SingleObserver<List<String>>() {

                                @Override
                                public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<String> strings) {
                                    Log.d(TAG, "onSuccess: " + strings.size());
                                    searchAdapter.renewItems(strings);
                                }

                                @Override
                                public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

                                }
                            });
                } else {
                    ViewUtils.show(binding.layoutRecommend);
                    ViewUtils.gone(binding.rvRecommendSearch);
                }*/


                int countName = 0;
                if (!s.toString().equals("")) {
                    ViewUtils.gone(binding.layoutRecommend);
                    ViewUtils.show(binding.rvRecommendSearch);
//                    for (String name : movieNames) {
//                        if (StringConvert.removeDiacriticalMarks(name).toLowerCase().contains(s.toString().toLowerCase())) {
//                            names.add(name);
//                            countName++;
//                            if (countName == 4) {
//                                break;
//                            }
//                        }
//                    }
                    textSearch = s.toString();
                    if (th == null || !th.isAlive()) {
                        th = new Thread(rb);
                        th.start();
                    }


                } else {
                    ViewUtils.show(binding.layoutRecommend);
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
                    Bundle bundle = new Bundle();
                    bundle.putString("search", binding.etSearch.getText().toString().trim());
                    callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
                    //actionShowFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
                    return true;
                }
                return false;
            }
        });

        binding.layoutSearch.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });
    }


    @Override
    protected FragmentSearchBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentSearchBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_RECOMMENDATION)) {
            movieRecommend = (Movie) data;
            MyApplication.getInstance().getStorage().movieRecommend = movieRecommend;
            MovieAdapter adapter = new MovieAdapter(context, movieRecommend, this);
            binding.rvRecommend.setAdapter(adapter);
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    /*private void actionShowFragment(String tag, Object data, boolean isBack, int anim) {
        NavigateFragment parentFrag = ((NavigateFragment) SearchFragment.this.getParentFragment());
        if (parentFrag != null) {
            parentFrag.setActionShowFragment(tag, data, isBack, anim);
        }
    }*/

    @Override
    public void selectTrending(String name) {
        hideSoftInput(binding.etSearch);
        Bundle bundle = new Bundle();
        bundle.putString("search", name);
        callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
        //actionShowFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
        //actionShowFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoSearch(String name) {
        hideSoftInput(binding.etSearch);
        Bundle bundle = new Bundle();
        bundle.putString("search", name);
        callBack.showFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
        //actionShowFragment(SearchResultFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
    }
}
