package com.cnjava.moviereview.data;

import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Response;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Statistic;
import com.cnjava.moviereview.model.Summary;
import com.cnjava.moviereview.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

public interface AccountService {
    
    @POST("auth/registration")
    @Headers("Content-type: application/json")
    Single<User> registration(@Body User user);

    @POST("auth/otp")
    @Headers("Content-type: application/json")
    Single<Response> sendOTP(@Body User user);

    @PUT("auth/otp/confirm")
    @Headers("Content-type: application/json")
    Single<Response> confirmOTP(@Body Response response);

    @PUT("auth/forgot-password ")
    @Headers("Content-type: application/json")
    Single<Void> forgotPassword(@Body User user);

    @GET("users/{user_id}")
    @Headers("Content-type: application/json")
    Single<User> getUserById(@Path("user_id") String user_id);

    @POST("auth/login")
    @FormUrlEncoded
    Single<Response> login(@Field("email") String username, @Field("password") String password);

    @GET("users/me")
    @Headers("Content-type: application/json")
    Single<User> getYourProfile(@Header("Authorization") String auth);

    @PUT("users/me")
    @Headers("Content-type: application/json")
    Single<Void> updateProfile(@Body User user, @Header("Authorization") String auth);

    @Multipart
    @POST("Api/AccountController/UploadImage")
    Single<ResponseBody> uploadImage(@Part MultipartBody.Part part, @Part("somedata") RequestBody requestBody);

    //Review
    @GET("reviews/movies/{movie_id}")
    @Headers("Content-type: application/json")
    Single<List<Review>> getReviewByMovieId(@Path("movie_id") String movie_id);

    @GET("reviews/users/{user_id} ")
    @Headers("Content-type: application/json")
    Single<List<Review>> getReviewByUserId(@Path("user_id") String user_id);

    @GET("reviews/{review_id}")
    @Headers("Content-type: application/json")
    Single<Review> getReviewById(@Path("review_id") String review_id);

    @POST("reviews")
    @Headers("Content-type: application/json")
    Single<Review> addReview(@Body Review review, @Header("Authorization") String auth);

    @PUT("reviews/{review_id}")
    @Headers("Content-type: application/json")
    Single<Review> updateReview(@Path("review_id") String review_id, @Body Review review, @Header("Authorization") String auth);

    @DELETE("reviews/{review_id}")
    @Headers("Content-type: application/json")
    Single<Void> deleteReview(@Path("review_id") String review_id, @Header("Authorization") String auth);

    @PUT("reviews/{review_id}/like")
    @Headers("Content-type: application/json")
    Single<Void> likeReview(@Path("review_id") String review_id, @Header("Authorization") String auth);

    @PUT("reviews/{review_id}/dislike")
    @Headers("Content-type: application/json")
    Single<Void> dislikeReview(@Path("review_id") String review_id, @Header("Authorization") String auth);

    @GET("users/me/statistics")
    @Headers("Content-type: application/json")
    Single<Statistic> getMyStatistics(@Header("Authorization") String auth);

    @GET("users/{user_id}/statistics")
    @Headers("Content-type: application/json")
    Single<Statistic> getStatisticsByUserId(@Path("user_id") String user_id);

    @GET("summarization")
    @Headers("Content-type: application/json")
    Single<Summary> summarizationReview(@Query("inputs") String inputs);

    @GET("favorites")
    @Headers("Content-type: application/json")
    Single<List<Favorite>> getMyFavorite(@Header("Authorization") String auth);

    @POST("favorites")
    @Headers("Content-type: application/json")
    Single<Favorite> addFavorite(@Body Favorite.MovieFavorite movie, @Header("Authorization") String auth);

    @DELETE("favorites/{id}")
    @Headers("Content-type: application/json")
    Single<Void> deleteFavorite(@Path("id") String id, @Header("Authorization") String auth);
}
