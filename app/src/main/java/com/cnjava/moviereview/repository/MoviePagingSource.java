package com.cnjava.moviereview.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.cnjava.moviereview.data.MovieService;
import com.cnjava.moviereview.model.Movie;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviePagingSource extends RxPagingSource<Integer, Movie.Result> {

    private MovieService movieService;
    private Integer nextPageNumber;
    @NonNull
    private String mQuery;

    public MoviePagingSource(@NonNull MovieService movieService, String query) {
        this.movieService = movieService;
        mQuery = query;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie.Result> pagingState) {
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, Movie.Result> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie.Result>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }
        /*return movieService.searchMoviePaging(mQuery, nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(obj -> new LoadResult.Page<>(
                        obj.results,
                        nextPageNumber == 1 ? null : nextPageNumber - 1,
                        nextPageNumber == obj.total_pages ? null : obj.page + 1))
                .onErrorReturn(LoadResult.Error::new);*/

        return movieService.searchMoviePaging(mQuery, nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(obj -> toLoadResult(nextPageNumber, obj))
                .onErrorReturn(LoadResult.Error::new);

        /*return movieService.searchMoviePaging(mQuery, nextPageNumber)
                .subscribeOn(Schedulers.io())
                .map(this::toLoadResult)
                .onErrorReturn(LoadResult.Error::new);*/
    }

    private LoadResult<Integer, Movie.Result> toLoadResult(Integer page,
            @NonNull Movie response) {
        return new LoadResult.Page<>(
                response.results,
                null, // Only paging forward.
                page == response.total_pages ? null : response.page + 1);
    }


    /*private LoadResult<Integer, Movie> toLoadResult2(
            @NonNull Movie response) {
        LoadResult<Integer, Movie> result = new LoadResult.Page(
                response.results,
                nextPageNumber == 1 ? null : nextPageNumber - 1,
                nextPageNumber == response.total_pages ? null : response.page + 1,
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
        return result;
    }*/
}
