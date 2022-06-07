package com.cnjava.moviereview.api;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {

    @POST("auth/registration")
    @Headers("Content-type: application/json")
    Call<User> registration(@Body User user);

    @POST("auth/otp")
    @Headers("Content-type: application/json")
    Call<Response> sendOTP(@Body User user);

    @PUT("auth/otp/confirm")
    @Headers("Content-type: application/json")
    Call<Response> confirmOTP(@Body Response response);

    @PUT("auth/forgot-password ")
    @Headers("Content-type: application/json")
    Call<String> forgotPassword(@Body User user);

    @GET("users/{user_id}")
    @Headers("Content-type: application/json")
    Call<User> getUserById(@Path("user_id") String user_id);

    @POST("auth/login")
    @FormUrlEncoded
    Call<Response> login(@Field("email") String username, @Field("password") String password);

    @GET("users/me")
    @Headers("Content-type: application/json")
    Call<User> getYourProfile(@Header("Authorization") String auth);
}
