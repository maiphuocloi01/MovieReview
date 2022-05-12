package com.cnjava.moviereview.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.cnjava.moviereview.api.Api;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.view.callback.OnAPICallBack;

import java.util.concurrent.TimeUnit;

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

    public void setCallBack(OnAPICallBack callBack) {
        this.callBack = callBack;
    }

    protected Api getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build())
                .build();
        return retrofit.create(Api.class);
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
