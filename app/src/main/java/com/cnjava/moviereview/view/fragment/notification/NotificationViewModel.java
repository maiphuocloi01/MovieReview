package com.cnjava.moviereview.view.fragment.notification;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.view.fragment.cast.CastViewModel;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class NotificationViewModel extends BaseViewModel {

    private static final String TAG = NotificationViewModel.class.getSimpleName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public NotificationViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<CastDetail> castDetailLD = new MutableLiveData<>();
    private final MutableLiveData<Movie> nowPlayingMovieLD = new MutableLiveData<>();
    public LiveData<CastDetail> castDetailLD() {
        return castDetailLD;
    }
    public LiveData<Movie> nowPlayingMovieLD() {
        return nowPlayingMovieLD;
    }

    /*public void getCastDetail(String creditId) {
        mLiveDataIsLoading.setValue(true);
        movieRepository.getCastDetail(creditId).subscribe(new CustomObserver<CastDetail>() {
            @Override
            public void onSuccess(@NonNull CastDetail castDetail) {
                castDetailLD.setValue(castDetail);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
            }
        });
    }*/
}
