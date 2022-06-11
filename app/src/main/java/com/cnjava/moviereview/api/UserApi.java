package com.cnjava.moviereview.api;

import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.view.adapter.Summary;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
import retrofit2.http.Query;

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
    Call<Void> forgotPassword(@Body User user);

    @GET("users/{user_id}")
    @Headers("Content-type: application/json")
    Call<User> getUserById(@Path("user_id") String user_id);

    @POST("auth/login")
    @FormUrlEncoded
    Call<Response> login(@Field("email") String username, @Field("password") String password);

    @GET("users/me")
    @Headers("Content-type: application/json")
    Call<User> getYourProfile(@Header("Authorization") String auth);

    @PUT("users/me")
    @Headers("Content-type: application/json")
    Call<Void> updateProfile(@Body User user, @Header("Authorization") String auth);

    @Multipart
    @POST("Api/AccountController/UploadImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody);

    //Review
    @GET("reviews/movies/{movie_id}")
    @Headers("Content-type: application/json")
    Call<List<Review>> getReviewByMovieId(@Path("movie_id") String movie_id);

    @GET("reviews/users/{user_id} ")
    @Headers("Content-type: application/json")
    Call<List<Review>> getReviewByUserId(@Path("user_id") String user_id);

    @GET("reviews/{review_id}")
    @Headers("Content-type: application/json")
    Call<Review> getReviewById(@Path("review_id") String review_id);

    @POST("reviews")
    @Headers("Content-type: application/json")
    Call<Review> addReview(@Body Review review, @Header("Authorization") String auth);

    @PUT("reviews/{review_id}/like")
    @Headers("Content-type: application/json")
    Call<Void> likeReview(@Path("review_id") String review_id, @Header("Authorization") String auth);

    @PUT("reviews/{review_id}/dislike")
    @Headers("Content-type: application/json")
    Call<Void> dislikeReview(@Path("review_id") String review_id, @Header("Authorization") String auth);

    @GET("summarization")
    @Headers("Content-type: application/json")
    Call<Summary> summarizationReview(@Query("inputs") String inputs);

}
