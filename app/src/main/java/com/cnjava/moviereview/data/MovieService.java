package com.cnjava.moviereview.data;

import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.Video;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    //String API_KEY = "5941024e8620246ad84260d2dfdac7ce";
    @GET("movie/popular")
    @Headers("Content-type: application/json")
    Single<Movie> getPopularMovie();

    @GET("movie/now_playing")
    @Headers("Content-type: application/json")
    Single<Movie> getNowPlayingMovie();

    @GET("movie/upcoming")
    @Headers("Content-type: application/json")
    Single<Movie> getUpcomingMovie();

    @GET("movie/top_rated")
    @Headers("Content-type: application/json")
    Single<Movie> getTopRatedMovie();

    @GET("trending/movie/{time_window}")
    @Headers("Content-type: application/json")
    Single<Movie> getTrending(@Path("time_window") String timeWindow);

    @GET("movie/{id}")
    @Headers("Content-type: application/json")
    Single<MovieDetail> getMovieDetail(@Path("id") int id);

    @GET("search/movie")
    @Headers("Content-type: application/json")
    Single<Movie> searchMovie(@Query("query") String keyword);

    @GET("search/movie")
    @Headers("Content-type: application/json")
    Observable<MovieName> autoCompleteSearch(@Query("query") String keyword);

    @GET("search/keyword")
    @Headers("Content-type: application/json")
    Single<Keyword> searchKeyword(@Query("query") String keyword);

    @GET("movie/{movie_id}/credits")
    @Headers("Content-type: application/json")
    Single<Actor> getCast(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/videos")
    @Headers("Content-type: application/json")
    Single<Video> getVideo(@Path("movie_id") int movie_id);

    @GET("movie/{movie_id}/recommendations")
    @Headers("Content-type: application/json")
    Single<Movie> getRecommendation(@Path("movie_id") int movie_id);

    @GET("collection/{collection_id}")
    @Headers("Content-type: application/json")
    Single<Collection> getCollection(@Path("collection_id") int collection_id);

    @GET("keyword/{keyword_id}/movies")
    @Headers("Content-type: application/json")
    Single<Movie> getMovieByKeywordId(@Path("keyword_id") int keyword_id);

    @GET("discover/movie")
    @Headers("Content-type: application/json")
    Single<Movie> getMovieByCategory(@Query("sort_by") String sort, @Query("with_genres") String category);

    @GET("movie/{movie_id}/external_ids")
    @Headers("Content-type: application/json")
    Single<Social> getSocial(@Path("movie_id") int movie_id);

    @GET("credit/{credit_id}")
    @Headers("Content-type: application/json")
    Single<CastDetail> getCastDetail(@Path("credit_id") String credit_id);
}
