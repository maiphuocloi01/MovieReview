package com.cnjava.moviereview.view.fragment.editprofile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.view.fragment.home.HomeViewModel;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

@HiltViewModel
public class EditProfileViewModel extends BaseViewModel {
    private static final String TAG = EditProfileViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public EditProfileViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<ResponseBody> uploadImageAccountLD = new MutableLiveData<>();

    public LiveData<ResponseBody> uploadImageAccountLD() {
        return uploadImageAccountLD;
    }

    public void uploadImageAccount(String token, MultipartBody.Part part, RequestBody someData) {
        mLiveDataIsLoading.postValue(true);
        accountRepository.uploadImage(token, part, someData).subscribe(new CustomSingleObserver<ResponseBody>() {
            @Override
            public void onSuccess(@NonNull ResponseBody responseBody) {
                uploadImageAccountLD.postValue(responseBody);
                mLiveDataIsLoading.postValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.postValue(false);
            }
        });
    }

    public void updateProfile(User user, String token) {
        mLiveDataIsLoading.postValue(true);
        accountRepository.updateProfile(user, token).subscribe(new CustomSingleObserver<Void>() {
            @Override
            public void onSuccess(@NonNull Void noResponse) {
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
