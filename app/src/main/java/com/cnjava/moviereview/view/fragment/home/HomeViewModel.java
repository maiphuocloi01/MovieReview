package com.cnjava.moviereview.view.fragment.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class HomeViewModel extends BaseViewModel {

    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public HomeViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<Movie> popularMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> nowPlayingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> upcomingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> topRatedMovieLD = new MutableLiveData<>();
    private final MutableLiveData<User> yourProfileLD = new MutableLiveData<>();

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

    public LiveData<User> yourProfileLD() {
        return yourProfileLD;
    }


    public void getPopularMovie() {
        mLiveDataIsLoading.setValue(true);
        movieRepository.getPopularMovie().subscribe(new CustomSingleObserver<Movie>() {
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
        movieRepository.getNowPlayingMovie().subscribe(new CustomSingleObserver<Movie>() {
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
        movieRepository.getUpComingMovie().subscribe(new CustomSingleObserver<Movie>() {
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
        movieRepository.getTopRatedMovie().subscribe(new CustomSingleObserver<Movie>() {
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

    public void getYourProfile(String token) {
        mLiveDataIsLoading.setValue(true);
        accountRepository.getMyProfile(token).subscribe(new CustomSingleObserver<User>() {
            @Override
            public void onSuccess(@NonNull User user) {
                yourProfileLD.setValue(user);
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
