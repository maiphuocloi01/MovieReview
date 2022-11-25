package com.cnjava.moviereview.view.fragment.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Statistic;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.view.fragment.home.HomeViewModel;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class ProfileViewModel extends BaseViewModel {
    private static final String TAG = HomeViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public ProfileViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<Statistic> myStatisticsLD = new MutableLiveData<>();


    public LiveData<Statistic> myStatisticsLD() {
        return myStatisticsLD;
    }




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

    public void deleteReview(String reviewId, String token) {
        accountRepository.deleteReview(reviewId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void dislikeReview(String reviewId, String token) {
        accountRepository.dislikeReview(reviewId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void likeReview(String reviewId, String token) {
        accountRepository.likeReview(reviewId, token).subscribe(new CustomCompletableObserver() {
            @Override
            public void onComplete() {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void getMyStatistics(String token) {
        accountRepository.getMyStatistics(token).subscribe(new CustomSingleObserver<Statistic>() {
            @Override
            public void onSuccess(@NonNull Statistic statistic) {
                myStatisticsLD.postValue(statistic);
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
