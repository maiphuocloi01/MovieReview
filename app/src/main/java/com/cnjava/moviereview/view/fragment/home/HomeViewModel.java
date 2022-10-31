package com.cnjava.moviereview.view.fragment.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.repository.Repository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class HomeViewModel extends BaseViewModel {

    private static final String TAG = "HomeViewModel";

    private final Repository repository;

    @Inject
    public HomeViewModel(Repository repository) {
        this.repository = repository;
    }

    private static MutableLiveData<Movie> popularMovieLD = new MutableLiveData<>();
    private static MutableLiveData<Movie> nowPlayingMovieLD = new MutableLiveData<>();
    private static MutableLiveData<Movie> upcomingMovieLD = new MutableLiveData<>();
    private static MutableLiveData<Movie> topRatedMovieLD = new MutableLiveData<>();

    public LiveData<Movie> popularMovieLD() {
        return popularMovieLD;
    }

    public LiveData<Movie> nowPlayingMovieLD() {
        return nowPlayingMovieLD;
    }

    public LiveData<Movie> upcomingMovieLD() {
        return upcomingMovieLD;
    }

    public LiveData<Movie> topRatedMovieLD() {
        return topRatedMovieLD;
    }


    public void getPopularMovie() {
        mLiveDataIsLoading.setValue(true);
        repository.getPopularMovie().subscribe(new MovieObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                popularMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getNowPlayingMovie() {
        repository.getNowPlayingMovie().subscribe(new MovieObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                nowPlayingMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getUpComingMovie() {
        repository.getUpComingMovie().subscribe(new MovieObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                upcomingMovieLD.setValue(movie);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getTopRatedMovie() {
        repository.getTopRatedMovie().subscribe(new MovieObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                topRatedMovieLD.setValue(movie);
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
