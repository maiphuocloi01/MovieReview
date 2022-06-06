package com.cnjava.moviereview.api;

import com.cnjava.moviereview.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApi {

    @POST("auth/registration")
    @Headers("Content-type: application/json")
    Call<User> registration(@Body User user);

}
