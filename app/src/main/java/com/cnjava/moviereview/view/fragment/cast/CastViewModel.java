package com.cnjava.moviereview.view.fragment.cast;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.People;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

@HiltViewModel
public class CastViewModel extends BaseViewModel {

    private static final String TAG = CastViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public CastViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<CastDetail> castDetailLD = new MutableLiveData<>();
    public LiveData<CastDetail> castDetailLD() {
        return castDetailLD;
    }

    private final MutableLiveData<People> peopleDetailLD = new MutableLiveData<>();
    public LiveData<People> peopleDetailLD() {
        return peopleDetailLD;
    }

    public void getCastDetail(String creditId) {
        mLiveDataIsLoading.postValue(true);
        movieRepository.getCastDetail(creditId).subscribe(new CustomSingleObserver<CastDetail>() {
            @Override
            public void onSuccess(@NonNull CastDetail castDetail) {
                castDetailLD.postValue(castDetail);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void getPeopleDetail(String personId) {
        movieRepository.getPeopleDetail(personId).subscribe(new CustomSingleObserver<People>() {
            @Override
            public void onSuccess(@NonNull People people) {
                peopleDetailLD.postValue(people);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

}
