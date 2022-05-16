package com.cnjava.moviereview.api;

import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("movie/{id}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Call<MovieDetail> getMovieDetail(@Path("id") int id);

    @GET("search/movie?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Call<Movie> searchMovie(@Query("query") String keyword);

}
