package com.cnjava.moviereview.api;

import com.cnjava.moviereview.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface Api {

    String API_KEY = "5941024e8620246ad84260d2dfdac7ce";

    @GET("movie/popular?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Call<Movie> getPopularMovie();

    @GET("movie/now_playing?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Call<Movie> getNowPlayingMovie();

    @GET("movie/upcoming?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Call<Movie> getUpcomingMovie();

    @GET("movie/top_rated?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Call<Movie> getTopRatedMovie();


}
