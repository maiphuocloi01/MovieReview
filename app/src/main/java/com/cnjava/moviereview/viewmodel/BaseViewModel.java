package com.cnjava.moviereview.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cnjava.moviereview.data.Api;
import com.cnjava.moviereview.data.UserApi;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Translate;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.callback.OnAPICallBack;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseViewModel extends ViewModel {

    private static final String TAG = BaseViewModel.class.getName();

    protected OnAPICallBack callBack;

    protected CompositeDisposable mMainCompDisposable = new CompositeDisposable();

    protected MutableLiveData<Boolean> mLiveDataIsLoading = new MutableLiveData<>();
    protected MutableLiveData<Throwable> mLiveDataOnError = new MutableLiveData<>();

    public LiveData<Boolean> getLiveDataIsLoading() {
        return mLiveDataIsLoading;
    }

    public LiveData<Throwable> getLiveDataOnError() {
        return mLiveDataOnError;
    }

    public abstract class TranslateObserver<T extends Translate> implements SingleObserver<T> {
        @Override
        public void onSubscribe(Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(Throwable e) {
            mLiveDataOnError.setValue(e);
            e.printStackTrace();
        }
    }

    public abstract class MovieObserver<T extends Movie> implements SingleObserver<T> {
        @Override
        public void onSubscribe(Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(Throwable e) {
            mLiveDataOnError.setValue(e);
            e.printStackTrace();
        }
    }

    public abstract class UserObserver<T extends User> implements SingleObserver<T> {
        @Override
        public void onSubscribe(Disposable d) {
            mMainCompDisposable.add(d);
        }

        @Override
        public void onError(Throwable e) {
            mLiveDataOnError.setValue(e);
            e.printStackTrace();
        }
    }

    public void setCallBack(OnAPICallBack callBack) {
        this.callBack = callBack;
    }

    protected Api getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_USER  )
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build())
                .build();
        return retrofit.create(Api.class);
    }

    protected UserApi getUserApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_USER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build())
                .build();
        return retrofit.create(UserApi.class);
    }

    protected UserApi uploadImageApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_UPlOAD_IMAGE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build())
                .build();
        return retrofit.create(UserApi.class);
    }

    protected UserApi aiApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_AI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build())
                .build();
        return retrofit.create(UserApi.class);
    }

    protected <T> Callback<T> initHandleResponse(String key) {
        return new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "initHandleResponse: " + response.body());
                    handleSuccess(key, response.body());
                } else {
                    handleFail(key, response.code(), response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                handleException(key, t);
            }
        };
    }

    protected void handleException(String key, Throwable t) {
        callBack.apiError(key, 999, t);
    }

    protected void handleFail(String key, int code, ResponseBody responseBody) {
        Log.e(TAG, "handleFail: " + code);
        Log.e(TAG, "handleFail: " + responseBody);
        callBack.apiError(key, code, responseBody);
    }

    protected void handleSuccess(String key, Object obj) {
        callBack.apiSuccess(key, obj);
    }
}
