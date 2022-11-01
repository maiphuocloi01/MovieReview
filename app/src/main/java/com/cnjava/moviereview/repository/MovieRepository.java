package com.cnjava.moviereview.repository;

import com.cnjava.moviereview.data.MovieService;
import com.cnjava.moviereview.data.TranslateService;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.Translate;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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

    public Single<Movie> getPopularMovie(){
        return subscribe(movieService.getPopularMovie());
    }

    public Single<Movie> getNowPlayingMovie(){
        return subscribe(movieService.getNowPlayingMovie());
    }

    public Single<Movie> getUpComingMovie(){
        return subscribe(movieService.getUpcomingMovie());
    }

    public Single<Movie> getTopRatedMovie(){
        return subscribe(movieService.getTopRatedMovie());
    }

    private <T> Single<T> subscribe(Single<T> single) {
        return single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
