package com.cnjava.moviereview.view.fragment.review;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

@HiltViewModel
public class ReviewViewModel extends BaseViewModel {

    private static final String TAG = ReviewViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    private final MutableLiveData<List<Review>> listReviewLD = new MutableLiveData<>();
    private final MutableLiveData<User> yourProfileLD = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> movieReviewLD = new MutableLiveData<>();

    public LiveData<List<Review>> listReviewLD() {
        return listReviewLD;
    }

    public LiveData<User> yourProfileLD() {
        return yourProfileLD;
    }

    public LiveData<List<Review>> movieReviewLD() {
        return movieReviewLD;
    }

    @Inject
    public ReviewViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    public void getYourProfile(String token) {
        mLiveDataIsLoading.setValue(true);
        accountRepository.getMyProfile(token).subscribe(new CustomObserver<User>() {
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

    public void getReviewByMovieId(String movieId) {
        accountRepository.getReviewByMovieId(movieId).subscribe(new CustomObserver<List<Review>>() {
            @Override
            public void onSuccess(@NonNull List<Review> reviews) {
                listReviewLD.setValue(reviews);
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
