package com.cnjava.moviereview.view.activity.main;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.util.cutom.SingleLiveEvent;
import com.cnjava.moviereview.viewmodel.BaseViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class MainViewModel extends BaseViewModel {
    private static final String TAG = MainViewModel.class.getName();

    private Fragment fragment;

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    private MutableLiveData<User> yourProfileLD = new MutableLiveData<>();

    public LiveData<User> yourProfileLD() {
        return yourProfileLD;
    }

    public void clearYourProfile() {
        yourProfileLD.postValue(null);
    }

    private final MutableLiveData<Movie> popularMovieLD = new MutableLiveData<>();

    public LiveData<Movie> popularMovieLD() {
        return popularMovieLD;
    }

    private final MutableLiveData<Movie> nowPlayingMovieLD = new MutableLiveData<>();

    public LiveData<Movie> nowPlayingMovieLD() {
        return nowPlayingMovieLD;
    }

    private final MutableLiveData<Movie> upcomingMovieLD = new MutableLiveData<>();

    public LiveData<Movie> upcomingMovieLD() {
        return upcomingMovieLD;
    }

    private final MutableLiveData<Movie> topRatedMovieLD = new MutableLiveData<>();

    public LiveData<Movie> topRatedMovieLD() {
        return topRatedMovieLD;
    }

    private final SingleLiveEvent<List<Review>> movieReviewLD = new SingleLiveEvent<>();

    public LiveData<List<Review>> movieReviewLD() {
        return movieReviewLD;
    }

    public void clearMovieReview() {
        movieReviewLD.postValue(null);
    }

    private final MutableLiveData<List<Review>> reviewByUserIdLD = new MutableLiveData<>();

    public LiveData<List<Review>> reviewByUserIdLD() {
        return reviewByUserIdLD;
    }

    public void clearReviewByUserId() {
        reviewByUserIdLD.postValue(null);
    }


    private int movieId = 0;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    private final MutableLiveData<FirebaseUser> loginSuccessLD = new MutableLiveData<>();

    public LiveData<FirebaseUser> getLoginSuccessLD() {
        return loginSuccessLD;
    }

    public void setLoginSuccessLD(FirebaseUser user) {
        loginSuccessLD.postValue(user);
    }


    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public MainViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    public void getYourProfile(String token) {
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
    }

    public void getPopularMovie() {
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
    }

    public void getReviewByMovieId(String movieId) {
        accountRepository.getReviewByMovieId(movieId).subscribe(new CustomSingleObserver<List<Review>>() {
            @Override
            public void onSuccess(@NonNull List<Review> reviews) {
                movieReviewLD.postValue(reviews);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void getReviewByUserId(String userId) {
        accountRepository.getReviewByUserId(userId)
                .subscribe(new CustomSingleObserver<List<Review>>() {
                    @Override
                    public void onSuccess(@NonNull List<Review> reviews) {
                        Collections.reverse(reviews);
                        reviewByUserIdLD.postValue(reviews);
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
