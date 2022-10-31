package com.cnjava.moviereview.view.fragment.reviewdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cnjava.moviereview.model.Translate;
import com.cnjava.moviereview.repository.Repository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class ReviewDetailViewModel extends BaseViewModel {

    private static final String TAG = "ReviewDetailViewModel";

    private final Repository repository;

    private static MutableLiveData<Translate> translateMutableLiveData = new MutableLiveData<>();

    public LiveData<Translate> translateLiveData() {
        return translateMutableLiveData;
    }

    @Inject
    public ReviewDetailViewModel(Repository repository) {
        this.repository = repository;
    }

    public void translateText(String text) {
        repository.translateText(text).subscribe(new TranslateObserver<Translate>() {
            @Override
            public void onSuccess(@NonNull Translate translate) {
                translateMutableLiveData.setValue(translate);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                String message = e.getMessage();
                Log.d(TAG, "onError: " + message);
            }
        });
        /*repository.translateText(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translate -> translateMutableLiveData.setValue(translate), throwable -> Log.d(TAG, "translate: " + throwable));*/
    }
}
