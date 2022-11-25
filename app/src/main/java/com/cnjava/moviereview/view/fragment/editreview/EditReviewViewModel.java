package com.cnjava.moviereview.view.fragment.editreview;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.view.fragment.notification.NotificationViewModel;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EditReviewViewModel extends BaseViewModel {

    private static final String TAG = EditReviewViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public EditReviewViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    /*private final MutableLiveData<CastDetail> castDetailLD = new MutableLiveData<>();
    public LiveData<CastDetail> castDetailLD() {
        return castDetailLD;
    }*/

    public void updateReview(String reviewId, Review review, String token) {
        mLiveDataIsLoading.setValue(true);
        accountRepository.updateReview(reviewId, review, token).subscribe(new CustomSingleObserver<Review>() {
            @Override
            public void onSuccess(@NonNull Review review1) {
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mLiveDataIsLoading.setValue(false);
            }
        });
    }
}
