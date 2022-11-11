package com.cnjava.moviereview.view.fragment.addreview;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class AddReviewViewModel extends BaseViewModel {

    private static final String TAG = AddReviewViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    private final MutableLiveData<Review> addReviewLD = new MutableLiveData<>();

    public LiveData<Review> addReviewLD() {
        return addReviewLD;
    }


    @Inject
    public AddReviewViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    public void addReview(Review review, String token) {
        mLiveDataIsLoading.postValue(true);
        accountRepository.addReview(review, token).subscribe(new CustomSingleObserver<Review>() {
            @Override
            public void onSuccess(@NonNull Review _review) {
                addReviewLD.postValue(_review);
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
