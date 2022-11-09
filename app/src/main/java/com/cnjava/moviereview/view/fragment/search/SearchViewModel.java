package com.cnjava.moviereview.view.fragment.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.repository.MovieRepository;
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

    private final MutableLiveData<Movie> trendingWeekLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> trendingDayLD = new MutableLiveData<>();
    private final MutableLiveData<MovieName> movieNameLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> movieResultLD = new MutableLiveData<>();

    public LiveData<Movie> trendingWeekLD() {
        return trendingWeekLD;
    }

    public LiveData<Movie> trendingDayLD() {
        return trendingDayLD;
    }

    public LiveData<MovieName> movieNameLD() {
        return movieNameLD;
    }

    public LiveData<Movie> movieResultLD() {
        return movieResultLD;
    }

    @Inject
    public SearchViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void getTrending(String timeWindow) {
        mLiveDataIsLoading.setValue(true);
        movieRepository.getTrending(timeWindow).subscribe(new CustomObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                if (timeWindow.equals("day")) {
                    trendingDayLD.setValue(movie);
                } else if (timeWindow.equals("week")) {
                    trendingWeekLD.setValue(movie);
                }
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void autoCompleteSearch(String searchView) {
        mLiveDataIsLoading.setValue(true);
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
                        movieNameLD.setValue(movieName);
                        mLiveDataIsLoading.setValue(false);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mLiveDataIsLoading.setValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void searchMovie(String text) {
        mLiveDataIsLoading.setValue(true);
        movieRepository.searchMovie(text).subscribe(new CustomObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                movieResultLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });

    }
}
