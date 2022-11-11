package com.cnjava.moviereview.view.fragment.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.util.cutom.SingleLiveEvent;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class SearchViewModel extends BaseViewModel {

    private static final String TAG = SearchViewModel.class.getName();

    private final MovieRepository movieRepository;
    private SavedStateHandle state;

    private final MutableLiveData<Movie> trendingWeekLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> trendingDayLD = new MutableLiveData<>();
    private final MutableLiveData<MovieName> movieNameLD = new MutableLiveData<>();
    public final SingleLiveEvent<Movie> movieResultLD = new SingleLiveEvent<>();
    private String textSearch = "";

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public LiveData<Movie> trendingWeekLD() {
        return trendingWeekLD;
    }

    public LiveData<Movie> trendingDayLD() {
        return trendingDayLD;
    }

    public LiveData<MovieName> movieNameLD() {
        return movieNameLD;
    }
    /*public SingleLiveEvent<Movie> movieResultLD() {
        return movieResultLD;
    }*/

    @Inject
    public SearchViewModel(MovieRepository movieRepository, SavedStateHandle savedStateHandle) {
        this.movieRepository = movieRepository;
        this.state = savedStateHandle;
    }

    public void getTrending(String timeWindow) {
        mLiveDataIsLoading.postValue(true);
        movieRepository.getTrending(timeWindow).subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                if (timeWindow.equals("day")) {
                    trendingDayLD.postValue(movie);
                } else if (timeWindow.equals("week")) {
                    trendingWeekLD.postValue(movie);
                }
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void autoCompleteSearch(String searchView) {
        movieRepository.autoCompleteSearch(searchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(new Observer<MovieName>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mMainCompDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull MovieName movieName) {
                        movieNameLD.postValue(movieName);
                        mLiveDataIsLoading.postValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mLiveDataIsLoading.postValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void searchMovie(String text) {
        mLiveDataIsLoading.postValue(true);
        movieRepository.searchMovie(text).subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                movieResultLD.postValue(movie);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });

    }
}
