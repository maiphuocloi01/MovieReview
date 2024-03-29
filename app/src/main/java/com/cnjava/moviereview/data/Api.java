package com.cnjava.moviereview.data;

import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.Video;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    //String API_KEY = "5941024e8620246ad84260d2dfdac7ce";

    @GET("movie/popular")
    Call<Movie> getPopularMovie();

    @GET("movie/now_playing")
    Call<Movie> getNowPlayingMovie();

    @GET("movie/upcoming")
    Call<Movie> getUpcomingMovie();

    @GET("movie/top_rated")
    Call<Movie> getTopRatedMovie();

    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetail(@Path("id") int id);

    @GET("search/movie")
    Call<Movie> searchMovie(@Query("query") String keyword);

    @GET("search/keyword")
    Call<Keyword> searchKeyword(@Query("query") String keyword);

    @GET("movie/{movie_id}/credits")
    Call<Actor> getCast(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/videos")
    Call<Video> getVideo(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/recommendations")
    Call<Movie> getRecommendation(@Path("movie_id") int movie_id);

    @GET("collection/{collection_id}")
    Call<Collection> getCollection(@Path("collection_id") int collection_id);

    @GET("keyword/{keyword_id}/movies")
    Call<Movie> getMovieByKeywordId(@Path("keyword_id") int keyword_id);

    @GET("discover/movie")
    Call<Movie> getMovieByCategory(@Query("sort_by") String sort, @Query("with_genres") String category);

    @GET("movie/{movie_id}/external_ids")
    Call<Social> getSocial(@Path("movie_id") int movie_id);

}
