package com.cnjava.moviereview.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cnjava.moviereview.model.Review;

import java.util.List;

public class ShareViewModel extends ViewModel {

    private static final String TAG = ShareViewModel.class.getName();

    private MutableLiveData<List<Review>> listReview = new MutableLiveData<>();

    public void setListReview(List<Review> input) {
        Log.d(TAG, "setListReview: ");
        listReview.setValue(input);
    }

    public MutableLiveData<List<Review>> getListReview() {
        Log.d(TAG, "getListReview: ");
        return listReview;
    }
}
