package com.cnjava.moviereview.view.fragment.reviewdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Translate;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;


@HiltViewModel
public class ReviewDetailViewModel extends BaseViewModel {

    private static final String TAG = "ReviewDetailViewModel";

    private final MovieRepository movieRepository;

    private static MutableLiveData<Translate> translateMutableLiveData = new MutableLiveData<>();

    public LiveData<Translate> translateLiveData() {
        return translateMutableLiveData;
    }

    @Inject
    public ReviewDetailViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void translateText(String text) {
        mLiveDataIsLoading.setValue(true);
        movieRepository.translateText(text).subscribe(new TranslateObserver<Translate>() {
            @Override
            public void onSuccess(@NonNull Translate translate) {
                translateMutableLiveData.setValue(translate);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                String message = e.getMessage();
                Log.d(TAG, "onError: " + message);
                mLiveDataIsLoading.setValue(false);
            }
        });
        /*repository.translateText(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translate -> translateMutableLiveData.setValue(translate), throwable -> Log.d(TAG, "translate: " + throwable));*/
    }
}
