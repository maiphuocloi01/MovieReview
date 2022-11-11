package com.cnjava.moviereview.view.fragment.review;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.view.fragment.detailmovie.DetailViewModel;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class ReviewViewModel extends BaseViewModel {

    private static final String TAG = ReviewViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;
    private SavedStateHandle state;

    private final MutableLiveData<User> yourProfileLD = new MutableLiveData<>();

    /*public LiveData<List<Review>> movieReviewLD() {
        return movieReviewLD;
    }*/
    public LiveData<User> yourProfileLD() {
        return yourProfileLD;
    }


    @Inject
    public ReviewViewModel(MovieRepository movieRepository, AccountRepository accountRepository, SavedStateHandle savedStateHandle) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
        this.state = savedStateHandle;
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

    public void getReviewByMovieId(String movieId) {
        accountRepository.getReviewByMovieId(movieId).subscribe(new CustomSingleObserver<List<Review>>() {
            @Override
            public void onSuccess(@NonNull List<Review> reviews) {
                movieReviewLD.postValue(reviews);
                //state.set(DetailViewModel.REVIEW_LIST, reviews);
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
