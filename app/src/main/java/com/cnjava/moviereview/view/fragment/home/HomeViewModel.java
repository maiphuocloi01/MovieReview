package com.cnjava.moviereview.view.fragment.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

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

    private static final String TAG = HomeViewModel.class.getName();
/*    private Movie popularMovie;
    private Movie topRatedMovie;
    private Movie upcomingMovie;
    private Movie nowPlayingMovie;
    private User user;*/
    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;
    private SavedStateHandle state;

    @Inject
    public HomeViewModel(MovieRepository movieRepository, AccountRepository accountRepository, SavedStateHandle savedStateHandle) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
        this.state = savedStateHandle;
    }

    /*private final MutableLiveData<Movie> popularMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> nowPlayingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> upcomingMovieLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> topRatedMovieLD = new MutableLiveData<>();


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
    }*/


    /*public void getPopularMovie() {
        mLiveDataIsLoading.postValue(true);
        movieRepository.getPopularMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                popularMovieLD.postValue(movie);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void getNowPlayingMovie() {
        movieRepository.getNowPlayingMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                nowPlayingMovieLD.postValue(movie);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void getUpComingMovie() {
        movieRepository.getUpComingMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                upcomingMovieLD.postValue(movie);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void getTopRatedMovie() {
        movieRepository.getTopRatedMovie().subscribe(new CustomSingleObserver<Movie>() {
            @Override
            public void onSuccess(@NonNull Movie movie) {
                topRatedMovieLD.postValue(movie);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }*/

    /*public void getYourProfile(String token) {
        mLiveDataIsLoading.postValue(true);
        accountRepository.getMyProfile(token).subscribe(new CustomSingleObserver<User>() {
            @Override
            public void onSuccess(@NonNull User user) {
                yourProfileLD.postValue(user);
                mLiveDataIsLoading.postValue(false);
            }
            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }*/

}
