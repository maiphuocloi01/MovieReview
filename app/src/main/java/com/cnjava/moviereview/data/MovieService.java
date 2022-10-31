package com.cnjava.moviereview.data;

import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.Video;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    String API_KEY = "5941024e8620246ad84260d2dfdac7ce";
    @GET("movie/popular?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getPopularMovie();

    @GET("movie/now_playing?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getNowPlayingMovie();

    @GET("movie/upcoming?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getUpcomingMovie();

    @GET("movie/top_rated?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getTopRatedMovie();

    @GET("movie/{id}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<MovieDetail> getMovieDetail(@Path("id") int id);

    @GET("search/movie?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> searchMovie(@Query("query") String keyword);

    @GET("search/keyword?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Keyword> searchKeyword(@Query("query") String keyword);

    @GET("movie/{movie_id}/credits?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Actor> getCast(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/videos?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Video> getVideo(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/recommendations?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getRecommendation(@Path("movie_id") int movie_id);

    @GET("collection/{collection_id}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Collection> getCollection(@Path("collection_id") int collection_id);

    @GET("keyword/{keyword_id}/movies?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getMovieByKeywordId(@Path("keyword_id") int keyword_id);

    @GET("discover/movie?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getMovieByCategory(@Query("sort_by") String sort, @Query("with_genres") String category);

    @GET("movie/{movie_id}/external_ids?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Social> getSocial(@Path("movie_id") int movie_id);
}
