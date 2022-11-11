package com.cnjava.moviereview.repository;

import com.cnjava.moviereview.data.MovieService;
import com.cnjava.moviereview.data.TranslateService;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.CastDetail;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Keyword;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.MovieName;
import com.cnjava.moviereview.model.MultiMedia;
import com.cnjava.moviereview.model.People;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.Translate;
import com.cnjava.moviereview.model.Video;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieRepository {

    private final TranslateService apiService;
    private final MovieService movieService;

    @Inject
    public MovieRepository(TranslateService apiService, MovieService movieService) {
        this.apiService = apiService;
        this.movieService = movieService;
    }

    public Single<Translate> translateText(String text) {
        return subscribe(apiService.translateText(text));
    }

    public Single<Movie> getPopularMovie() {
        return subscribe(movieService.getPopularMovie());
    }

    public Single<Movie> getNowPlayingMovie() {
        return subscribe(movieService.getNowPlayingMovie());
    }

    public Single<Movie> getUpComingMovie() {
        return subscribe(movieService.getUpcomingMovie());
    }

    public Single<Movie> getTopRatedMovie() {
        return subscribe(movieService.getTopRatedMovie());
    }

    public Single<MovieDetail> getMovieDetail(int id) {
        return subscribe(movieService.getMovieDetail(id));
    }

    public Single<Actor> getCast(int id) {
        return subscribe(movieService.getCast(id));
    }

    public Single<Movie> getRecommendation(int id) {
        return subscribe(movieService.getRecommendation(id));
    }

    public Single<Video> getVideo(int id) {
        return subscribe(movieService.getVideo(id));
    }

    public Single<Social> getSocial(int id) {
        return subscribe(movieService.getSocial(id));
    }

    public Single<Collection> getCollection(int collectionId) {
        return subscribe(movieService.getCollection(collectionId));
    }

    public Single<Movie> getTrending(String timeWindow) {
        return subscribe(movieService.getTrending(timeWindow));
    }

    public Single<Movie> getMovieByKeywordId(int keywordId) {
        return subscribe(movieService.getMovieByKeywordId(keywordId));
    }

    public Single<Movie> getMovieByCategory(String sort, String category) {
        return subscribe(movieService.getMovieByCategory(sort, category));
    }

    public Single<MultiMedia> searchMultiMedia(String query) {
        return subscribe(movieService.searchMultiMedia(query));
    }

    public Single<CastDetail> getCastDetail(String creditId) {
        return subscribe(movieService.getCastDetail(creditId));
    }

    public Observable<MovieName> autoCompleteSearch(String queryString) {
        return subscribeObservable(movieService.autoCompleteSearch(queryString));
    }

    public Single<Movie> searchMovie(String queryString) {
        return subscribe(movieService.searchMovie(queryString));
    }

    public Single<People> getPeopleDetail(String personId) {
        return subscribe(movieService.getPeopleDetail(personId));
    }

    public Single<Keyword> searchKeyword(String keyword) {
        return subscribe(movieService.searchKeyword(keyword));
    }




    private <T> Single<T> subscribe(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private <T> Observable<T> subscribeObservable(Observable<T> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
