package com.cnjava.moviereview.view.fragment.reviewdetail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cnjava.moviereview.model.Translate;
import com.cnjava.moviereview.repository.Repository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


@HiltViewModel
public class ReviewDetailViewModel extends ViewModel {

    private static final String TAG = "ReviewDetailViewModel";

    private final Repository repository;

    private static MutableLiveData<Translate> translateMutableLiveData = new MutableLiveData<>();
    public static LiveData<Translate> translateLiveData = translateMutableLiveData;

    @Inject
    public ReviewDetailViewModel(Repository repository) {
        this.repository = repository;
    }

    public void translateText(String text) {
        repository.translateText(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(translate -> translateMutableLiveData.setValue(translate), throwable -> Log.d(TAG, "translate: " + throwable));
    }
}
