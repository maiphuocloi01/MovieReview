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

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class ProfileViewModel extends BaseViewModel {
    private static final String TAG = HomeViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public ProfileViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<Statistic> myStatisticsLD = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> reviewByUserIdLD = new MutableLiveData<>();
    private final MutableLiveData<User> yourProfileLD = new MutableLiveData<>();

    public LiveData<Statistic> myStatisticsLD() {
        return myStatisticsLD;
    }

    public LiveData<List<Review>> reviewByUserIdLD() {
        return reviewByUserIdLD;
    }

    public LiveData<User> yourProfileLD() {
        return yourProfileLD;
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

    public void getMyStatistics(String token) {
        accountRepository.getMyStatistics(token).subscribe(new CustomSingleObserver<Statistic>() {
            @Override
            public void onSuccess(@NonNull Statistic statistic) {
                myStatisticsLD.setValue(statistic);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }

    public void getReviewByUserId(String userId) {
        accountRepository.getReviewByUserId(userId).subscribe(new CustomSingleObserver<List<Review>>() {
            @Override
            public void onSuccess(@NonNull List<Review> reviews) {
                reviewByUserIdLD.setValue(reviews);
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
