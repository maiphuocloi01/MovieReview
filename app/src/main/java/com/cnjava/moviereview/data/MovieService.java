package com.cnjava.moviereview.data;

import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.model.MultiMedia;
import com.cnjava.moviereview.model.People;
import com.cnjava.moviereview.model.PeopleImage;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.Video;

import io.reactivex.rxjava3.core.Observable;
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

    @GET("movie/now_playing?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getNowPlayingMoviePaging(@Query("page") int page);

    @GET("movie/upcoming?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getUpcomingMovie();

    @GET("movie/upcoming?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getUpcomingMoviePaging(@Query("page") int page);

    @GET("movie/top_rated?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getTopRatedMovie();

    @GET("movie/top_rated?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getTopRatedMoviePaging(@Query("page") int page);

    @GET("trending/movie/{time_window}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getTrending(@Path("time_window") String timeWindow);

    @GET("movie/{id}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<MovieDetail> getMovieDetail(@Path("id") int id);

    @GET("search/movie?api_key=" + API_KEY + "&include_adult=false")
    @Headers("Content-type: application/json")
    Single<Movie> searchMovie(@Query("query") String keyword);

    @GET("search/movie?api_key=" + API_KEY + "&include_adult=false")
    @Headers("Content-type: application/json")
    Single<Movie> searchMoviePaging(@Query("query") String keyword, @Query("page") int page);

    @GET("movie/{movie_id}/recommendations?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Movie> getRecommendationPaging(@Path("movie_id") String movie_id, @Query("page") int page);

    @GET("search/multi?api_key=" + API_KEY + "&include_adult=false")
    @Headers("Content-type: application/json")
    Single<MultiMedia> searchMultiMedia(@Query("query") String query);

    @GET("search/movie?api_key=" + API_KEY + "&include_adult=false")
    @Headers("Content-type: application/json")
    Observable<MovieName> autoCompleteSearch(@Query("query") String keyword);

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
    Single<Movie> getMovieByCategory(@Query("with_genres") String category, @Query("page") int page);

    @GET("movie/{movie_id}/external_ids?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<Social> getSocial(@Path("movie_id") int movie_id);

    @GET("credit/{credit_id}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<CastDetail> getCastDetail(@Path("credit_id") String credit_id);

    @GET("person/{person_id}?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<People> getPeopleDetail(@Path("person_id") String person_id);

    @GET("person/{person_id}/images?api_key=" + API_KEY)
    @Headers("Content-type: application/json")
    Single<PeopleImage> getPeopleImage(@Path("person_id") String person_id);
}
