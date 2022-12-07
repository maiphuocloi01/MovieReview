package com.cnjava.moviereview.view.fragment.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.model.UserResponse;
import com.cnjava.moviereview.repository.AccountRepository;
import com.cnjava.moviereview.repository.MovieRepository;
import com.cnjava.moviereview.viewmodel.BaseViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class LoginViewModel extends BaseViewModel {
    private static final String TAG = LoginViewModel.class.getName();

    private final MovieRepository movieRepository;
    private final AccountRepository accountRepository;

    @Inject
    public LoginViewModel(MovieRepository movieRepository, AccountRepository accountRepository) {
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
    }

    private final MutableLiveData<UserResponse> loginLD = new MutableLiveData<>();

    public LiveData<UserResponse> loginLD() {
        return loginLD;
    }

    private final MutableLiveData<User> loginWithGoogleLD = new MutableLiveData<>();

    public LiveData<User> loginWithGoogleLD() {
        return loginWithGoogleLD;
    }

    public void loginUser(String email, String password) {
        mLiveDataIsLoading.setValue(true);
        accountRepository.login(email, password).subscribe(new CustomSingleObserver<UserResponse>() {
            @Override
            public void onSuccess(@NonNull UserResponse userResponse) {
                loginLD.setValue(userResponse);
                mLiveDataIsLoading.setValue(false);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
                mLiveDataOnError.setValue(e);
            }
        });
    }

    public void loginWithGoogle(User user) {
        mLiveDataIsLoading.setValue(true);
        accountRepository.loginWithGoogle(user).subscribe(new CustomSingleObserver<User>() {
            @Override
            public void onSuccess(@NonNull User userResponse) {
                loginWithGoogleLD.setValue(userResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
                mLiveDataIsLoading.setValue(false);
                mLiveDataOnError.setValue(e);
            }
        });
    }
}
