package com.cnjava.moviereview.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.cnjava.moviereview.data.MovieService;
import com.cnjava.moviereview.model.Movie;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviePagingSource extends RxPagingSource<Integer, Movie.Result> {

    private MovieService movieService;
    private Integer nextPageNumber;
    @NonNull
    private String mQuery;

    @Inject
    public MoviePagingSource(@NonNull MovieService movieService,
                             @NonNull String query) {
        this.movieService = movieService;
        mQuery = query;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie.Result> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie.Result>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }
        return movieService.searchMoviePaging(mQuery, nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new);
    }

    private LoadResult<Integer, Movie.Result> toLoadResult(
            @NonNull Movie response) {
        return new LoadResult.Page<>(
                response.results,
                nextPageNumber == 1 ? null : nextPageNumber - 1, // Only paging forward.
                response.page + 1,
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }
}
